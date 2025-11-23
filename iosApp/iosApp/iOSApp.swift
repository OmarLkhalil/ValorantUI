import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        // Initialize Koin DI for iOS
        print("🔧 Initializing Koin DI...")
        do {
            KoinHelperKt.doInitKoin()
            print("✅ Koin initialized successfully")
        } catch {
            print("❌ Koin initialization failed: \(error)")
            fatalError("Failed to initialize Koin: \(error)")
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}


