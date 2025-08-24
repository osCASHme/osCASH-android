plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "me.oscash.config"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        targetSdk = 34

        buildConfigField("String", "OSCASH_VERSION", "\"${project.ext["osCashVersion"]}\"")
        buildConfigField("String", "MOLLY_VERSION", "\"${project.ext["mollyVersion"]}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    
    buildFeatures {
        buildConfig = true
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Core Dependencies
    implementation(project(":molly-core:core-util"))
    implementation(project(":oscash-core:addon-manager"))
    
    // Android
    implementation(libs.androidx.core.ktx)
    
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}