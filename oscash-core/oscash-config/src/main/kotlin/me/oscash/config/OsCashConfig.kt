/*
 * Copyright 2025 osCASH.me Contributors
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package me.oscash.config

/**
 * Central configuration for osCASH.me
 * 
 * This class manages feature flags, build variants, and add-on configurations
 * that make osCASH.me different from Molly while maintaining compatibility.
 */
object OsCashConfig {
    
    /**
     * osCASH.me version information
     */
    const val VERSION = BuildConfig.OSCASH_VERSION
    const val MOLLY_VERSION = "7.53.4"
    const val BUILD_FLAVOR = "oscash"
    
    /**
     * Feature flags that override Molly defaults
     */
    object Features {
        // Payment-related features
        const val PAYMENTS_ENABLED = true
        const val MOBILECOIN_ENABLED = true
        const val QR_PAYMENTS_ENABLED = true
        
        // osCASH.me specific features
        const val ADDON_SYSTEM_ENABLED = true
        const val MULTICHAIN_BRIDGE_ENABLED = true
        const val MESH_NETWORK_ENABLED = false // Optional feature
        
        // Integration features
        const val SENTZ_INTEGRATION_ENABLED = true
        const val SIGNAL_PAYMENTS_ENABLED = true
        
        // Privacy features
        const val ENHANCED_PRIVACY_MODE = true
        const val OFFLINE_PAYMENTS = false // Future feature
    }
    
    /**
     * Network configuration
     */
    object Network {
        const val USE_MOLLY_SERVERS = true
        const val PAYMENT_SERVER_OVERRIDE = false
        
        // osCASH.me specific endpoints (if needed)
        const val OSCASH_API_BASE = "https://api.oscash.me"
        const val ADDON_REGISTRY_URL = "https://addons.oscash.me"
    }
    
    /**
     * Security settings
     */
    object Security {
        const val REQUIRE_SCREEN_LOCK_FOR_PAYMENTS = true
        const val PAYMENT_PIN_REQUIRED = true
        const val MAX_PAYMENT_AMOUNT_WITHOUT_PIN = 1000 // In smallest currency unit
    }
    
    /**
     * Add-on configuration
     */
    object AddOns {
        /**
         * Core add-ons that should always be loaded
         */
        val CORE_ADDONS = listOf(
            "qr-gateway"
        )
        
        /**
         * Optional add-ons based on build flavor
         */
        val OPTIONAL_ADDONS = listOf(
            "multichain-bridge",
            "sentz-integration"
        )
        
        /**
         * Experimental add-ons (not loaded by default)
         */
        val EXPERIMENTAL_ADDONS = listOf(
            "mesh-network",
            "lightning-integration"
        )
    }
    
    /**
     * UI/UX customizations
     */
    object UI {
        const val SHOW_OSCASH_BRANDING = true
        const val SHOW_PAYMENT_TAB = true
        const val SHOW_QR_SCANNER_BUTTON = true
        
        // Color scheme
        const val PRIMARY_COLOR = 0xFF6B35FF // osCASH purple
        const val ACCENT_COLOR = 0xFF004E89  // Deep blue
    }
    
    /**
     * Debug and development settings
     */
    object Debug {
        const val ENABLE_ADDON_DEBUG_LOGS = true
        val ENABLE_PAYMENT_DEBUG_LOGS = BuildConfig.DEBUG
        val MOCK_PAYMENT_RESPONSES = BuildConfig.DEBUG
    }
    
    /**
     * Check if a feature is enabled
     */
    fun isFeatureEnabled(feature: String): Boolean {
        return when (feature) {
            "payments" -> Features.PAYMENTS_ENABLED
            "mobilecoin" -> Features.MOBILECOIN_ENABLED
            "qr_payments" -> Features.QR_PAYMENTS_ENABLED
            "addon_system" -> Features.ADDON_SYSTEM_ENABLED
            "multichain_bridge" -> Features.MULTICHAIN_BRIDGE_ENABLED
            "mesh_network" -> Features.MESH_NETWORK_ENABLED
            "sentz_integration" -> Features.SENTZ_INTEGRATION_ENABLED
            "signal_payments" -> Features.SIGNAL_PAYMENTS_ENABLED
            else -> false
        }
    }
    
    /**
     * Get build information string
     */
    fun getBuildInfo(): String {
        return "osCASH.me $VERSION (Molly $MOLLY_VERSION)"
    }
    
    /**
     * Get enabled add-ons based on current configuration
     */
    fun getEnabledAddOns(): List<String> {
        val enabled = mutableListOf<String>()
        
        // Always include core add-ons
        enabled.addAll(AddOns.CORE_ADDONS)
        
        // Add optional add-ons based on feature flags
        if (Features.MULTICHAIN_BRIDGE_ENABLED) {
            enabled.add("multichain-bridge")
        }
        if (Features.MESH_NETWORK_ENABLED) {
            enabled.add("mesh-network")
        }
        if (Features.SENTZ_INTEGRATION_ENABLED) {
            enabled.add("sentz-integration")
        }
        if (Features.SIGNAL_PAYMENTS_ENABLED) {
            enabled.add("signal-integration")
        }
        
        return enabled
    }
}