#!/bin/bash
echo "📱 osCASH.me Development Status"
echo "================================"
echo "📅 Target: GitHub Launch 30. August 2024 🎂"
echo ""
echo "🔧 Environment:"
echo "Java: $(java -version 2>&1 | head -1)"
echo "Android SDK: ${ANDROID_HOME:-'❌ Not set'}"
echo "NDK: ${NDK_HOME:-'❌ Not set'}"
echo ""
echo "📊 Repository:"
echo "Size: $(du -sh . | cut -f1)"
echo "Commits: $(git rev-list --count HEAD 2>/dev/null || echo 'N/A')"
echo ""
echo "🏗️ Last Build:"
if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo "Debug APK: ✅ $(stat -c%y app/build/outputs/apk/debug/app-debug.apk)"
else
    echo "Debug APK: ❌ Not built yet"
fi
echo ""
DAYS_UNTIL=$(( ($(date -d '2024-08-30' +%s) - $(date +%s)) / 86400 ))
if [ $DAYS_UNTIL -lt 0 ]; then
    echo "🎯 GitHub Launch war vor $((-$DAYS_UNTIL)) Tagen! Zeit für v2! 🎉"
else
    echo "🎯 Days until GitHub Launch: $DAYS_UNTIL days! 🚀"
fi