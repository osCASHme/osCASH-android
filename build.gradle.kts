// Top-level build file for osCASH.me
plugins {
    id("com.android.application") version "8.7.2" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.parcelize") version "2.1.0" apply false
}

allprojects {
    ext["osCashVersion"] = "1.0.0-alpha"
    ext["mollyVersion"] = "7.53.4"
    
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

subprojects {
    // Gemeinsame Konfiguration für alle Module
    
    afterEvaluate {
        if (plugins.hasPlugin("com.android.library")) {
            configure<com.android.build.gradle.LibraryExtension> {
                compileSdk = 35
                
                defaultConfig {
                    minSdk = 26
                    targetSdk = 35
                    
                    consumerProguardFiles("consumer-rules.pro")
                }
                
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }
        }
    }
}