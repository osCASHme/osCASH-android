#!/bin/bash

# osCASH.me Release Keystore Generation Script
# Creates a secure release keystore for signing APKs

echo "🔐 Generating osCASH.me Release Keystore..."

# Create keystore with secure parameters
keytool -genkey -v \
  -keystore osCASH-release.keystore \
  -alias oscash-key \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -storepass "osCASH2025Release!" \
  -keypass "osCASH2025Release!" \
  -dname "CN=osCASH.me, OU=osCASH Development, O=osCASH.me, L=Blockchain, S=Crypto, C=WW"

echo "✅ Keystore generated successfully!"
echo "📍 Location: $(pwd)/osCASH-release.keystore"
echo "🔑 Alias: oscash-key"
echo "🛡️ Password: osCASH2025Release!"