package com.premsingh.fintechflow.domain

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

/** Parses a positive decimal amount into minor currency units without rounding. */
fun parseAmountMinor(input: String): Long? {
    val normalized = input.trim()
    if (!normalized.matches(Regex("\\d+(?:\\.\\d{1,2})?"))) return null
    return runCatching {
        BigDecimal(normalized).movePointRight(2).setScale(0, RoundingMode.UNNECESSARY).longValueExact()
    }.getOrNull()?.takeIf { it > 0 }
}

fun formatUsd(amountMinor: Long): String = NumberFormat.getCurrencyInstance(Locale.US)
    .format(amountMinor / 100.0)
