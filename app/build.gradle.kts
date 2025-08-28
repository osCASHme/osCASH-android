plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "me.oscash.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "me.oscash.app"
        minSdk = 27  // Match Molly minSdk  
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0-alpha1-molly-7.53.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    lint {
        targetSdk = 35
    }
}

dependencies {
    // Molly Core Library Dependencies (not app module)
    implementation(project(":molly-core:libsignal-service"))
    implementation(project(":molly-core:core-ui"))
    implementation(project(":molly-core:core-util"))
    implementation(project(":molly-core:core-util-jvm"))
    implementation(project(":molly-core:contacts"))
    implementation(project(":molly-core:image-editor"))
    implementation(project(":molly-core:qr"))
    implementation(project(":molly-core:paging"))
    implementation(project(":molly-core:photoview"))
    implementation(project(":molly-core:sticky-header-grid"))
    implementation(project(":molly-core:video"))
    implementation(project(":molly-core:glide-config"))
    implementation(project(":molly-core:donations"))
    implementation(project(":molly-core:billing"))
    implementation(project(":molly-core:device-transfer"))
    implementation(project(":molly-core:libfakegms"))
    implementation(project(":molly-core:libnetcipher"))
    implementation(project(":molly-core:debuglogs-viewer"))
    implementation(project(":molly-core:wire-handler"))
    implementation(project(":molly-core:spinner"))
    
    // osCASH.me Core Extensions
    implementation(project(":oscash-core:payments-base"))
    implementation(project(":oscash-core:addon-manager"))
    implementation(project(":oscash-core:oscash-config"))
    
    // Basic Add-Ons
    implementation(project(":addons:qr-gateway"))
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}