#!/bin/bash

# Script to diagnose iOS app crash
# Run this script from the iosApp directory

echo "🔍 iOS App Crash Diagnostics"
echo "=================================="
echo ""

echo "1️⃣ Checking if simulator is running..."
xcrun simctl list devices | grep "Booted"
echo ""

echo "2️⃣ Building the app..."
cd ..
./gradlew :composeApp:embedAndSignAppleFrameworkForXcodeIosSimulatorArm64
cd iosApp
echo ""

echo "3️⃣ Building Xcode project..."
xcodebuild build -project iosApp.xcodeproj -scheme iosApp -sdk iphonesimulator -destination "platform=iOS Simulator,OS=17.5,name=iPhone 15 Pro" -configuration Debug CODE_SIGN_IDENTITY= CODE_SIGNING_REQUIRED=NO -derivedDataPath build
echo ""

echo "4️⃣ Installing app to simulator..."
SIMULATOR_ID=$(xcrun simctl list devices | grep "iPhone 15 Pro" | grep "17.5" | head -1 | grep -o '[A-Z0-9]\{8\}-[A-Z0-9]\{4\}-[A-Z0-9]\{4\}-[A-Z0-9]\{4\}-[A-Z0-9]\{12\}')
echo "Simulator ID: $SIMULATOR_ID"

xcrun simctl install $SIMULATOR_ID build/Build/Products/Debug-iphonesimulator/iosApp.app
echo ""

echo "5️⃣ Launching app and capturing logs..."
xcrun simctl launch --console $SIMULATOR_ID com.larryyu.valorantui &
LAUNCH_PID=$!

# Wait for 10 seconds to capture logs
sleep 10

# Kill the launch process
kill $LAUNCH_PID 2>/dev/null

echo ""
echo "6️⃣ Checking crash logs..."
xcrun simctl spawn $SIMULATOR_ID log show --predicate 'process == "iosApp"' --last 2m

echo ""
echo "=================================="
echo "✅ Diagnostics complete!"
echo ""
echo "If the app crashed, check the logs above for error messages."
echo "Common issues:"
echo "  - SQLite not linked: Check build.gradle.kts for linkerOpts.add(\"-lsqlite3\")"
echo "  - Koin not initialized: Check KoinHelper.kt"
echo "  - Database schema issues: Check ValorantDatabase.sq files"

