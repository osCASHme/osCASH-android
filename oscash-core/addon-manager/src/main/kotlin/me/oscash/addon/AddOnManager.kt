/*
 * Copyright 2025 osCASH.me Contributors
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package me.oscash.addon

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.signal.core.util.logging.Log

/**
 * Central manager for all osCASH.me Add-Ons
 * 
 * Handles loading, dependency resolution, lifecycle management,
 * and health monitoring of add-ons.
 */
class AddOnManager(private val context: Context) {
    
    companion object {
        private val TAG = Log.tag(AddOnManager::class.java)
    }
    
    private val _loadedAddOns = mutableMapOf<String, AddOn>()
    private val _addOnStates = MutableStateFlow<Map<String, AddOnState>>(emptyMap())
    
    /**
     * Currently loaded add-ons
     */
    val loadedAddOns: Map<String, AddOn> get() = _loadedAddOns.toMap()
    
    /**
     * Flow of add-on states
     */
    val addOnStates: Flow<Map<String, AddOnState>> = _addOnStates.asStateFlow()
    
    /**
     * Load an add-on
     * 
     * @param addOn The add-on to load
     * @return true if loading was successful
     */
    suspend fun loadAddOn(addOn: AddOn): Boolean {
        Log.i(TAG, "Loading add-on: ${addOn.id}")
        
        return try {
            // Check if already loaded
            if (_loadedAddOns.containsKey(addOn.id)) {
                Log.w(TAG, "Add-on ${addOn.id} is already loaded")
                return true
            }
            
            // Check dependencies
            if (!checkDependencies(addOn.dependencies)) {
                Log.e(TAG, "Dependencies not met for add-on: ${addOn.id}")
                updateAddOnState(addOn.id, AddOnState.DEPENDENCY_ERROR)
                return false
            }
            
            // Initialize the add-on
            updateAddOnState(addOn.id, AddOnState.INITIALIZING)
            
            val success = addOn.initialize(context)
            if (success) {
                _loadedAddOns[addOn.id] = addOn
                updateAddOnState(addOn.id, AddOnState.ACTIVE)
                Log.i(TAG, "Successfully loaded add-on: ${addOn.id}")
                true
            } else {
                updateAddOnState(addOn.id, AddOnState.INITIALIZATION_FAILED)
                Log.e(TAG, "Failed to initialize add-on: ${addOn.id}")
                false
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error loading add-on: ${addOn.id}", e)
            updateAddOnState(addOn.id, AddOnState.ERROR)
            false
        }
    }
    
    /**
     * Unload an add-on
     * 
     * @param id The ID of the add-on to unload
     */
    suspend fun unloadAddOn(id: String) {
        Log.i(TAG, "Unloading add-on: $id")
        
        _loadedAddOns[id]?.let { addOn ->
            try {
                updateAddOnState(id, AddOnState.SHUTTING_DOWN)
                addOn.shutdown()
                _loadedAddOns.remove(id)
                updateAddOnState(id, AddOnState.UNLOADED)
                Log.i(TAG, "Successfully unloaded add-on: $id")
            } catch (e: Exception) {
                Log.e(TAG, "Error unloading add-on: $id", e)
                updateAddOnState(id, AddOnState.ERROR)
            }
        }
    }
    
    /**
     * Get a specific add-on by ID
     */
    inline fun <reified T : AddOn> getAddOn(id: String): T? {
        return _loadedAddOns[id] as? T
    }
    
    /**
     * Get all add-ons that provide a specific capability
     */
    fun getAddOnsWithCapability(capabilityType: CapabilityType): List<AddOn> {
        return _loadedAddOns.values.filter { addOn ->
            addOn.getCapabilities().any { it.type == capabilityType }
        }
    }
    
    /**
     * Perform health check on all loaded add-ons
     */
    suspend fun performHealthCheck(): Map<String, HealthStatus> {
        Log.d(TAG, "Performing health check on ${_loadedAddOns.size} add-ons")
        
        return _loadedAddOns.mapValues { (id, addOn) ->
            try {
                addOn.healthCheck()
            } catch (e: Exception) {
                Log.w(TAG, "Health check failed for add-on: $id", e)
                HealthStatus(
                    isHealthy = false,
                    errorMessage = e.message
                )
            }
        }
    }
    
    /**
     * Check if all dependencies are available
     */
    private fun checkDependencies(dependencies: List<String>): Boolean {
        return dependencies.all { depId ->
            val isLoaded = _loadedAddOns.containsKey(depId)
            if (!isLoaded) {
                Log.w(TAG, "Missing dependency: $depId")
            }
            isLoaded
        }
    }
    
    /**
     * Update the state of an add-on
     */
    private fun updateAddOnState(id: String, state: AddOnState) {
        val currentStates = _addOnStates.value.toMutableMap()
        currentStates[id] = state
        _addOnStates.value = currentStates
    }
    
    /**
     * Get statistics about loaded add-ons
     */
    fun getStatistics(): AddOnManagerStats {
        val states = _addOnStates.value
        return AddOnManagerStats(
            totalAddOns = states.size,
            activeAddOns = states.values.count { it == AddOnState.ACTIVE },
            errorAddOns = states.values.count { it == AddOnState.ERROR },
            capabilities = _loadedAddOns.values.flatMap { it.getCapabilities() }.groupingBy { it.type }.eachCount()
        )
    }
}

/**
 * Possible states of an add-on
 */
enum class AddOnState {
    UNLOADED,
    INITIALIZING,
    ACTIVE,
    ERROR,
    DEPENDENCY_ERROR,
    INITIALIZATION_FAILED,
    SHUTTING_DOWN
}

/**
 * Statistics about the add-on manager
 */
data class AddOnManagerStats(
    val totalAddOns: Int,
    val activeAddOns: Int,
    val errorAddOns: Int,
    val capabilities: Map<CapabilityType, Int>
)