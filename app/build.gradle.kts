plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)

    // Firebase Plugins - للـ CI/CD
    alias(libs.plugins.googleServices)
    // alias(libs.plugins.firebaseCrashlytics)  // Optional - enable when needed
    alias(libs.plugins.firebaseAppdistribution)
    // alias(libs.plugins.firebasePerf)  // Optional - enable when needed
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
    implementation(libs.androidx.compose.material)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.browser)
    implementation(libs.androidx.palette.ktx)


    // Navigation Compose
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // testing deps
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)

    //Koin
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.koin.android)

    // Firebase - للـ CI/CD و App Distribution
    // BOM سيتم تفعيله بعد setup Firebase بشكل كامل
    // implementation(platform(libs.firebaseBom))
    // implementation(libs.firebaseAnalytics)
    // implementation(libs.firebaseCrashlyticsKtx)
    // implementation(libs.firebasePerfKtx)

    implementation(project(":composeApp"))

}