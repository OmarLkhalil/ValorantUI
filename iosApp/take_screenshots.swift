#!/usr/bin/swift

import Foundation

// This script automates taking screenshots by simulating UI interactions
// Usage: swift take_screenshots.swift <simulator_udid> <bundle_id> <output_dir> <mode>

let args = CommandLine.arguments
guard args.count >= 5 else {
    print("❌ Usage: swift take_screenshots.swift <simulator_udid> <bundle_id> <output_dir> <mode>")
    exit(1)
}

let simulatorUDID = args[1]
let bundleID = args[2]
let outputDir = args[3]
let mode = args[4] // "light" or "dark"

print("📸 Taking screenshots for \(bundleID) in \(mode) mode")

// Helper function to run shell command
@discardableResult
func shell(_ command: String) -> Int32 {
    let task = Process()
    task.launchPath = "/bin/bash"
    task.arguments = ["-c", command]
    task.launch()
    task.waitUntilExit()
    return task.terminationStatus
}

func waitSeconds(_ seconds: UInt32) {
    Foundation.sleep(seconds)
}

func takeScreenshot(name: String, description: String) {
    print("📷 Screenshot: \(description)")
    shell("xcrun simctl io \(simulatorUDID) screenshot \(outputDir)/\(name)")
    waitSeconds(2)
}

func tap(x: Int, y: Int, description: String) {
    print("👆 \(description)")
    shell("xcrun simctl io \(simulatorUDID) tap \(x) \(y)")
    waitSeconds(3)
}

func swipe(fromX: Int, fromY: Int, toX: Int, toY: Int, description: String) {
    print("↔️  \(description)")
    shell("xcrun simctl io \(simulatorUDID) swipe \(fromX) \(fromY) \(toX) \(toY)")
    waitSeconds(3)
}

// Set appearance mode
print("🎨 Setting appearance to \(mode) mode")
shell("xcrun simctl ui \(simulatorUDID) appearance \(mode)")
waitSeconds(3)

// Launch app
print("🚀 Launching app...")
shell("xcrun simctl launch \(simulatorUDID) \(bundleID)")
waitSeconds(10) // Wait for app to fully load

// ═══════════════════════════════════════════════════════════
// AGENTS SECTION
// ═══════════════════════════════════════════════════════════

// Screenshot 1: Agents Screen (initial screen)
takeScreenshot(name: "01_agents_screen.png", description: "Agents List Screen")

// Tap on first agent (center-top area of grid)
// iPhone 15 Pro screen: 393x852 points
// Assuming grid layout, first item should be around center-left, top third
tap(x: 150, y: 250, description: "Tapping first agent card")
waitSeconds(2) // Extra wait for transition animation

// Screenshot 2: Agent Details
takeScreenshot(name: "02_agent_details.png", description: "Agent Details Screen")

// Go back - swipe from left edge
swipe(fromX: 10, fromY: 400, toX: 300, toY: 400, description: "Swiping back from left edge")

// ═══════════════════════════════════════════════════════════
// GUNS/WEAPONS SECTION
// ═══════════════════════════════════════════════════════════

// Tap bottom navigation to switch to Guns/Weapons
// Bottom navigation typically at bottom of screen
// Assuming 2-tab layout, second tab would be on the right side
// iPhone 15 Pro height: 852, nav bar usually ~80pt from bottom
tap(x: 300, y: 800, description: "Tapping Guns/Weapons tab in bottom navigation")
waitSeconds(2) // Wait for navigation animation

// Screenshot 3: Guns/Weapons Screen
takeScreenshot(name: "03_guns_screen.png", description: "Guns/Weapons List Screen")

// Tap on first gun/weapon
tap(x: 150, y: 250, description: "Tapping first weapon card")
waitSeconds(2) // Extra wait for transition animation

// Screenshot 4: Gun/Weapon Details
takeScreenshot(name: "04_gun_details.png", description: "Weapon Details Screen")

// ═══════════════════════════════════════════════════════════
// CLEANUP
// ═══════════════════════════════════════════════════════════

print("🧹 Terminating app...")
shell("xcrun simctl terminate \(simulatorUDID) \(bundleID)")
waitSeconds(1)

print("")
print("✅ Screenshots completed for \(mode) mode!")
print("   Saved to: \(outputDir)")
print("")

