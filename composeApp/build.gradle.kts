import org.gradle.kotlin.dsl.implementation
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinX.serialization.plugin)
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
}

kotlin {
    // Android target (generate androidMain)
    androidTarget {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
    }

//    composeCompiler {
//        reportsDestination = layout.buildDirectory.dir("compose_compiler")
//        // stabilityConfigurationFile =
//        //     rootProject.layout.projectDirectory.file("stability_config.conf")
//    }

    // Desktop JVM target
    jvm("desktop")

    // iOS targets
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true

            // Link SQLite for iOS
            linkerOpts.add("-lsqlite3")
        }
    }

    sourceSets {
        // commonMain: only multiplatform-safe dependencies
        commonMain.dependencies {
            // JetBrains Compose MPP primitives (runtime/ui/foundation)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)

            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(compose.material3)

            // KotlinX & Coroutines / Serialization
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)

            // Ktor (core multiplatform)
            implementation(libs.ktor.core)
            implementation(libs.ktor.contentNegotiation)
            implementation(libs.ktor.logging)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Koin core (multiplatform)
            api(libs.koin.core)
            implementation(libs.koin.compose)

            // Napier, SQLDelight runtime (multiplatform)
            api(libs.napier)
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)

            // Navigation Compose Multiplatform
            implementation(libs.navigation.compose.multiplatform)

            // Other multiplatform utilities
            implementation(libs.multiplatform.settings)

            //Coil
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)

            //datastore
            implementation(libs.androidx.datastore.core.okio)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.kotlinx.coroutines.core.v190)


        }

        // Android-only dependencies (place all Android-specific compose / koin android / android-driver here)
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.lifecycle.runtime.ktx)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.datastore.preferences)

            // Android-specific Ktor engine and OkHttp (Android engine)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.android)

            // Android-only Compose material / icons / navigation (androidx)
            implementation(libs.androidx.compose.material) // androidx-material alias
            implementation(libs.androidx.compose.material.icons.extended) // icons
            implementation(libs.androidx.compose.foundation.layout) // foundation layout
            implementation(libs.androidx.navigation.compose) // navigation-compose

            // Koin Android specifics
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
//                implementation(libs.koin.androidx.viewmodel)
            implementation(libs.coil.network.ktor3)
            implementation(libs.coil.network.okhttp)
            // SQLDelight Android driver
            implementation(libs.sqldelight.android.driver)

            // Coil (Android)
            implementation(libs.coil.compose)
            implementation(libs.lottie.compose)
            implementation(libs.accompanist.pager)
            implementation(libs.sdp.compose)
        }

        // Android Testing dependencies
        androidInstrumentedTest.dependencies {
            implementation(libs.androidx.test.ext.junit)
            implementation(libs.androidx.test.runner)
            implementation(libs.androidx.test.rules)
            implementation(libs.androidx.espresso.core)
            implementation(libs.androidx.compose.ui.test.junit4)
            implementation(libs.androidx.compose.ui.test.manifest)
        }

        // iOS-only dependencies
        iosMain.dependencies {
            implementation(libs.ktor.darwin)
            // use native driver for iOS
            implementation(libs.sqldelight.native.driver)

            // Firebase Crashlytics for iOS (via CocoaPods)
            // Note: Firebase Crashlytics will be added via Podfile
        }

        // Desktop-only dependencies
        val desktopMain by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.java)
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.sqldelight.jvm.driver)
            implementation(libs.coil.network.ktor3)
            implementation(libs.coil.network.okhttp)
        }

        // Tests
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.junit)
                implementation(libs.turbine)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }

    // workaround for MPP testClasses task
    task("testClasses")
}

android {
    namespace = "com.larryyu.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        debugImplementation(libs.androidx.ui.tooling)
        implementation(libs.androidx.compose.foundation.layout)
    }
}

sqldelight {
    databases {
        create("ValorantDatabase") {
            packageName.set("com.larryyu.db")
            // SQLDelight input should be in src/commonMain/sqldelight
            srcDirs.setFrom("src/commonMain/sqldelight")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/com/larryyu/db"))
            migrationOutputDirectory.set(file("src/commonMain/sqldelight/com/larryyu/migrations"))
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.larryyu.shared"
            packageVersion = "1.0.0"
        }
    }
}
