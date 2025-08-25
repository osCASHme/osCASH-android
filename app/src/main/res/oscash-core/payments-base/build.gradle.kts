plugins { id("com.android.library"); id("org.jetbrains.kotlin.android") }
android { namespace = "me.oscash.payments"; compileSdk = 34
defaultConfig { minSdk = 26 }; lint { targetSdk = 34 }
compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
kotlinOptions { jvmTarget = "17" } }
