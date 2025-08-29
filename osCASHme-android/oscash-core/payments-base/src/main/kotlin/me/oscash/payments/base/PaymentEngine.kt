/*
 * Copyright 2025 osCASH.me Contributors
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package me.oscash.payments.base

import org.whispersystems.signalservice.api.payments.Money
import java.math.BigDecimal

/**
 * Core payment engine interface for osCASH.me
 * 
 * This interface abstracts the underlying payment system (MobileCoin)
 * and provides a clean API for payment operations.
 */
interface PaymentEngine {
    
    /**
     * Enable payments functionality
     * This will override Molly's default payment disabling
     */
    suspend fun enablePayments(): Boolean
    
    /**
     * Check if payments are currently enabled
     */
    fun isPaymentsEnabled(): Boolean
    
    /**
     * Get current MobileCoin balance
     */
    suspend fun getMobileCoinBalance(): Money.MobileCoin
    
    /**
     * Create a new transaction
     * 
     * @param recipient The recipient's address/identifier
     * @param amount The amount to send
     * @param memo Optional memo for the transaction
     */
    suspend fun createTransaction(
        recipient: String,
        amount: Money.MobileCoin,
        memo: String? = null
    ): PaymentTransaction
    
    /**
     * Get transaction history
     */
    suspend fun getTransactionHistory(): List<PaymentTransaction>
    
    /**
     * Generate a payment request QR code data
     */
    fun generatePaymentRequest(
        amount: Money.MobileCoin,
        memo: String? = null
    ): PaymentRequest
}

/**
 * Represents a payment transaction
 */
data class PaymentTransaction(
    val id: String,
    val from: String,
    val to: String,
    val amount: Money.MobileCoin,
    val memo: String?,
    val timestamp: Long,
    val status: TransactionStatus,
    val blockHeight: Long? = null,
    val confirmations: Int = 0
)

/**
 * Transaction status enum
 */
enum class TransactionStatus {
    PENDING,
    CONFIRMED,
    FAILED,
    CANCELLED
}

/**
 * Payment request for QR code generation
 */
data class PaymentRequest(
    val amount: Money.MobileCoin,
    val recipient: String? = null,
    val memo: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val validUntil: Long? = null
)