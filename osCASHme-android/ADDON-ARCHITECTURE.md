# 🔌 osCASH.me Add-On Architektur Design

> Modulares Plugin-System für erweiterte Zahlungsfunktionen

## 🎯 Vision
**Molly Core + MobileCoin Base + Modulare Add-Ons = osCASH.me**

Jedes Feature als austauschbares Modul → Einfache Updates vom Molly Upstream

## 🏗️ Architektur-Übersicht

```
osCASH.me
├── molly-core/              # Unveränderte Molly Basis
│   ├── app/                 # Basis Android App
│   ├── libsignal-service/   # Signal Protokoll
│   └── core-*               # Core Module
│
├── oscash-core/             # osCASH.me Kern-Erweiterungen
│   ├── payments-base/       # MobileCoin Reaktivierung
│   ├── addon-manager/       # Plugin-System Core
│   └── oscash-config/       # osCASH.me Konfiguration
│
└── addons/                  # Modulare Add-Ons
    ├── qr-gateway/          # QR-Code Payment Gateway
    ├── multichain-bridge/   # eUSD/USDT/USDC Support
    ├── mesh-network/        # Offline Mesh Payments
    ├── sentz-integration/   # SENTZ Wallet Bridge
    └── signal-integration/  # Signal Payments Bridge
```

## 🔧 Core-System Design

### 1. payments-base Module

**Zweck**: MobileCoin-Funktionalität reaktivieren
```kotlin
// oscash-core/payments-base/src/main/kotlin/
package me.oscash.payments.base

interface PaymentEngine {
    fun enablePayments(): Boolean
    fun getMobileCoinBalance(): Money.MobileCoin
    fun createTransaction(recipient: String, amount: Money.MobileCoin): Transaction
    fun getTransactionHistory(): List<Transaction>
}

class MobileCoinPaymentEngine : PaymentEngine {
    override fun enablePayments(): Boolean {
        // RemoteConfig Override: android.payments.kill = false
        SignalStore.payments().setPaymentsEnabled(true)
        return true
    }
}
```

### 2. addon-manager Module

**Zweck**: Plugin-Lifecycle Management
```kotlin
// oscash-core/addon-manager/src/main/kotlin/
package me.oscash.addon

interface AddOn {
    val id: String
    val version: String
    val dependencies: List<String>
    
    fun initialize(context: Context): Boolean
    fun shutdown()
    fun getCapabilities(): List<Capability>
}

class AddOnManager {
    private val loadedAddOns = mutableMapOf<String, AddOn>()
    
    fun loadAddOn(addOn: AddOn): Boolean {
        // Dependency-Check
        if (checkDependencies(addOn.dependencies)) {
            loadedAddOns[addOn.id] = addOn
            return addOn.initialize(context)
        }
        return false
    }
    
    fun unloadAddOn(id: String) {
        loadedAddOns[id]?.shutdown()
        loadedAddOns.remove(id)
    }
}
```

## 🎯 Add-On Implementierungen

### 1. QR-Gateway Add-On

**Zweck**: Universeller QR-Code Payment Gateway
```kotlin
// addons/qr-gateway/src/main/kotlin/
package me.oscash.addons.qr

class QRGatewayAddOn : AddOn {
    override val id = "qr-gateway"
    override val version = "1.0.0"
    override val dependencies = listOf("payments-base")
    
    fun generatePaymentQR(amount: Money, recipient: String): String {
        val paymentRequest = PaymentRequest(
            amount = amount,
            recipient = recipient,
            methods = listOf("mobilecoin", "signal", "sentz"),
            timestamp = System.currentTimeMillis()
        )
        return QRCodeGenerator.encode(paymentRequest.toJson())
    }
    
    fun parsePaymentQR(qrCode: String): PaymentRequest {
        val json = QRCodeParser.decode(qrCode)
        return PaymentRequest.fromJson(json)
    }
}

data class PaymentRequest(
    val amount: Money,
    val recipient: String,
    val methods: List<String>,
    val timestamp: Long,
    val memo: String? = null
)
```

### 2. MultiChain-Bridge Add-On

**Zweck**: eUSD/USDT/USDC Cross-Chain Support
```kotlin
// addons/multichain-bridge/src/main/kotlin/
package me.oscash.addons.multichain

class MultiChainBridgeAddOn : AddOn {
    override val id = "multichain-bridge"
    override val version = "1.0.0"
    override val dependencies = listOf("payments-base", "qr-gateway")
    
    fun getSupportedTokens(): List<Token> {
        return listOf(
            Token("eUSD", "MobileCoin", 6),
            Token("USDT", "Ethereum", 6),
            Token("USDC", "Ethereum", 6),
            Token("MOB", "MobileCoin", 12)
        )
    }
    
    suspend fun swapTokens(
        from: Token, 
        to: Token, 
        amount: BigDecimal,
        slippage: Double = 0.01
    ): SwapResult {
        return when {
            from.chain == "MobileCoin" && to.chain == "Ethereum" -> 
                bridgeToEthereum(from, to, amount)
            from.chain == "Ethereum" && to.chain == "MobileCoin" -> 
                bridgeToMobileCoin(from, to, amount)
            else -> SwapResult.Unsupported
        }
    }
}
```

### 3. SENTZ-Integration Add-On

**Zweck**: SENTZ Wallet API Bridge
```kotlin
// addons/sentz-integration/src/main/kotlin/
package me.oscash.addons.sentz

class SentzIntegrationAddOn : AddOn {
    override val id = "sentz-integration"
    override val version = "1.0.0"
    override val dependencies = listOf("multichain-bridge")
    
    private val sentzApi = SentzApiClient()
    
    suspend fun sendToSentz(
        recipient: String,
        amount: Money,
        currency: String = "eUSD"
    ): SendResult {
        val sentzPayload = SentzPaymentPayload(
            recipient = recipient,
            amount = amount.serialize(),
            currency = currency,
            source = "oscash-me"
        )
        return sentzApi.sendPayment(sentzPayload)
    }
    
    suspend fun receiveFromSentz(): List<SentzTransaction> {
        return sentzApi.getIncomingTransactions()
    }
}
```

### 4. Mesh-Network Add-On

**Zweck**: Offline Mesh-Network Payments
```kotlin
// addons/mesh-network/src/main/kotlin/
package me.oscash.addons.mesh

class MeshNetworkAddOn : AddOn {
    override val id = "mesh-network"
    override val version = "1.0.0"
    override val dependencies = listOf("payments-base")
    
    fun broadcastTransaction(tx: OfflineTransaction) {
        meshBroadcaster.broadcast(tx)
    }
    
    fun syncWithNetwork(): Int {
        val pendingTx = meshReceiver.getPendingTransactions()
        var synced = 0
        
        pendingTx.forEach { tx ->
            if (validateTransaction(tx)) {
                processOfflineTransaction(tx)
                synced++
            }
        }
        return synced
    }
}

data class OfflineTransaction(
    val id: String,
    val from: String,
    val to: String,
    val amount: Money,
    val timestamp: Long,
    val signature: String,
    val hops: List<String> = emptyList()
)
```

## 🔗 Integration Patterns

### 1. Gradle Build Integration

```kotlin
// settings.gradle.kts
include(":molly-core:app")
include(":molly-core:libsignal-service")

include(":oscash-core:payments-base")
include(":oscash-core:addon-manager")
include(":oscash-core:oscash-config")

include(":addons:qr-gateway")
include(":addons:multichain-bridge") 
include(":addons:mesh-network")
include(":addons:sentz-integration")
include(":addons:signal-integration")

// Conditional includes based on build flavor
if (project.hasProperty("enableMeshNetwork")) {
    include(":addons:mesh-network")
}
```

### 2. Dependency Injection

```kotlin
// oscash-core/addon-manager/src/main/kotlin/
@Module
class AddOnModule {
    
    @Provides
    @Singleton
    fun provideAddOnManager(): AddOnManager = AddOnManager()
    
    @Provides
    @ElementsIntoSet
    fun provideQRGateway(): AddOn = QRGatewayAddOn()
    
    @Provides
    @ElementsIntoSet  
    fun provideMultiChainBridge(): AddOn = MultiChainBridgeAddOn()
}
```

### 3. UI-Integration Hooks

```kotlin
// molly-core/app/src/main/kotlin/ (erweitert)
class PaymentsActivity : AppCompatActivity() {
    
    @Inject lateinit var addOnManager: AddOnManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Dynamische UI basierend auf geladenen Add-Ons
        val qrGateway = addOnManager.getAddOn<QRGatewayAddOn>("qr-gateway")
        if (qrGateway != null) {
            showQRPaymentOption()
        }
        
        val multiChain = addOnManager.getAddOn<MultiChainBridgeAddOn>("multichain-bridge")
        if (multiChain != null) {
            showTokenSwapOptions(multiChain.getSupportedTokens())
        }
    }
}
```

## 🚀 Build-Varianten & Flavors

### Build-Konfiguration
```kotlin
// app/build.gradle.kts
android {
    productFlavors {
        create("oscashBasic") {
            dimension = "features"
            // Nur Core + QR-Gateway
            buildConfigField("String[]", "ENABLED_ADDONS", 
                "{\"qr-gateway\"}")
        }
        
        create("oscashFull") {
            dimension = "features"  
            // Alle Add-Ons aktiviert
            buildConfigField("String[]", "ENABLED_ADDONS", 
                "{\"qr-gateway\", \"multichain-bridge\", \"mesh-network\", \"sentz-integration\"}")
        }
        
        create("oscashMesh") {
            dimension = "features"
            // Spezielle Mesh-Network Variante
            buildConfigField("String[]", "ENABLED_ADDONS", 
                "{\"qr-gateway\", \"mesh-network\"}")
        }
    }
}
```

## 🔧 Update-Strategie

### Molly Upstream-Merge
```bash
#!/bin/bash
# update-from-molly.sh

echo "🔄 Updating from Molly upstream..."

# 1. Molly Core updaten (unverändert)
git subtree pull --prefix=molly-core molly main --squash

# 2. osCASH.me spezifische Änderungen beibehalten
git checkout HEAD -- oscash-core/
git checkout HEAD -- addons/

# 3. Integration-Tests
./gradlew testOscashBasicDebug
./gradlew testOscashFullDebug

echo "✅ Upstream merge completed"
```

### Add-On Updates
```kotlin
// Individuelle Add-On Updates ohne Core-Eingriff
class AddOnUpdater {
    suspend fun updateAddOn(id: String, version: String): Boolean {
        val currentAddOn = addOnManager.getAddOn(id)
        currentAddOn?.shutdown()
        
        val newAddOn = downloadAddOn(id, version)
        return addOnManager.loadAddOn(newAddOn)
    }
}
```

## 📊 Monitoring & Analytics

### Add-On Performance Tracking
```kotlin
class AddOnMetrics {
    fun trackAddOnUsage(addOnId: String, action: String) {
        Analytics.track("addon_usage") {
            put("addon_id", addOnId)
            put("action", action)
            put("timestamp", System.currentTimeMillis())
        }
    }
    
    fun getAddOnHealth(): Map<String, AddOnHealthStatus> {
        return addOnManager.getLoadedAddOns().mapValues { (_, addOn) ->
            AddOnHealthStatus(
                isResponding = ping(addOn),
                memoryUsage = getMemoryUsage(addOn),
                errorCount = getErrorCount(addOn)
            )
        }
    }
}
```

## 🎯 Deployment-Strategie

### 1. F-Droid Kompatibilität
- Alle Add-Ons als FOSS
- Keine proprietären Dependencies
- Reproducible Builds

### 2. Schrittweise Rollout
- **v1.0**: Basic (Core + QR-Gateway)
- **v1.1**: + MultiChain-Bridge  
- **v1.2**: + SENTZ-Integration
- **v1.3**: + Mesh-Network
- **v2.0**: + Signal-Integration

### 3. Community Add-Ons
```kotlin
// Externes Add-On Beispiel
class CommunityLightningAddOn : AddOn {
    override val id = "community-lightning"
    override val version = "0.1.0"
    override val dependencies = listOf("qr-gateway")
    
    // Lightning Network Integration
    // Community-entwickelt, separat maintained
}
```

---

## ✅ Kernvorteile dieser Architektur

1. **🔄 Molly-kompatibel**: Core bleibt unverändert → einfache Updates
2. **🧩 Modular**: Jedes Feature als austauschbares Modul  
3. **🚀 Skalierbar**: Neue Add-Ons ohne Core-Änderungen
4. **🔒 Sicher**: Isolierte Add-Ons, controlled Dependencies
5. **👥 Community-ready**: Externe Add-On Entwicklung möglich

**Status**: ✅ **Architektur Design komplett!**  
**Nächster Schritt**: Projektstruktur implementieren

*Namasté* 🙏