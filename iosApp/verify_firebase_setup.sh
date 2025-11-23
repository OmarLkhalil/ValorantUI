#!/bin/bash

# Firebase Crashlytics Verification Script
# This script checks if Firebase is properly configured

echo "🔍 Checking Firebase Crashlytics Setup..."
echo "=========================================="
echo ""

# Check 1: GoogleService-Info.plist
echo "📋 Check 1: GoogleService-Info.plist"
if [ -f "iosApp/GoogleService-Info.plist" ]; then
    echo "✅ Found GoogleService-Info.plist"

    # Check Bundle ID
    BUNDLE_ID=$(grep -A 1 "BUNDLE_ID" iosApp/GoogleService-Info.plist | grep "string" | sed 's/.*<string>\(.*\)<\/string>/\1/')
    echo "   Bundle ID: $BUNDLE_ID"

    if [ "$BUNDLE_ID" = "com.larryyu.valorantui" ]; then
        echo "   ✅ Bundle ID matches!"
    else
        echo "   ⚠️  Bundle ID mismatch! Expected: com.larryyu.valorantui"
    fi
else
    echo "❌ GoogleService-Info.plist NOT FOUND!"
    echo "   Expected location: iosApp/GoogleService-Info.plist"
fi
echo ""

# Check 2: Podfile
echo "📋 Check 2: Podfile Configuration"
if grep -q "FirebaseCrashlytics" iosApp/Podfile; then
    echo "✅ Firebase Crashlytics found in Podfile"
else
    echo "❌ Firebase Crashlytics NOT in Podfile!"
fi

if grep -q "FirebaseCore" iosApp/Podfile; then
    echo "✅ Firebase Core found in Podfile"
else
    echo "❌ Firebase Core NOT in Podfile!"
fi
echo ""

# Check 3: Pods installed
echo "📋 Check 3: CocoaPods Installation"
if [ -d "iosApp/Pods" ]; then
    echo "✅ Pods directory exists"

    if [ -f "iosApp/Pods/Manifest.lock" ]; then
        echo "✅ Pods are installed"

        # Check Firebase version
        if grep -q "FirebaseCrashlytics" iosApp/Pods/Manifest.lock; then
            VERSION=$(grep "FirebaseCrashlytics" iosApp/Pods/Manifest.lock | head -1)
            echo "   Firebase version: $VERSION"
        fi
    else
        echo "⚠️  Pods Manifest not found. Run: pod install"
    fi
else
    echo "⚠️  Pods not installed yet. Run: pod install"
fi
echo ""

# Check 4: Swift file
echo "📋 Check 4: iOSApp.swift Configuration"
if grep -q "import FirebaseCore" iosApp/iosApp/iOSApp.swift; then
    echo "✅ Firebase imports found"
else
    echo "❌ Firebase imports NOT found!"
fi

if grep -q "FirebaseApp.configure()" iosApp/iosApp/iOSApp.swift; then
    echo "✅ Firebase initialization found"
else
    echo "❌ Firebase initialization NOT found!"
fi
echo ""

# Check 5: Info.plist Configuration
echo "📋 Check 5: Info.plist Configuration"
if grep -q "FirebaseCrashlyticsCollectionEnabled" iosApp/iosApp/Info.plist; then
    echo "✅ Crashlytics collection enabled in Info.plist"
else
    echo "⚠️  Crashlytics collection not configured in Info.plist"
fi
echo ""

# Check 6: Analytics Enabled
echo "📋 Check 6: Analytics Configuration"
if grep -A 1 "IS_ANALYTICS_ENABLED" iosApp/GoogleService-Info.plist | grep -q "<true/>"; then
    echo "✅ Analytics enabled (for breadcrumb logs)"
else
    echo "⚠️  Analytics disabled - enable for better crash context"
fi
echo ""

# Check 7: Crashlytics Run Script
echo "📋 Check 7: Crashlytics Build Phase Script"
if [ -f "iosApp/iosApp.xcodeproj/project.pbxproj" ]; then
    if grep -q "FirebaseCrashlytics/run" iosApp/iosApp.xcodeproj/project.pbxproj; then
        echo "✅ Crashlytics run script found"
    else
        echo "⚠️  Crashlytics run script NOT found!"
        echo "   ⚠️  This is REQUIRED to upload dSYM files"
        echo "   Run: bash iosApp/add_crashlytics_script.sh"
    fi
fi
echo ""

# Check 8: Xcode Workspace
echo "📋 Check 8: Xcode Workspace"
if [ -f "iosApp/iosApp.xcworkspace/contents.xcworkspacedata" ]; then
    echo "✅ Xcode workspace exists"
    echo "   📂 Open with: open iosApp/iosApp.xcworkspace"
else
    echo "⚠️  Workspace not found. Run pod install first."
fi
echo ""

# Summary
echo "=========================================="
echo "📊 SUMMARY"
echo "=========================================="
echo ""

ERRORS=0
WARNINGS=0

if [ ! -f "iosApp/GoogleService-Info.plist" ]; then
    ERRORS=$((ERRORS + 1))
fi

if ! grep -q "FirebaseCrashlytics" iosApp/Podfile; then
    ERRORS=$((ERRORS + 1))
fi

if [ ! -d "iosApp/Pods" ]; then
    WARNINGS=$((WARNINGS + 1))
fi

if ! grep -q "FirebaseApp.configure()" iosApp/iosApp/iOSApp.swift; then
    ERRORS=$((ERRORS + 1))
fi

if ! grep -q "FirebaseCrashlyticsCollectionEnabled" iosApp/iosApp/Info.plist; then
    WARNINGS=$((WARNINGS + 1))
fi

if ! grep -q "FirebaseCrashlytics/run" iosApp/iosApp.xcodeproj/project.pbxproj; then
    WARNINGS=$((WARNINGS + 1))
    echo "⚠️  Important: Add Crashlytics build phase script manually"
    echo "   See: iosApp/FIREBASE_CRASHLYTICS_SETUP.md"
fi

if [ $ERRORS -eq 0 ] && [ $WARNINGS -eq 0 ]; then
    echo "🎉 All checks passed!"
    echo ""
    echo "Next steps:"
    echo "1. cd iosApp"
    echo "2. pod install"
    echo "3. open iosApp.xcworkspace"
    echo "4. Build & Run (⌘R)"
elif [ $ERRORS -eq 0 ]; then
    echo "⚠️  $WARNINGS warning(s) found"
    echo ""
    echo "Run: cd iosApp && pod install"
else
    echo "❌ $ERRORS error(s) found"
    echo ""
    echo "Please fix the errors above before proceeding."
fi

echo ""

