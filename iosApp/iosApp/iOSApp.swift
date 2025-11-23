import SwiftUI
import ComposeApp
import FirebaseCore
import FirebaseCrashlytics

@main
struct iOSApp: App {
    init() {
        // Initialize Firebase
        print("🔥 Initializing Firebase...")
        FirebaseApp.configure()
        print("✅ Firebase initialized")

        // Enable Crashlytics collection
        Crashlytics.crashlytics().setCrashlyticsCollectionEnabled(true)
        print("📊 Crashlytics enabled")

        // Initialize Koin DI for iOS with error handling
        print("🔧 Initializing Koin DI...")
        do {
            KoinHelperKt.doInitKoin()
            print("✅ Koin initialized successfully")

            // Log successful init to Crashlytics
            Crashlytics.crashlytics().log("Koin DI initialized successfully")
        } catch {
            print("❌ Error initializing Koin: \(error)")

            // Log error to Crashlytics
            Crashlytics.crashlytics().log("Failed to initialize Koin: \(error.localizedDescription)")
            Crashlytics.crashlytics().record(error: error)

            // Create a non-fatal error
            let nsError = error as NSError
            Crashlytics.crashlytics().record(error: nsError)
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}


