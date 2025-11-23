#!/bin/bash

# iOS App Diagnostic Script
# Checks if app crashes and captures crash logs

set -e

SIMULATOR_UDID="$1"
BUNDLE_ID="com.larryyu.valorantui"

if [ -z "$SIMULATOR_UDID" ]; then
    echo "❌ Usage: $0 <SIMULATOR_UDID>"
    exit 1
fi

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🔍 iOS App Diagnostic Check"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "📱 Simulator: $SIMULATOR_UDID"
echo "📦 Bundle ID: $BUNDLE_ID"
echo ""

# Boot simulator
echo "🔌 Booting simulator..."
xcrun simctl boot "$SIMULATOR_UDID" 2>/dev/null || echo "Already booted"
sleep 2

# Clear previous logs
echo "🧹 Clearing previous crash logs..."
rm -rf ~/Library/Logs/DiagnosticReports/${BUNDLE_ID}* 2>/dev/null || true

# Launch app
echo "🚀 Launching app..."
xcrun simctl launch --console "$SIMULATOR_UDID" "$BUNDLE_ID" &
LAUNCH_PID=$!

# Wait and check if still running
echo "⏳ Waiting 5 seconds..."
sleep 5

# Check if app is running
echo ""
echo "🔍 Checking app status..."
RUNNING=$(xcrun simctl spawn "$SIMULATOR_UDID" launchctl list | grep "$BUNDLE_ID" || echo "")

if [ -z "$RUNNING" ]; then
    echo "❌ App is NOT running! Crashed or failed to launch."
    echo ""

    # Check crash logs
    echo "📋 Checking crash logs..."
    CRASH_LOG=$(ls -t ~/Library/Logs/DiagnosticReports/${BUNDLE_ID}* 2>/dev/null | head -1 || echo "")

    if [ -n "$CRASH_LOG" ]; then
        echo "💥 Found crash log: $CRASH_LOG"
        echo ""
        echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        echo "CRASH LOG EXCERPT:"
        echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        head -50 "$CRASH_LOG"
        echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    else
        echo "⚠️  No crash log found yet"
    fi

    # Check console logs
    echo ""
    echo "📋 Last console output:"
    xcrun simctl spawn "$SIMULATOR_UDID" log show --predicate 'processImagePath contains "iosApp"' --last 30s 2>/dev/null || echo "No logs found"

else
    echo "✅ App is running successfully!"
    echo "Process info: $RUNNING"
fi

# Terminate
echo ""
echo "🧹 Terminating app..."
xcrun simctl terminate "$SIMULATOR_UDID" "$BUNDLE_ID" 2>/dev/null || echo "App already terminated"

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "✅ Diagnostic check complete"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

