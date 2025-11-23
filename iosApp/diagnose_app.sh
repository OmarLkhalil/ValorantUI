#!/bin/bash

# iOS App Crash Diagnostic Script
# Run this to capture detailed crash information

echo "🔍 iOS App Crash Diagnostic Tool"
echo "================================="
echo ""

APP_BUNDLE_ID="com.larryyu.valorantui"
SIMULATOR_NAME="iPhone 15 Pro"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "📱 App Bundle ID: $APP_BUNDLE_ID"
echo "📲 Simulator: $SIMULATOR_NAME"
echo ""

# Step 1: Boot simulator
echo "1️⃣  Booting simulator..."
DEVICE_ID=$(xcrun simctl list devices | grep "$SIMULATOR_NAME" | grep -v unavailable | head -1 | grep -oE '\([A-F0-9\-]+\)' | tr -d '()')

if [ -z "$DEVICE_ID" ]; then
    echo "${RED}❌ Simulator not found!${NC}"
    exit 1
fi

echo "   Device ID: $DEVICE_ID"
xcrun simctl boot "$DEVICE_ID" 2>/dev/null || echo "   (Already booted)"
sleep 2
echo "${GREEN}✅ Simulator booted${NC}"
echo ""

# Step 2: Check if app is installed
echo "2️⃣  Checking app installation..."
if xcrun simctl get_app_container "$DEVICE_ID" "$APP_BUNDLE_ID" 2>/dev/null; then
    echo "${GREEN}✅ App is installed${NC}"
    APP_PATH=$(xcrun simctl get_app_container "$DEVICE_ID" "$APP_BUNDLE_ID" app 2>/dev/null)
    echo "   Path: $APP_PATH"
else
    echo "${RED}❌ App not installed!${NC}"
    echo "   Build and run from Xcode first."
    exit 1
fi
echo ""

# Step 3: Clear previous logs
echo "3️⃣  Clearing old logs..."
rm -f app_launch.log crash_report.log
echo "${GREEN}✅ Logs cleared${NC}"
echo ""

# Step 4: Start log streaming in background
echo "4️⃣  Starting log stream..."
xcrun simctl spawn "$DEVICE_ID" log stream --predicate 'process == "iosApp"' --level debug > app_launch.log 2>&1 &
LOG_PID=$!
echo "   Log PID: $LOG_PID"
echo "${GREEN}✅ Log streaming started${NC}"
sleep 1
echo ""

# Step 5: Launch app
echo "5️⃣  Launching app..."
echo "   ${YELLOW}Watch for crashes...${NC}"
echo ""

xcrun simctl launch --console "$DEVICE_ID" "$APP_BUNDLE_ID" 2>&1 | tee -a app_launch.log
LAUNCH_STATUS=$?

sleep 5

# Step 6: Check if app is still running
echo ""
echo "6️⃣  Checking app status..."
if xcrun simctl spawn "$DEVICE_ID" launchctl print system | grep -q "$APP_BUNDLE_ID"; then
    echo "${GREEN}✅ App is running${NC}"
else
    echo "${RED}❌ App crashed or terminated${NC}"
fi
echo ""

# Step 7: Analyze logs
echo "7️⃣  Analyzing logs..."
echo ""

# Kill log stream
kill $LOG_PID 2>/dev/null
sleep 1

# Check for crashes in system logs
echo "   Searching for crash indicators..."
if grep -i "exception\|fatal\|crash\|assertion\|abort" app_launch.log > crash_report.log; then
    echo "${RED}⚠️  Crash indicators found!${NC}"
    echo ""
    echo "   Top errors:"
    echo "   ============"
    grep -i "exception\|fatal\|crash\|assertion\|abort" app_launch.log | head -10 | while read line; do
        echo "   ${RED}$line${NC}"
    done
else
    echo "${GREEN}✅ No obvious crash indicators in logs${NC}"
fi
echo ""

# Check for SQLite errors
if grep -i "sqlite\|database" app_launch.log; then
    echo "${YELLOW}⚠️  Database-related messages found${NC}"
    grep -i "sqlite\|database" app_launch.log | while read line; do
        echo "   $line"
    done
    echo ""
fi

# Check for Firebase errors
if grep -i "firebase\|crashlytics" app_launch.log; then
    echo "   Firebase initialization:"
    grep -i "firebase\|crashlytics" app_launch.log | while read line; do
        echo "   $line"
    done
    echo ""
fi

# Check for Koin errors
if grep -i "koin" app_launch.log; then
    echo "   Koin initialization:"
    grep -i "koin" app_launch.log | while read line; do
        echo "   $line"
    done
    echo ""
fi

# Step 8: Check system crash reports
echo "8️⃣  Checking system crash reports..."
CRASH_DIR="$HOME/Library/Logs/DiagnosticReports"
LATEST_CRASH=$(ls -t "$CRASH_DIR"/iosApp*.crash 2>/dev/null | head -1)

if [ -n "$LATEST_CRASH" ]; then
    echo "${RED}⚠️  Found crash report!${NC}"
    echo "   Location: $LATEST_CRASH"
    echo ""
    echo "   Crash summary:"
    echo "   =============="
    head -50 "$LATEST_CRASH" | grep -A 5 "Exception Type\|Thread 0 Crashed"
    echo ""
    echo "   ${YELLOW}Full crash report saved${NC}"
    cp "$LATEST_CRASH" ./crash_report_full.txt
    echo "   File: crash_report_full.txt"
else
    echo "${GREEN}✅ No recent crash reports found${NC}"
fi
echo ""

# Step 9: Summary
echo "================================="
echo "📊 DIAGNOSTIC SUMMARY"
echo "================================="
echo ""

if [ -f crash_report.log ] && [ -s crash_report.log ]; then
    echo "${RED}❌ CRASH DETECTED${NC}"
    echo ""
    echo "Files created:"
    echo "  • app_launch.log - Full app output"
    echo "  • crash_report.log - Extracted errors"
    if [ -f crash_report_full.txt ]; then
        echo "  • crash_report_full.txt - System crash report"
    fi
    echo ""
    echo "Next steps:"
    echo "1. Open crash_report.log to see errors"
    echo "2. Search for the first error/exception"
    echo "3. Fix the issue in the code"
    echo "4. Rebuild and test again"
elif [ $LAUNCH_STATUS -eq 0 ]; then
    echo "${GREEN}✅ APP LAUNCHED SUCCESSFULLY${NC}"
    echo ""
    echo "If the app still crashes after launch:"
    echo "1. Check Firebase Console for crash reports"
    echo "2. Open app_launch.log for detailed logs"
    echo "3. Use Xcode debugger for real-time debugging"
else
    echo "${YELLOW}⚠️  LAUNCH STATUS UNCLEAR${NC}"
    echo ""
    echo "Check app_launch.log for details"
fi

echo ""
echo "🔍 Logs saved in: $(pwd)"
echo ""

