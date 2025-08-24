pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "osCASH.me"

// Molly Core (unverändert)
include(":molly-core:app")
include(":molly-core:libsignal-service")
include(":molly-core:core-util")
include(":molly-core:core-util-jvm")
include(":molly-core:core-ui")
include(":molly-core:qr")
include(":molly-core:billing")
include(":molly-core:contacts")
include(":molly-core:donations")
include(":molly-core:device-transfer")
include(":molly-core:debuglogs-viewer")
include(":molly-core:glide-config")
include(":molly-core:image-editor")
include(":molly-core:libfakegms")
include(":molly-core:libnetcipher")
include(":molly-core:lintchecks")
include(":molly-core:paging")
include(":molly-core:photoview")
include(":molly-core:spinner")
include(":molly-core:sticky-header-grid")
include(":molly-core:video")
include(":molly-core:wire-handler")

// osCASH.me Core
include(":oscash-core:payments-base")
include(":oscash-core:addon-manager")
include(":oscash-core:oscash-config")

// Add-Ons (conditional based on build properties)
include(":addons:qr-gateway")

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