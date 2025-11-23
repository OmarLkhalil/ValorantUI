import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {

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


