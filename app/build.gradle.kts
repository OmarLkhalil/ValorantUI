plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)

    // Firebase Plugins
    alias(libs.plugins.googleServices)
    alias(libs.plugins.firebaseCrashlytics)
    alias(libs.plugins.firebaseAppdistribution)
}

android {
    namespace = "com.larryyu.valorantui"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.larryyu.valorantui"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    composeCompiler {
        reportsDestination = layout.buildDirectory.dir("compose_compiler")
        // stabilityConfigurationFile removed - file doesn't exist
    }
    buildTypes {
        release {
            isMinifyEnabled = false
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
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.browser)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Testing deps
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.koin.android)

    // Firebase
    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseCrashlyticsKtx)
    implementation(libs.firebaseAnalytics)

    implementation(project(":composeApp"))
}
