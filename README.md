# ⚔️ Solo Leveling: System — Hunter Awakening & Workout App

A native Android application themed directly after the iconic **Solo Leveling System HUD interface**. The app serves as a real-world hunter training system that assesses your physical baseline, assigns your hunter rank, scales your daily workout quests dynamically as you level up, tracks your steps with hardware sensors, alerts you with high-priority system notifications, and penalizes you if you neglect your training!

---

## 📸 Visual Styling & Theme

- **Obsidian Dark Background:** `#0B0813` with subtle fractured-ground grid and animated ethereal aura particles.
- **Neon Violet & Vibrant Purple:** `#A855F7`, `#C084FC`, `#7E22CE`.
- **Glowing Magenta HUD Borders:** `#D946EF` with multi-layered neon stroke and soft radial glow simulation.
- **Custom Beveled / Notched HUD Cards:** Authentic cut-corner anime system windows with tech brackets `[ ]`.
- **Vital Gauges:**
  - **HP Meter:** High-intensity crimson red (`#EF4444`) with animated progress and trailing glare.
  - **MP Meter:** Arcane cyan/blue (`#38BDF8`).
  - **Fatigue Meter:** Amber orange (`#F59E0B`).
  - **Quest Progress:** Vibrant purple-to-magenta linear gradient with percentage badges.
- **Typography:** Tech display uppercase styling with wide tracking and high-contrast white text (`#FFFFFF`).

---

## 🔮 Core Features

### 1. Hunter Awakening Assessment (Onboarding Evaluation)
Upon launching the application for the first time, "THE SYSTEM" initiates a Hunter Qualification Evaluation:
- **Hunter Code Name:** Customize your player name (e.g., `SUNG JIN-WOO`).
- **Physical Capacity Evaluation:**
  - **Push-ups:** Novice (<10), Apprentice (10-25), Veteran (26-50), Shadow Monarch (50+).
  - **Sit-ups:** Novice (<15), Apprentice (15-30), Veteran (31-60), Shadow Monarch (60+).
  - **Squats:** Novice (<20), Apprentice (20-40), Veteran (41-70), Shadow Monarch (70+).
  - **Running / Steps:** Sedentary (<2.5k), Moderate (3-5k), Active (6-9k), Extreme Hunter (10k+).
  - **Combat Focus:** Choose between Strength (STR), Agility (AGI), Vitality (VIT), or Intelligence (INT).
- **Rank Calculation:** The System evaluates total mana density and assigns an initial Hunter Rank:
  - `E-Rank Hunter (Evolving)`
  - `D-Rank Hunter (Aspirant)`
  - `C-Rank Hunter (Raid Captain)`
  - `B-Rank Hunter (Strike Leader)`
  - `A-Rank Hunter (Elite Warrior)`
  - `S-Rank Hunter (National Level)`
- **Tailored Workout Plan:** Initial daily targets are customized according to your rank (e.g., E-Rank starts with 20 pushups, 20 situps, 20 squats, 3,000 steps; scaling up to 100/100/100/10k steps).

### 2. Daily Quests Container
- Header: `[QUEST ARRIVED: DAILY TRAINING]`
- **Exercise Trackers:** Push-ups, sit-ups, squats, and running/steps.
- **Dynamic Quest Scaling:** As player Level increases, physical targets scale up proportionally.
- **Countdown Timer:** Active real-time countdown to midnight reset (`REMAINING TIME: HH:MM:SS`).
- **Rewards Claiming:** When all exercises reach 100%, the glowing `[ COMPLETE ]` button activates, granting EXP, Gold, full HP/MP/Fatigue recovery, and level-ups!

### 3. Hardware Step Counter Integration
- Automatically hooks into Android's `Sensor.TYPE_STEP_COUNTER` and `Sensor.TYPE_STEP_DETECTOR`.
- Seamlessly increments the running bar as you walk throughout the day.
- Includes quick step simulation buttons for emulator testing.

### 4. Alarms & System Notifications
- **Daily Quest Alarm:** Schedules exact morning alarms via `AlarmManager` with high-priority heads-up anime alert notifications.
- **Penalty Quest Radar:** At 22:00 (2 hours before midnight), if daily training remains incomplete, the System sends a critical alert:
  > *⚠️ [WARNING: PENALTY QUEST IMMINENT]*
  > *Unfinished daily quests will transport the Player to the Penalty Zone (Survive the Centipede Desert for 4 hours).*
- **In-App Alarm HUD:** Customize alert times, toggle penalty radar, and trigger instant test notifications directly.

### 5. Sub-Quests / Dungeon Gate Clearance
- Optional Gate Clearance cards: D-Rank Goblin Den, C-Rank Cerberus' Lair, B-Rank Ice Elf Kingdom, and the Red Gate!
- **Interactive Boss Fight Modal:** Deplete boss HP through physical workout reps.
- Defeating dungeon bosses awards rare weapons (e.g., *Kasaka's Poison Fang*), Gold, and massive EXP.

### 6. RPG Progression & Hunter Navigation Dock
A floating neon dock containing 4 hunter tabs:
1. **Gate / Portal (Home / Quests):** Daily quests, vital gauges, and gate clearances.
2. **Helmet / Status (Attributes):** Allocate unspent attribute points into STR, AGI, VIT, INT, and PER.
3. **Inventory Box (Items & Rewards):** Equip weapons, consume Full Recovery Elixirs, and manage dungeon keys.
4. **Dual Daggers (Skills & Combat):** Activate skills like *Sprint*, *Bloodlust*, *Stealth*, *Ruler's Authority*, and *Shadow Extraction: ARISE*.

### 7. Local Persistence (Room DB)
- `UserProfileEntity`: Player stats, level, EXP, vitals, gold, and alarm preferences.
- `DailyQuestEntity`: Daily workout tracking, date state, targets, and completion flags.
- `GateQuestEntity`: Dungeon raids, boss health, and clearance state.
- `InventoryItemEntity`: Weapons, potions, and artifacts.
- `SkillEntity`: Hunter abilities, levels, and mana costs.

---

## 🚀 CI/CD Release Pipeline

The repository includes `.github/workflows/build-apk.yml`.
On every push to `main`, GitHub Actions:
1. Sets up JDK 17 (Temurin).
2. Builds the project using Gradle (`./gradlew assembleDebug`).
3. Publishes an automated **GitHub Release** with the installable `app-debug.apk` attached directly as a downloadable asset.

---

## 🛠️ Building & Running Locally

### Prerequisites
- Android Studio Ladybug / Koala or newer, or command line JDK 17+ and Android SDK 34.

### Build via Gradle Wrapper:
```bash
# Windows
.\gradlew.bat assembleDebug

# Linux / macOS
chmod +x gradlew
./gradlew assembleDebug
```
The resulting APK will be generated at:
`app/build/outputs/apk/debug/app-debug.apk`
