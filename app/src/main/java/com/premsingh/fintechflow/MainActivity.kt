package com.premsingh.fintechflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF17212B)
private val Muted = Color(0xFF75818B)
private val Canvas = Color(0xFFF4F7F8)
private val Mint = Color(0xFFB9F2D5)
private val Navy = Color(0xFF173C49)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = lightColorScheme(
                primary = Navy,
                background = Canvas,
                surface = Color.White,
                onBackground = Ink,
                onSurface = Ink
            )) {
                DashboardScreen()
            }
        }
    }
}

@Composable
private fun DashboardScreen() {
    Surface(modifier = Modifier.fillMaxSize(), color = Canvas) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 20.dp, top = 24.dp, end = 20.dp, bottom = 32.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Good morning,", color = Muted, fontSize = 14.sp)
                        Text("Alex Morgan", color = Ink, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                    Surface(shape = CircleShape, color = Mint, modifier = Modifier.size(44.dp)) {
                        androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                            Text("AM", color = Navy, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
            item { BalanceCard() }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Quick actions", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        ActionCard("↗", "Send", Modifier.weight(1f))
                        ActionCard("＋", "Add money", Modifier.weight(1f))
                        ActionCard("▤", "Cards", Modifier.weight(1f))
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recent activity", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Text("See all", color = Navy, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            items(sampleTransactions) { transaction -> TransactionRow(transaction) }
            item {
                Text(
                    "Demo interface · fictional data · no real financial services",
                    color = Muted,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun BalanceCard() {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Navy),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text("TOTAL BALANCE", color = Color(0xFFB9C9CF), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            Text("$12,480.50", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text("••••  4829", color = Color(0xFFD4E0E3), fontSize = 14.sp)
                Text("+ 4.8% this month", color = Mint, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun ActionCard(symbol: String, title: String, modifier: Modifier = Modifier) {
    Card(
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
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(shape = RoundedCornerShape(14.dp), color = transaction.tint, modifier = Modifier.size(46.dp)) {
            androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                Text(transaction.initial, color = Navy, fontWeight = FontWeight.Bold)
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(transaction.merchant, color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(transaction.date, color = Muted, fontSize = 12.sp)
        }
        Text(transaction.amount, color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }
}

private data class Transaction(
    val merchant: String,
    val date: String,
    val amount: String,
    val initial: String,
    val tint: Color
)

private val sampleTransactions = listOf(
    Transaction("Northstar Coffee", "Today · 9:41 AM", "−$5.80", "N", Color(0xFFE6E9FF)),
    Transaction("Metro Market", "Yesterday · 6:12 PM", "−$42.35", "M", Color(0xFFFFE9D8)),
    Transaction("Salary deposit", "Oct 14 · 8:00 AM", "+$3,200.00", "S", Color(0xFFDDF4E7)),
    Transaction("Streamline Music", "Oct 12 · 2:16 PM", "−$10.99", "S", Color(0xFFFFE1EB))
)
