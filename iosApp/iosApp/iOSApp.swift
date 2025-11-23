import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        // Initialize Koin DI for iOS
        print("🔧 Initializing Koin DI...")
        KoinHelperKt.doInitKoin()
        print("✅ Koin initialized successfully")
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}


