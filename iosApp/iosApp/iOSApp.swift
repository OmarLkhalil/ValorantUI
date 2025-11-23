import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        KoinHelperKt.initKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
