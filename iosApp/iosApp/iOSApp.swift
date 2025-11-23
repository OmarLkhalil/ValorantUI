import UIKit
import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        // Initialize Koin for iOS
        MainViewControllerKt.initKoinIos()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

struct ContentView: View {
    @Environment(\.colorScheme) var colorScheme

    var body: some View {
        ComposeView()
            .ignoresSa2feArea(.keyboard)
            .preferredColorScheme(nil) // Allow system theme switching
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // Update for theme changes if needed
    }
}

