/*
 * Copyright 2025 osCASH.me Contributors
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package me.oscash.addon

import android.content.Context

/**
 * Base interface for all osCASH.me Add-Ons
 * 
 * Add-Ons are modular extensions that provide additional functionality
 * without modifying the core Molly codebase.
 */
interface AddOn {
    
    /**
     * Unique identifier for this add-on
     */
    val id: String
    
    /**
     * Human-readable name
     */
    val name: String
    
    /**
     * Version string (semver recommended)
     */
    val version: String
    
    /**
     * List of other add-on IDs that this add-on depends on
     */
    val dependencies: List<String>
    
    /**
     * Initialize the add-on
     * 
     * @param context Android application context
     * @return true if initialization was successful
     */
    suspend fun initialize(context: Context): Boolean
    
    /**
     * Shutdown the add-on and clean up resources
     */
    suspend fun shutdown()
    
    /**
     * Get the capabilities provided by this add-on
     */
    fun getCapabilities(): List<Capability>
    
    /**
     * Check if this add-on is currently healthy/responding
     */
    suspend fun healthCheck(): HealthStatus
    
    /**
     * Get configuration schema for this add-on
     */
    fun getConfigSchema(): ConfigSchema?
}

/**
 * Represents a capability provided by an add-on
 */
data class Capability(
    val type: CapabilityType,
    val name: String,
    val description: String,
    val version: String = "1.0.0"
)

/**
 * Types of capabilities that add-ons can provide
 */
enum class CapabilityType {
    PAYMENT_GATEWAY,
    TOKEN_BRIDGE,
    NETWORK_TRANSPORT,
    UI_COMPONENT,
    DATA_PROVIDER,
    SECURITY_SERVICE
}

/**
 * Health status of an add-on
 */
data class HealthStatus(
    val isHealthy: Boolean,
    val lastCheck: Long = System.currentTimeMillis(),
    val errorMessage: String? = null,
    val metrics: Map<String, Any> = emptyMap()
)

/**
 * Configuration schema for add-on settings
 */
data class ConfigSchema(
    val version: String,
    val fields: List<ConfigField>
)

/**
 * Configuration field definition
 */
data class ConfigField(
    val key: String,
    val type: ConfigFieldType,
    val required: Boolean,
    val defaultValue: Any? = null,
    val description: String? = null,
    val validation: ConfigValidation? = null
)

enum class ConfigFieldType {
    STRING,
    INTEGER,
    BOOLEAN,
    URL,
    EMAIL,
    PASSWORD
}

data class ConfigValidation(
    val pattern: String? = null,
    val minLength: Int? = null,
    val maxLength: Int? = null,
    val min: Number? = null,
    val max: Number? = null
)