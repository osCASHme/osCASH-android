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

// Molly Core Integration (via symlink)
include(":molly-core:app")
include(":molly-core:libsignal-service")
include(":molly-core:core-ui")
include(":molly-core:core-util")
include(":molly-core:core-util-jvm")
include(":molly-core:contacts")
include(":molly-core:image-editor")
include(":molly-core:qr")
include(":molly-core:paging")
include(":molly-core:photoview")
include(":molly-core:sticky-header-grid")
include(":molly-core:video")
include(":molly-core:glide-config")
include(":molly-core:donations")
include(":molly-core:billing")
include(":molly-core:device-transfer")
include(":molly-core:libfakegms")
include(":molly-core:libnetcipher")
include(":molly-core:lintchecks")
include(":molly-core:debuglogs-viewer")
include(":molly-core:wire-handler")
include(":molly-core:spinner")

// osCASH.me Core Extensions
include(":oscash-core:payments-base")
include(":oscash-core:addon-manager") 
include(":oscash-core:oscash-config")

// Add-Ons
include(":addons:qr-gateway")

// Main osCASH.me App (replaces molly-core:app as final build target)
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