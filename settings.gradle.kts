pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        // MobileCoin dependencies
        maven { url = uri("https://s3-us-west-1.amazonaws.com/mobilecoin.chain") }
    }
}

rootProject.name = "osCASH.me"

// osCASH.me Core (simplified build for now)
include(":oscash-core:payments-base")
include(":oscash-core:addon-manager") 
include(":oscash-core:oscash-config")

// Add-Ons
include(":addons:qr-gateway")

// Main App
include(":app")

if (gradle.startParameter.projectProperties.containsKey("enableMultiChain") || gradle.startParameter.projectProperties.containsKey("enableAll")) {
    include(":addons:multichain-bridge")
}

if (gradle.startParameter.projectProperties.containsKey("enableMesh") || gradle.startParameter.projectProperties.containsKey("enableAll")) {
    include(":addons:mesh-network")
}

if (gradle.startParameter.projectProperties.containsKey("enableSentz") || gradle.startParameter.projectProperties.containsKey("enableAll")) {
    include(":addons:sentz-integration")
}

if (gradle.startParameter.projectProperties.containsKey("enableSignal") || gradle.startParameter.projectProperties.containsKey("enableAll")) {
    include(":addons:signal-integration")
}