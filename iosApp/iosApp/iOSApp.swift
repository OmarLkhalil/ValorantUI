import SwiftUI
import ComposeApp

extension KoinHelper {
    func doInitKoin() {
        KoinHelper.shared.doInitKoinFromSwift()
    }
}

@main
struct iOSApp: App {
    init() {
        KoinHelper().doInitKoin()
        print("DEBUG: iOSApp - Koin initialized")
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
