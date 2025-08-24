**[🇩🇪 Deutsche Version](../../README.md)** | 🇬🇧 English Version

# osCASH.me Android - Privacy Messenger with eUSD & MOB Wallet

The official Android client for osCASH.me - a privacy-focused messenger with integrated eUSD and MOB cryptocurrency wallet on the MobileCoin blockchain.

## 🔒 Privacy First

**Why osCASH.me?**
- **End-to-End Encryption** for all messages and calls
- **No metadata storage** on our servers
- **Integrated Crypto Wallet** for secure, private payments
- **Open Source** and community-audited
- **Decentralized** - your data belongs to you

## 💰 Integrated Wallet Features

### MobileCoin Integration
- **eUSD Stablecoin** - stable currency for daily use
- **MOB Token** - native MobileCoin for privacy-optimized transactions  
- **Secure Storage** - keys stay on your device
- **Fast Transactions** - sub-second confirmation
- **Privacy-by-Design** - no blockchain surveillance possible

### Payment Functions
- **In-Chat Payments** - send money directly in conversations
- **QR Code Scanner** - easy address input
- **Transaction History** - encrypted payment history
- **Multi-Currency** - eUSD and MOB in one app

## 🏗️ Technical Foundation

### Molly Core Integration
Based on proven **Molly** privacy messenger technology:
- Signal Protocol for encryption
- Tor integration for anonymity
- Metadata protection
- Secure data storage

### MobileCoin Blockchain
- **Privacy-optimized blockchain** - CryptoNote technology
- **Fast Finality** - no long waiting times
- **Environmentally friendly** - Proof-of-Stake consensus
- **Regulatory compliant** - eUSD is a regulated stablecoin

## 📱 Installation

### From Releases
```bash
# Download latest APK
wget https://github.com/osCASHme/android/releases/latest/osCASH.me.apk

# Installation
adb install osCASH.me.apk
```

### From Source Code
```bash
# Clone repository
git clone https://github.com/osCASHme/android.git
cd android

# Install dependencies
./gradlew build

# Debug build
./gradlew assembleDebug
```

## 🛡️ Security Features

- **Signal Protocol** - proven end-to-end encryption
- **Perfect Forward Secrecy** - old messages stay secure
- **Sealed Sender** - no metadata leaks
- **Screen Lock** - biometric app protection
- **Auto-Delete Messages** - messages disappear automatically
- **Tor Integration** - complete anonymity optional

## 🤝 Development & Contributing

### Prerequisites
```bash
# Android SDK
- Android API Level 24+ (Android 7.0+)
- NDK for native crypto libraries
- Java 11+

# MobileCoin SDK
- Rust 1.70+
- Protocol Buffers
- SGX SDK (for enclave features)
```

### Development Setup
```bash
git clone https://github.com/osCASHme/android.git
cd android

# Initialize submodules
git submodule update --init --recursive

# Build
./gradlew assembleDebug
```

### Code Structure
```
android/
├── molly-core/           # Molly Privacy Messenger Base
├── oscash-core/          # osCASH.me specific features
│   ├── payments-base/    # MobileCoin Wallet Integration
│   ├── oscash-config/    # App Configuration
│   └── addon-manager/    # Modular Extensions
├── addons/               # Optional Features
│   ├── mesh-network/     # P2P Mesh Networking
│   ├── qr-gateway/       # QR Code Integration
│   └── multichain-bridge/# Multi-Blockchain Support
└── build.gradle.kts      # Main Build Configuration
```

## 🔗 Links

- **Website**: [osCASH.me](https://osCASH.me)
- **recode.at DAO**: [github.com/recodeat](https://github.com/recodeat)
- **MobileCoin**: [mobilecoin.com](https://mobilecoin.com)
- **Molly Messenger**: [molly.im](https://molly.im)

## 📄 License

MIT License - see [LICENSE](../../LICENSE) for details.

**Dependencies:**
- Molly Core: AGPLv3
- MobileCoin SDK: Apache 2.0
- Signal Libraries: GPLv3

---

*Connected to be FREE* 🔒💰