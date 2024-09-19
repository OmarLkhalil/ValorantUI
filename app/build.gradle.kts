plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.larryyu.valorantui"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.larryyu.valorantui"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    kapt {
        correctErrorTypes = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.0-alpha10")
    implementation("androidx.compose.material:material:1.2.1")
    implementation(platform(libs.androidx.compose.bom.alpha))
    implementation("androidx.browser:browser:1.8.0")
    implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")
    implementation(libs.androidx.palette.ktx)
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("io.coil-kt:coil-compose:2.5.0")

    implementation("com.google.accompanist:accompanist-pager:0.23.0")

    implementation ("com.github.skydoves:cloudy:0.1.2")


    // Navigation Compose
    implementation(libs.androidx.navigation.compose)
    // ConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.compose.material:material-icons-extended-android:1.6.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // moshi converter
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    // coroutine adapter
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation ("com.github.Kaaveh:sdp-compose:1.1.0")
    // MOSHI KOTLIN
    implementation("com.squareup.moshi:moshi-kotlin:1.12.0")

    val lottieVersion = "6.0.0"
    implementation ("com.airbnb.android:lottie-compose:$lottieVersion")


    // room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

}