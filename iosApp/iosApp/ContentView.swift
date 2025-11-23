import UIKit
import SwiftUI
import ComposeApp
import FirebaseCrashlytics

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ZStack {
            ComposeView()
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler

            // Test crash button (only visible in debug builds)
            #if DEBUG
            VStack {
                Spacer()
                Button(action: {
                    // Force a test crash as per Firebase documentation
                    fatalError("Test Crash - Firebase Crashlytics")
                }) {
                    Text("🧪 Test Crash")
                        .foregroundColor(.white)
                        .padding()
                        .background(Color.red)
                        .cornerRadius(8)
                }
                .padding(.bottom, 50)
            }
            #endif
        }
    }
}

