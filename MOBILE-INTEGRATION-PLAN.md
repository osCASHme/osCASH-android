# osCASH.me Mobile Integration Plan

**Phase 2A: APP ↔ GATE Integration**

## 🎯 Implementation Strategy

### 1. Payment Settings UI (First Priority)

**Location**: After Privacy settings in `AppSettingsFragment.kt:358`

```kotlin
// Add after Privacy settings (line 358)
item {
  Rows.TextRow(
    text = stringResource(R.string.preferences__payments),
    icon = painterResource(R.drawable.symbol_payment_24),
    onClick = {
      callbacks.navigate(R.id.action_appSettingsFragment_to_paymentSettingsFragment)
    },
    enabled = isRegisteredAndUpToDate
  )
}
```

**Required Components:**
1. **PaymentSettingsFragment.kt** - Main settings screen
2. **PaymentSettingsViewModel.kt** - Data management  
3. **String resources** - UI text
4. **Navigation entry** - Fragment routing
5. **Payment icon** - UI symbol

### 2. GATE API Client Architecture

```kotlin
// New package: org.thoughtcrime.securesms.oscash.gate

interface GateApiClient {
    suspend fun testConnection(): GateConnectionResult
    suspend fun createInvoice(request: CreateInvoiceRequest): Invoice
    suspend fun getInvoiceStatus(invoiceId: String): InvoiceStatus
    suspend fun getTransactionHistory(): List<Transaction>
}

data class GateServerConfig(
    val serverUrl: String,
    val apiKey: String,
    val serverName: String = "Custom Server"
)

// Predefined servers
object GateServers {
    val DEFAULT = GateServerConfig(
        serverUrl = "https://gate.osCASH.me",
        apiKey = "", // User needs to generate
        serverName = "osCASH.me Official"
    )
}
```

### 3. Payment Settings Storage

```kotlin
// Extend existing SignalStore with GateValues
class GateValues internal constructor(store: KeyValueStore) : SignalStoreValues(store) {
    
    companion object {
        private const val GATE_ENABLED = "gate_enabled"
        private const val GATE_SERVER_URL = "gate_server_url" 
        private const val GATE_API_KEY = "gate_api_key"
        private const val GATE_SERVER_NAME = "gate_server_name"
    }
    
    fun isGateEnabled(): Boolean = getBoolean(GATE_ENABLED, false)
    
    fun getGateServerConfig(): GateServerConfig? {
        val url = getString(GATE_SERVER_URL, null) ?: return null
        val apiKey = getString(GATE_API_KEY, null) ?: return null
        val name = getString(GATE_SERVER_NAME, "Custom Server")
        
        return GateServerConfig(url, apiKey, name)
    }
    
    fun setGateServerConfig(config: GateServerConfig) {
        store.beginWrite()
            .putBoolean(GATE_ENABLED, true)
            .putString(GATE_SERVER_URL, config.serverUrl)
            .putString(GATE_API_KEY, config.apiKey) 
            .putString(GATE_SERVER_NAME, config.serverName)
            .commit()
    }
}
```

## 📱 UI Mockup: Payment Settings

### Settings Screen Structure
```
┌─────────────────────────────────────┐
│ ← Payments                          │
├─────────────────────────────────────┤
│                                     │
│ Payment Gateway                     │
│ ┌─────────────────────────────────┐ │
│ │ ○ osCASH.me Official            │ │
│ │   gate.osCASH.me                │ │  
│ │                                 │ │
│ │ ○ Custom Server                 │ │
│ │   company.example.com:23000     │ │
│ │                                 │ │
│ │ ○ Self-hosted                   │ │
│ │   192.168.1.100:23000          │ │
│ └─────────────────────────────────┘ │
│                                     │
│ API Configuration                   │
│ ┌─────────────────────────────────┐ │
│ │ Server URL                      │ │
│ │ [https://gate.example.com    ] │ │
│ │                                 │ │
│ │ API Key                         │ │
│ │ [********************************] │ │
│ │                                 │ │
│ │ [Test Connection]               │ │
│ └─────────────────────────────────┘ │
│                                     │
│ [Transaction History]               │
│ [Generate QR Code]                  │
│                                     │
└─────────────────────────────────────┘
```

## 🔗 Chat Integration

### Payment Button in Compose Menu

**Location**: Attachment menu in chat compose

```kotlin
// Add to ComposeText attachment options
if (SignalStore.gateValues().isGateEnabled()) {
    AttachmentOption(
        icon = R.drawable.symbol_payment_24,
        text = R.string.compose_payment,
        onClick = { showPaymentDialog() }
    )
}
```

### QR Payment Dialog
```
┌─────────────────────────────────────┐
│ Send Payment                        │
├─────────────────────────────────────┤
│                                     │
│ Amount: [0.001      ] BTC ▼         │
│         [$45.67     ] USD           │
│                                     │
│ Note (optional):                    │
│ [Coffee payment            ]        │
│                                     │
│ ┌─────────────────────────────────┐ │
│ │                                 │ │
│ │        QR CODE HERE             │ │
│ │                                 │ │
│ └─────────────────────────────────┘ │
│                                     │
│ [Copy Payment Link] [Share QR]      │
│                                     │
│ [Cancel]           [Send Payment]   │
│                                     │
└─────────────────────────────────────┘
```

## 🛠️ Implementation Steps

### Step 1: String Resources (1 hour)
```xml
<!-- Add to strings.xml -->
<string name="preferences__payments">Payments</string>
<string name="payment_settings__title">Payment Settings</string>
<string name="payment_gateway__title">Payment Gateway</string>
<string name="payment_gateway__official">osCASH.me Official</string>
<string name="payment_gateway__custom">Custom Server</string>
<string name="payment_gateway__self_hosted">Self-hosted</string>
<string name="payment_api_key__title">API Key</string>
<string name="payment_server_url__title">Server URL</string>
<string name="payment_test_connection">Test Connection</string>
<string name="payment_transaction_history">Transaction History</string>
<string name="compose_payment">Payment</string>
```

### Step 2: Payment Settings Fragment (3 hours)
- Create PaymentSettingsFragment.kt
- Implement server selection UI
- Add API key management
- Connection testing functionality

### Step 3: GATE API Client (4 hours) 
- HTTP client with Retrofit/OkHttp
- Authentication handling
- Error management
- Response models

### Step 4: Settings Integration (2 hours)
- Add navigation entry
- Integrate with AppSettingsFragment
- Create payment icon asset

### Step 5: Chat Payment Button (2 hours)
- Add to compose attachment menu
- Payment dialog implementation
- QR code generation

### Step 6: Transaction History (3 hours)
- History screen layout
- API integration
- Local caching

## 🎯 Success Criteria

### Phase 2A Complete When:
- [ ] Payment Settings screen accessible from main settings
- [ ] Users can configure custom GATE server
- [ ] API connection test works
- [ ] Payment button appears in chat compose
- [ ] QR payment generation works
- [ ] Transaction history displays

## ⚡ Quick Start (First 2 hours)

1. **Add string resources** (30 min)
2. **Create payment icon** (15 min) 
3. **Add settings menu entry** (15 min)
4. **Test navigation** (15 min)
5. **Create basic PaymentSettingsFragment** (45 min)

**Ready to implement! Swiss Clockwork precision starts now!** 🇨🇭⚡

---

*"Privacy First. Swiss Clockwork Reliability."* 🔒⏰