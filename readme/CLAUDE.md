# Claude Context Loading Protocol - osCASHme/android

**⚡ 60-Second Context Restoration for osCASH.me Tier 3 Development**

## 🚀 Quick Start (Read This First!)

```bash
# You are in: /home/mayer/prog/claude/osCASH.me/android
# Project: osCASH.me Tier 3 Architecture (Molly Fork with crypto payments)
# Status: APK v1.0.0-alpha3 SUCCESSFULLY BUILT & RELEASED ✅
```

## 📱 Current State

### ✅ COMPLETED: Tier 3 Foundation + MOB Payments (2025-08-28)
- **APK Built**: `osCASH.me-v1.0.0-alpha3-MOB-FOSS.apk` (84MB) ✅
- **Package ID**: `me.oscash.app`
- **GitHub Release**: https://github.com/osCASHme/android/releases/tag/v1.0.0-alpha3-MOB
- **Docker Build**: Fully functional, based on Molly-Repro system
- **Architecture**: Multi-module with Molly-Core symlink integration
- **🎉 MOB PAYMENTS**: ✅ **ACTIVATED & RELEASED!**

### 🚀 BREAKTHROUGH ACHIEVED: MobileCoin Payments Active
**Historic milestone**: First crypto-native Signal fork with activated MOB payments!

## 🏗️ Repository Structure

```
osCASHme/android/
├── molly-core/              # Symlink → ../mollyim-android (Molly v7.53.5)
├── settings.gradle.kts      # Multi-module configuration ✅
├── docker-build/            # osCASH.me Docker system ✅
│   ├── docker-compose.oscash.yml
│   ├── Dockerfile.oscash
│   └── .env                 # CI_PACKAGE_ID=me.oscash.app
├── oscash-core/             # osCASH.me extensions (ready for MOB)
│   ├── payments-base/       # Payment engine foundation
│   ├── addon-manager/       # Plugin system
│   └── oscash-config/       # Configuration management
├── addons/                  # Addon system
│   └── qr-gateway/          # QR payment gateway
└── readme/                  # 📚 KNOWLEDGE MANAGEMENT ✅
    ├── osCASH-CRITICAL-FIXES.md    # All build solutions
    ├── SESSION_SUMMARY_2025-08-28.md # Today's achievements  
    └── CLAUDE.md                    # This file
```

## 🔧 Critical Knowledge (READ IMMEDIATELY)

### Working Build Command:
```bash
cd /home/mayer/prog/claude/osCASH.me/android/molly-core/reproducible-builds
CI_APP_TITLE="osCASH.me" \
CI_APP_FILENAME="osCASH.me" \
CI_PACKAGE_ID="me.oscash.app" \
CI_BUILD_VARIANTS="prodFossWebsite" \
docker compose run --rm assemble
```

### Key Environment Variables:
```bash
CI_APP_TITLE="osCASH.me"           # UI display name
CI_APP_FILENAME="osCASH.me"        # APK filename  
CI_PACKAGE_ID="me.oscash.app"      # Android package
CI_BUILD_VARIANTS="prodFossWebsite" # Molly variant filter
```

## ⚠️ Critical Build Fixes (Essential Knowledge!)

**NEVER attempt these patterns** (they fail):
```bash
# ❌ WRONG - Module dependency approach:
implementation(project(":molly-core:app"))  # molly-core:app is Application, not Library!

# ❌ WRONG - Custom docker setup:
command: :app:assembleOscashBasicRelease    # This variant doesn't exist!
```

**✅ CORRECT approach**:
- Use original Molly Docker system with Environment Variables
- Build `:molly-core:app` directly, not as dependency
- Environment variables control branding

## 📚 Deep Knowledge Files

**If you need detailed context**, read in this order:
1. **`osCASH-CRITICAL-FIXES.md`** - All build issues & solutions
2. **`SESSION_SUMMARY_2025-08-28.md`** - Today's complete achievements
3. **Check symlink**: `ls -la molly-core` → should point to `../mollyim-android`

## 🎯 User Context & Goals

### User Profile:
- **Expertise**: Advanced Android/blockchain development
- **Goal**: Create osCASH.me as crypto-enhanced Signal/Molly messenger
- **Approach**: Systematic 3-tier architecture (Signal→Molly→Molly-Repro→osCASH.me)
- **Current Focus**: MOB (MobileCoin) payment integration

### Communication Style:
- **Language**: German preferred for discussion
- **Detail Level**: Technical precision required
- **Planning**: Uses todo lists, systematic approach
- **Verification**: Always wants proof (APK files, GitHub links, working builds)

## 🔮 Immediate Next Steps (When Ready)

1. ✅ **Analyze existing MOB payment code** in Molly-Core (COMPLETED)
2. ✅ **Activate payment functionality** for osCASH.me context (COMPLETED) 
3. **Test MOB transactions** with new package ID
4. **Prepare multi-chain architecture** for future eUSD support
5. **User testing** of MOB send/receive functionality

## 🚨 Emergency Commands

```bash
# If build breaks, check these first:
cd /home/mayer/prog/claude/osCASH.me/android
ls -la molly-core/                    # Verify symlink exists
cat docker-build/.env                 # Check environment vars
cat readme/osCASH-CRITICAL-FIXES.md   # Review all solutions

# Last known working APK:
ls -la ~/osCASH-release/              # Contains v1.0.0-alpha3 APK
```

## 📈 Success Metrics (Current Status)

- ✅ **Tier 3 Architecture**: Complete
- ✅ **Docker Build System**: Working (84MB APK)
- ✅ **osCASH.me Branding**: Package `me.oscash.app`
- ✅ **GitHub Release**: v1.0.0-alpha3-MOB published
- ✅ **Knowledge Management**: This system operational
- ✅ **MOB Integration**: **COMPLETED & RELEASED!** 🎉
- ✅ **Keystore Signing**: Multi-scheme v2+v3 
- ✅ **Payment System**: MobileCoin fully activated

---

**Protocol**: Read this file first, then proceed based on user's immediate request.  
**Context Loaded**: You now understand the complete osCASH.me Tier 3 system! 🚀  
**Ready For**: MOB payment activation in existing working APK.