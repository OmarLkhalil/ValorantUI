#!/bin/bash

# 🔥 Firebase Crashlytics - Add Build Phase Script
# This script adds the required run script to upload dSYM files

echo "🔥 Firebase Crashlytics - Adding Build Phase Script"
echo "=================================================="
echo ""

PROJECT_FILE="iosApp.xcodeproj/project.pbxproj"

if [ ! -f "$PROJECT_FILE" ]; then
    echo "❌ Error: project.pbxproj not found!"
    echo "   Make sure you're in the iosApp directory"
    exit 1
fi

echo "📋 Checking if script already exists..."

if grep -q "FirebaseCrashlytics/run" "$PROJECT_FILE"; then
    echo "✅ Crashlytics run script already exists!"
    exit 0
fi

echo "⚠️  Crashlytics run script NOT found in Xcode project"
echo ""
echo "📝 MANUAL SETUP REQUIRED:"
echo "========================="
echo ""
echo "Please follow these steps in Xcode:"
echo ""
echo "1. Open: iosApp.xcworkspace"
echo "   Command: open iosApp.xcworkspace"
echo ""
echo "2. In Xcode:"
echo "   • Select 'iosApp' project in navigator"
echo "   • Select 'iosApp' target"
echo "   • Go to 'Build Phases' tab"
echo ""
echo "3. Add New Run Script Phase:"
echo "   • Click '+' button"
echo "   • Choose 'New Run Script Phase'"
echo ""
echo "4. Configure the script:"
echo "   • Name: Upload Crashlytics dSYM"
echo "   • Shell: /bin/sh"
echo "   • Script:"
echo '     "${PODS_ROOT}/FirebaseCrashlytics/run"'
echo ""
echo "5. Add Input Files:"
echo '   ${DWARF_DSYM_FOLDER_PATH}/${DWARF_DSYM_FILE_NAME}/Contents/Resources/DWARF/${TARGET_NAME}'
echo '   $(SRCROOT)/$(BUILT_PRODUCTS_DIR)/$(INFOPLIST_PATH)'
echo ""
echo "6. IMPORTANT: Move this script phase AFTER 'Compile Sources'"
echo ""
echo "7. Save and rebuild the project"
echo ""
echo "=================================================="
echo ""
echo "📖 For detailed instructions, see:"
echo "   iosApp/FIREBASE_CRASHLYTICS_SETUP.md"
echo ""

