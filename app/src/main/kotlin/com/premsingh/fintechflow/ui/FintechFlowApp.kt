package com.premsingh.fintechflow.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.premsingh.fintechflow.domain.DashboardState
import com.premsingh.fintechflow.domain.Transaction
import com.premsingh.fintechflow.domain.TransferResult
import com.premsingh.fintechflow.domain.formatUsd

private val Ink = Color(0xFF17212B)
private val Muted = Color(0xFF75818B)
private val Canvas = Color(0xFFF4F7F8)
private val Mint = Color(0xFFB9F2D5)
private val Navy = Color(0xFF173C49)

private enum class Destination(val label: String, val marker: String) {
    Home("Home", "⌂"), Activity("Activity", "↕"), Transfer("Pay", "↗"), Cards("Cards", "▣"), Insights("Insights", "▥")
}

private data class FinancePalette(val label: String, val primary: Color, val canvas: Color, val accent: Color)
private val financePalettes = listOf(
    FinancePalette("Harbor", Color(0xFF173C49), Color(0xFFF4F7F8), Color(0xFFB9F2D5)),
    FinancePalette("Evergreen", Color(0xFF315D48), Color(0xFFF2F6F0), Color(0xFFD9EAD7)),
    FinancePalette("Midnight", Color(0xFF14232C), Color(0xFFEAF0F2), Color(0xFFC4DDF0))
)

@Composable
fun FintechFlowApp(financeViewModel: FinanceViewModel = viewModel()) {
    val dashboard by financeViewModel.dashboard.collectAsStateWithLifecycle()
    var destination by rememberSaveable { mutableStateOf(Destination.Home) }
    var paletteIndex by rememberSaveable { mutableIntStateOf(0) }
    val palette = financePalettes[paletteIndex]
    MaterialTheme(colorScheme = lightColorScheme(
        primary = palette.primary,
        secondary = palette.accent,
        background = palette.canvas,
        surface = Color.White,
        onBackground = Ink,
        onSurface = Ink
    )) {
        Scaffold(
            containerColor = palette.canvas,
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    Destination.entries.forEach { item ->
                        NavigationBarItem(
                            selected = destination == item,
                            onClick = { destination = item },
                            icon = { Text(item.marker, fontSize = 18.sp) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        ) { padding ->
            when (destination) {
                Destination.Home -> HomeScreen(dashboard, padding, onTransfer = { destination = Destination.Transfer }, onActivity = { destination = Destination.Activity })
                Destination.Activity -> ActivityScreen(dashboard, padding)
                Destination.Transfer -> TransferScreen(
                    dashboard = dashboard,
                    padding = padding,
                    onSend = financeViewModel::sendTransfer
                )
                Destination.Cards -> CardsScreen(dashboard, padding, paletteIndex, onPaletteChange = { paletteIndex = it })
                Destination.Insights -> InsightsScreen(dashboard, padding)
            }
        }
    }
}

@Composable
private fun HomeScreen(state: DashboardState, padding: PaddingValues, onTransfer: () -> Unit, onActivity: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(start = 20.dp, top = 24.dp, end = 20.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Good morning,", color = Muted, fontSize = 14.sp)
                    Text(state.accountHolder, color = Ink, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Surface(shape = CircleShape, color = Mint, modifier = Modifier.size(44.dp)) {
                    androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                        Text("AM", color = Navy, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
        item { BalanceCard(state.balanceMinor, state.accountLastFour) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                ActionCard("↗", "Send money", Modifier.weight(1f), onTransfer)
                ActionCard("＋", "Add money", Modifier.weight(1f), onTransfer)
                ActionCard("▣", "Cards", Modifier.weight(1f), onTransfer)
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Recent activity", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                TextButton(onClick = onActivity) { Text("See all", color = Navy) }
            }
        }
        items(state.transactions.take(3), key = { it.id }) { TransactionRow(it) }
        item { SafetyNote() }
    }
}

@Composable
private fun ActivityScreen(state: DashboardState, padding: PaddingValues) {
    var selectedCategory by rememberSaveable { mutableStateOf("All") }
    val categories = listOf("All", "Income", "Food & drink", "Shopping")
    val visible = state.transactions.filter { selectedCategory == "All" || it.category == selectedCategory }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Text("Activity", color = Ink, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Your recent sample transactions", color = Muted, modifier = Modifier.padding(top = 4.dp))
        }
        item { Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { categories.forEach { category -> FilterChip(selected = selectedCategory == category, onClick = { selectedCategory = category }, label = { Text(category) }) } } }
        items(visible, key = { it.id }) { TransactionRow(it) }
        item { SafetyNote() }
    }
}

@Composable
private fun TransferScreen(
    dashboard: DashboardState,
    padding: PaddingValues,
    onSend: (String, String) -> TransferResult
) {
    var recipient by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf<String?>(null) }
    var succeeded by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Send a demo transfer", color = Ink, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("Available balance  ${formatUsd(dashboard.balanceMinor)}", color = Muted)
        OutlinedTextField(
            value = recipient,
            onValueChange = { recipient = it; message = null },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Recipient") },
            supportingText = { Text("Use a fictional name") },
            singleLine = true
        )
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it; message = null },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Amount") },
            leadingIcon = { Text("\$", modifier = Modifier.padding(start = 12.dp), color = Muted) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )
        Button(
            onClick = {
                when (val result = onSend(recipient, amount)) {
                    is TransferResult.Sent -> { message = "Demo transfer sent to ${result.transaction.merchant}."; succeeded = true; recipient = ""; amount = "" }
                    TransferResult.MissingRecipient -> { message = "Enter a recipient name."; succeeded = false }
                    TransferResult.InvalidAmount -> { message = "Enter an amount greater than zero with up to two decimals."; succeeded = false }
                    TransferResult.InsufficientFunds -> { message = "The sample balance is too low for this amount."; succeeded = false }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Navy)
        ) { Text("Review demo transfer", modifier = Modifier.padding(vertical = 6.dp)) }
        message?.let { Text(it, color = if (succeeded) Color(0xFF246747) else Color(0xFFB3261E)) }
        SafetyNote()
    }
}

@Composable
private fun CardsScreen(state: DashboardState, padding: PaddingValues, paletteIndex: Int, onPaletteChange: (Int) -> Unit) {
    var frozen by rememberSaveable { mutableStateOf(false) }
    var monthlyLimit by rememberSaveable { mutableStateOf("1200") }
    Column(
        modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Your card", color = Ink, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        BalanceCard(state.balanceMinor, state.accountLastFour)
        Text("Virtual demo card", color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        Text("Online purchases · sample card only", color = Muted)
        androidx.compose.material3.OutlinedButton(onClick = { frozen = !frozen }, modifier = Modifier.fillMaxWidth()) { Text(if (frozen) "Unfreeze demo card" else "Freeze demo card") }
        Text(if (frozen) "Card is paused in this demo." else "Card is ready in this demo.", color = Muted, fontSize = 13.sp)
        Text("Monthly spend limit", color = Ink, fontWeight = FontWeight.SemiBold)
        OutlinedTextField(monthlyLimit, { monthlyLimit = it.filter(Char::isDigit).take(5) }, modifier = Modifier.fillMaxWidth(), label = { Text("USD limit") }, leadingIcon = { Text("$", modifier = Modifier.padding(start = 12.dp)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
        Text("App theme", color = Ink, fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { financePalettes.forEachIndexed { index, option -> FilterChip(selected = paletteIndex == index, onClick = { onPaletteChange(index) }, label = { Text(option.label) }) } }
        Text("This screen does not issue or connect to a real payment card.", color = Muted, fontSize = 13.sp)
    }
}

@Composable
private fun InsightsScreen(state: DashboardState, padding: PaddingValues) {
    val income = state.transactions.filter { it.isCredit }.sumOf { it.amountMinor }
    val spending = state.transactions.filterNot { it.isCredit }.sumOf { it.amountMinor }
    Column(Modifier.fillMaxSize().padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Spending insights", color = Ink, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("A snapshot from your fictional demo activity", color = Muted)
        MetricCard("Income this month", formatUsd(income), Color(0xFFE0F3E8))
        MetricCard("Spending this month", formatUsd(spending), Color(0xFFFFE9D8))
        Text("Top categories", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        state.transactions.filterNot { it.isCredit }.groupBy { it.category }.forEach { (category, transactions) ->
            val total = transactions.sumOf { it.amountMinor }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(category, color = Ink); Text(formatUsd(total), color = Muted) }
                androidx.compose.material3.LinearProgressIndicator(progress = { (total.toFloat() / spending.coerceAtLeast(1)).coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth(), color = Navy)
            }
        }
        SafetyNote()
    }
}

@Composable
private fun MetricCard(title: String, value: String, color: Color) {
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = color), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp)) { Text(title, color = Muted); Text(value, color = Ink, fontSize = 24.sp, fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun BalanceCard(balanceMinor: Long, lastFour: String) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Navy),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text("TOTAL BALANCE", color = Color(0xFFB9C9CF), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            Text(formatUsd(balanceMinor), color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(18.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Text("••••  $lastFour", color = Color(0xFFD4E0E3), fontSize = 14.sp)
                Text("DEMO ACCOUNT", color = Mint, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun ActionCard(symbol: String, title: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(symbol, color = Navy, fontSize = 22.sp, fontWeight = FontWeight.Medium)
            Text(title, color = Ink, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun TransactionRow(transaction: Transaction) {
    val tint = when (transaction.category) {
        "Income" -> Color(0xFFDDF4E7)
        "Food & drink" -> Color(0xFFE6E9FF)
        "Shopping" -> Color(0xFFFFE9D8)
        else -> Color(0xFFFFE1EB)
    }
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(shape = RoundedCornerShape(14.dp), color = tint, modifier = Modifier.size(46.dp)) {
            androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                Text(transaction.merchant.take(1), color = Navy, fontWeight = FontWeight.Bold)
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(transaction.merchant, color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text("${transaction.category} · ${transaction.dateLabel}", color = Muted, fontSize = 12.sp)
        }
        Text(
            (if (transaction.isCredit) "+" else "−") + formatUsd(transaction.amountMinor),
            color = if (transaction.isCredit) Color(0xFF246747) else Ink,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun SafetyNote() {
    Text(
        "Portfolio demo · fictional data · no real financial services",
        color = Muted,
        fontSize = 12.sp,
        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
    )
}
