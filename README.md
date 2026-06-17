# 🚀 Millennium Travel

## Overview
Millennium Travel is a Star Wars–themed Android driving game built in Kotlin. You pilot a starship through a five-lane hyperspace road, dodging enemy ships and collecting extra life points while your odometer tracks how far you've traveled.

## 🛠️ Features
- **Dual Control Modes**: Players can choose between two steering modes selectable from the main menu:
	- **Buttons Mode**: Tap the left/right buttons to change lanes.
	- **Tilt Mode**: Tilt the device left or right to steer.
- **Controllable Speed**: Tilt the device forward/backward to adjust speed in real time.
- **Location-Based Leaderboard**: High scores are stored locally (using `SharedPreferences`) with the player's name and GPS coordinates (using `Google Maps SDK`).
- **Interactive Map**: Clicking a high score on the leaderboard zooms into the specific location where that score was achieved.
- **Sound Effects**: Background music during gameplay and an explosion sound upon losing the last life point.
  
## 🚀 Getting Started

1. Clone the repository and open it in Android Studio.
2. Add your Google Maps API key to `local.properties`:
	```
 	MAPS_API_KEY=YOUR_API_KEY
 	```
3. Build and run on a physical device (minSdk 36).



**What you'll encounter on the road:**

- **Enemy ships** — hit one and you lose a life. The game ends when all three lives are gone.
- **Rebel Alliance logos** — collecting one restores a lost life.

**Score / Odometer** — distance is shown live during the game and converts from meters to kilometers automatically. On game over, your final distance is shown in a toast notification.


## 🎮 Gameplay

The road scrolls toward you at a constant speed - obstacles come to you. Your ship sits at the bottom of the screen and can move across five lanes. Survive as long as you can with your three lives.

## 💀 Game Over

When the last life is lost:
- A **crash sound** plays and the device **vibrates**.
- A **Toast message** announces the distance you traveled.
- Your score (along with your current GPS coordinates) is saved to the **leaderboard** if it ranks in the top 10.
- The app returns to the main menu automatically.

---

## 🏆 Leaderboard

Accessible from the main menu. Displays the top 10 runs, each showing the distance achieved as a tappable button. Tapping a score pans the embedded **Google Map** to the location where that run ended, marked with a pin.

All scores are stored locally using `SharedPreferences` and serialized with **Gson**.

---

## 🛠️ Technical Highlights

**Game loop** — driven by a `Handler` + `Runnable` on the main thread. The tick interval starts at 1000ms and shrinks dynamically based on device tilt (Y-axis), clamped between 100ms and 1000ms.

**Enemy grid** — represented as a 5×9 `Array<IntArray>`. Each tick, values shift down one row. A `0` means empty, `1` is an enemy ship, `2` is a Rebel logo (coin). A new object spawns randomly at the top with 50% probability for an enemy and 10% (of the remaining 50%) for a coin.

**Collision detection** — checked at the last row of the player's current lane before the grid advances each tick.

**Tilt detection** — the `TiltDetector` wraps the accelerometer with a 300ms debounce on lateral (X-axis) input and a continuous response on the forward/back (Y-axis) for speed control. Threshold: ±3.0 for lane change, ±0.5 for speed.

**Location** — `FusedLocationProviderClient` fetches the device's last known position at game start. Falls back to a hardcoded Tel Aviv coordinate if unavailable.

**Architecture patterns used:**
- Singleton (`SignalManager`, `BackgroundMusicPlayer`, `SharedPreferencesManager`)
- Builder pattern (`HighScore.Builder`)
- Interface callbacks for fragment–activity communication

---

## 🔒 Permissions

```xml
<uses-permission android:name="android.permission.VIBRATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

Location permission is requested at launch via `ActivityCompat.requestPermissions`.

---

## 🚀 Getting Started

1. Clone the repository and open it in Android Studio.
2. Add your Google Maps API key to `local.properties`:
   ```
   MAPS_API_KEY=your_key_here
   ```
3. Build and run on a physical device (minSdk 36; sensor mode requires real hardware).
