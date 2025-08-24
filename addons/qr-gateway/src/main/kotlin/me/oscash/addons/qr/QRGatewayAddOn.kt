/*
 * Copyright 2025 osCASH.me Contributors
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package me.oscash.addons.qr

import android.content.Context
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import me.oscash.addon.AddOn
import me.oscash.addon.Capability
import me.oscash.addon.CapabilityType
import me.oscash.addon.HealthStatus
import me.oscash.payments.base.PaymentRequest
import org.json.JSONObject
import org.signal.core.util.logging.Log
import org.whispersystems.signalservice.api.payments.Money

/**
 * QR Gateway Add-On for osCASH.me
 * 
 * Provides universal QR code generation and parsing for payments.
 * Supports multiple payment methods: MobileCoin, SENTZ, Signal, Lightning, etc.
 */
class QRGatewayAddOn : AddOn {
    
    companion object {
        private val TAG = Log.tag(QRGatewayAddOn::class.java)
        private const val QR_VERSION = "1.0"
    }
    
    override val id: String = "qr-gateway"
    override val name: String = "QR Payment Gateway"
    override val version: String = "1.0.0"
    override val dependencies: List<String> = listOf("payments-base")
    
    private var initialized = false
    
    override suspend fun initialize(context: Context): Boolean {
        Log.i(TAG, "Initializing QR Gateway Add-On")
        
        return try {
            // Initialize QR code library
            // Setup payment method handlers
            initialized = true
            Log.i(TAG, "QR Gateway Add-On initialized successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize QR Gateway Add-On", e)
            false
        }
    }
    
    override suspend fun shutdown() {
        Log.i(TAG, "Shutting down QR Gateway Add-On")
        initialized = false
    }
    
    override fun getCapabilities(): List<Capability> {
        return listOf(
            Capability(
                type = CapabilityType.PAYMENT_GATEWAY,
                name = "QR Code Generation",
                description = "Generate QR codes for payment requests",
                version = version
            ),
            Capability(
                type = CapabilityType.PAYMENT_GATEWAY,
                name = "QR Code Parsing",
                description = "Parse payment requests from QR codes",
                version = version
            )
        )
    }
    
    override suspend fun healthCheck(): HealthStatus {
        return HealthStatus(
            isHealthy = initialized,
            metrics = mapOf(
                "initialized" to initialized,
                "version" to version
            )
        )
    }
    
    override fun getConfigSchema() = null
    
    /**
     * Generate a QR code for a payment request
     * 
     * @param paymentRequest The payment request to encode
     * @param size The size of the QR code bitmap
     * @return Bitmap containing the QR code
     */
    fun generatePaymentQR(
        paymentRequest: PaymentRequest,
        size: Int = 512
    ): Bitmap? {
        if (!initialized) {
            Log.e(TAG, "QR Gateway not initialized")
            return null
        }
        
        return try {
            val qrData = createUniversalPaymentQR(paymentRequest)
            val encoder = BarcodeEncoder()
            encoder.encodeBitmap(qrData, BarcodeFormat.QR_CODE, size, size)
        } catch (e: WriterException) {
            Log.e(TAG, "Failed to generate QR code", e)
            null
        }
    }
    
    /**
     * Parse a payment request from QR code data
     * 
     * @param qrData The raw QR code data string
     * @return Parsed payment request or null if invalid
     */
    fun parsePaymentQR(qrData: String): UniversalPaymentRequest? {
        if (!initialized) {
            Log.e(TAG, "QR Gateway not initialized")
            return null
        }
        
        return try {
            parseUniversalPaymentQR(qrData)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse QR code", e)
            null
        }
    }
    
    /**
     * Create universal payment QR format
     * 
     * Format supports multiple payment methods and is extensible
     */
    private fun createUniversalPaymentQR(paymentRequest: PaymentRequest): String {
        val qrPayload = JSONObject().apply {
            put("v", QR_VERSION) // Version
            put("t", "payment") // Type
            put("ts", paymentRequest.timestamp) // Timestamp
            
            // Amount (always in smallest unit)
            put("amount", paymentRequest.amount.serialize())
            put("currency", "MOB")
            
            // Optional fields
            paymentRequest.recipient?.let { put("recipient", it) }
            paymentRequest.memo?.let { put("memo", it) }
            paymentRequest.validUntil?.let { put("validUntil", it) }
            
            // Supported payment methods
            put("methods", JSONObject().apply {
                put("mobilecoin", true)
                put("signal", true)
                put("sentz", true)
                // Future: Lightning, Bitcoin, etc.
            })
            
            // osCASH.me identifier
            put("source", "oscash.me")
        }
        
        return "oscash:${qrPayload}"
    }
    
    /**
     * Parse universal payment QR format
     */
    private fun parseUniversalPaymentQR(qrData: String): UniversalPaymentRequest? {
        // Support multiple formats
        return when {
            qrData.startsWith("oscash:") -> parseOsCashFormat(qrData)
            qrData.startsWith("signal:") -> parseSignalFormat(qrData)
            qrData.startsWith("lightning:") -> parseLightningFormat(qrData)
            qrData.startsWith("bitcoin:") -> parseBitcoinFormat(qrData)
            else -> {
                // Try to parse as generic JSON
                try {
                    val json = JSONObject(qrData)
                    parseGenericFormat(json)
                } catch (e: Exception) {
                    Log.w(TAG, "Unknown QR format: $qrData")
                    null
                }
            }
        }
    }
    
    private fun parseOsCashFormat(qrData: String): UniversalPaymentRequest? {
        val jsonData = qrData.removePrefix("oscash:")
        val json = JSONObject(jsonData)
        
        return UniversalPaymentRequest(
            amount = Money.parse(json.getString("amount")),
            currency = json.optString("currency", "MOB"),
            recipient = json.optString("recipient"),
            memo = json.optString("memo"),
            timestamp = json.optLong("ts"),
            validUntil = json.optLong("validUntil"),
            supportedMethods = parsePaymentMethods(json.optJSONObject("methods")),
            source = json.optString("source", "unknown")
        )
    }
    
    private fun parseSignalFormat(qrData: String): UniversalPaymentRequest? {
        // TODO: Implement Signal payment QR parsing
        Log.d(TAG, "Parsing Signal format: $qrData")
        return null
    }
    
    private fun parseLightningFormat(qrData: String): UniversalPaymentRequest? {
        // TODO: Implement Lightning invoice parsing
        Log.d(TAG, "Parsing Lightning format: $qrData")
        return null
    }
    
    private fun parseBitcoinFormat(qrData: String): UniversalPaymentRequest? {
        // TODO: Implement Bitcoin URI parsing (BIP-21)
        Log.d(TAG, "Parsing Bitcoin format: $qrData")
        return null
    }
    
    private fun parseGenericFormat(json: JSONObject): UniversalPaymentRequest? {
        // TODO: Implement generic JSON payment format
        Log.d(TAG, "Parsing generic format")
        return null
    }
    
    private fun parsePaymentMethods(methodsJson: JSONObject?): List<PaymentMethod> {
        if (methodsJson == null) return emptyList()
        
        val methods = mutableListOf<PaymentMethod>()
        
        if (methodsJson.optBoolean("mobilecoin")) {
            methods.add(PaymentMethod.MOBILECOIN)
        }
        if (methodsJson.optBoolean("signal")) {
            methods.add(PaymentMethod.SIGNAL)
        }
        if (methodsJson.optBoolean("sentz")) {
            methods.add(PaymentMethod.SENTZ)
        }
        if (methodsJson.optBoolean("lightning")) {
            methods.add(PaymentMethod.LIGHTNING)
        }
        
        return methods
    }
}

/**
 * Universal payment request that supports multiple payment methods
 */
data class UniversalPaymentRequest(
    val amount: Money,
    val currency: String,
    val recipient: String?,
    val memo: String?,
    val timestamp: Long,
    val validUntil: Long?,
    val supportedMethods: List<PaymentMethod>,
    val source: String
)

/**
 * Supported payment methods
 */
enum class PaymentMethod {
    MOBILECOIN,
    SIGNAL,
    SENTZ,
    LIGHTNING,
    BITCOIN,
    ETHEREUM
}