# Last Defenders - Code Improvement Suggestions

**Generated:** 2025-10-17
**Codebase Stats:** ~19,500 LOC, 349 Java files
**Overall Grade:** B+ (Very Good)

---

## ✅ Recently Completed

### Wave System Refactoring (Oct 2025)
- ✅ Created `WaveLoaderStrategy` interface
- ✅ Implemented `HybridWaveLoaderStrategy` for clean wave transitions
- ✅ Fixed exponential growth bug (was doubling, now linear 10% scaling)
- ✅ Simplified `Level.java` from 23 lines to 7 lines for wave loading
- ✅ Removed confusing dual-check transition logic

**Impact:** Eliminated entire class of bugs, improved maintainability significantly.

---

## 🔥 HIGH PRIORITY Suggestions

### 1. Replace String-Based Type System with Enums

**Problem:** Tower and enemy types use string literals throughout codebase.

**Location:**
- `CombatActorFactory.java:98-169` - Two large switch statements
- `EnlistView.java:108-117` - Magic strings for tower creation
- Wave JSON files use strings
- `DynamicWaveLoader.java:88` - Fragile string parsing

**Issues:**
- ❌ No compile-time safety (typos cause runtime errors)
- ❌ No IDE autocomplete or refactoring support
- ❌ Repeated logic across multiple files
- ❌ Hard to add new types

**Solution:**
```java
// New: TowerType.java
public enum TowerType {
    RIFLE("Rifle"),
    TANK("Tank"),
    HUMVEE("Humvee"),
    SNIPER("Sniper"),
    MACHINE_GUN("MachineGun"),
    ROCKET_LAUNCHER("RocketLauncher"),
    FLAME_THROWER("FlameThrower");

    private final String displayName;

    TowerType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TowerType fromString(String str) {
        for (TowerType type : values()) {
            if (type.displayName.equals(str)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid tower type: " + str);
    }
}

// New: EnemyType.java (same pattern)
```

**Updated Factory:**
```java
public <T extends Tower> T loadTower(TowerType type, boolean addToGroup) {
    Logger.info("Loading tower: " + type);

    TowerPool<? extends Tower> pool = switch (type) {
        case RIFLE -> towerRiflePool;
        case TANK -> towerTankPool;
        case HUMVEE -> towerHumveePool;
        case SNIPER -> towerSniperPool;
        case MACHINE_GUN -> towerMachinePool;
        case ROCKET_LAUNCHER -> towerRocketLauncherPool;
        case FLAME_THROWER -> towerFlameThrowerPool;
    };
    // Compiler enforces exhaustiveness - no default case needed!

    @SuppressWarnings("unchecked")
    T tower = (T) pool.obtain();

    if (addToGroup) {
        actorGroups.getTowerGroup().addActor(tower);
    }

    return tower;
}
```

**Benefits:**
- ✅ Compile-time type safety
- ✅ IDE autocomplete and refactoring
- ✅ Impossible to have typos
- ✅ Exhaustiveness checking (compiler ensures all cases handled)
- ✅ Clean JSON deserialization with `fromString()`

**Effort:** ~2 hours
**Files to change:**
- Create `TowerType.java` and `EnemyType.java`
- Update `CombatActorFactory.java`
- Update `EnlistView.java`
- Update `FileWaveLoader.java` to deserialize enums
- Update `DynamicWaveLoader.java`

---

### 2. Externalize Tower Display Config

**Problem:** Hardcoded display values in UI code.

**Location:** `EnlistView.java:106-117`
```java
// TODO attribute scale should be stored somewhere.
createTowerButton(enlistTable, skin, "Rifle", towerCosts.get("Rifle"), 4, 4, 5, 3);
createTowerButton(enlistTable, skin, "Sniper", towerCosts.get("Sniper"), 7, 8, 10, 1);
```

**Issues:**
- ❌ Magic numbers scattered in code
- ❌ Can't tweak UI without recompiling
- ❌ Inconsistency risk between actual and display stats
- ❌ TODO has been there a while!

**Solution:** Create `tower-display-config.json`
```json
{
  "Rifle": {
    "displayName": "Rifle Soldier",
    "healthBars": 4,
    "attackBars": 4,
    "rangeBars": 5,
    "speedBars": 3,
    "description": "Balanced all-around soldier",
    "icon": "rifle-icon"
  },
  "Sniper": {
    "displayName": "Sniper",
    "healthBars": 7,
    "attackBars": 8,
    "rangeBars": 10,
    "speedBars": 1,
    "description": "Long-range precision specialist",
    "icon": "sniper-icon"
  }
  // ... etc
}
```

**Load in Resources.java:**
```java
private Map<TowerType, TowerDisplayConfig> displayConfigs;

private void loadTowerDisplayConfigs() {
    Json json = new Json();
    FileHandle file = Gdx.files.internal("game/ui/tower-display-config.json");
    // Deserialize into map
}
```

**Benefits:**
- ✅ Game designers can tweak without code changes
- ✅ Consistency with actual attributes
- ✅ Easy to add new towers
- ✅ Addresses TODO
- ✅ Supports localization later

**Effort:** ~1 hour
**Files to change:**
- Create `tower-display-config.json`
- Create `TowerDisplayConfig.java` (simple POJO)
- Update `Resources.java` to load config
- Update `EnlistView.java` to use config

---

### 3. Refactor CombatActorFactory (Reduce from 495 lines)

**Problem:** Large factory with repetitive pool declarations.

**Location:** `CombatActorFactory.java` (495 lines)
```java
// 14 separate pool fields
private TowerPool<TowerRifle> towerRiflePool = new TowerPool<>(TowerRifle.class);
private TowerPool<TowerTank> towerTankPool = new TowerPool<>(TowerTank.class);
// ... 12 more
```

**Solution:** Generic Pool Registry
```java
public class CombatActorFactory {
    private Map<TowerType, TowerPool<? extends Tower>> towerPools = new EnumMap<>(TowerType.class);
    private Map<EnemyType, EnemyPool<? extends Enemy>> enemyPools = new EnumMap<>(EnemyType.class);

    public CombatActorFactory(...) {
        // Register pools
        towerPools.put(TowerType.RIFLE, new TowerPool<>(TowerRifle.class));
        towerPools.put(TowerType.TANK, new TowerPool<>(TowerTank.class));
        // ... etc - or use reflection to auto-register
    }

    public <T extends Tower> T loadTower(TowerType type, boolean addToGroup) {
        TowerPool<? extends Tower> pool = towerPools.get(type);
        if (pool == null) {
            throw new IllegalStateException("No pool for: " + type);
        }

        @SuppressWarnings("unchecked")
        T tower = (T) pool.obtain();

        if (addToGroup) {
            actorGroups.getTowerGroup().addActor(tower);
        }

        return tower;
    }
}
```

**Benefits:**
- ✅ Eliminates 14 field declarations
- ✅ Removes duplicate switch statements (~100 lines)
- ✅ Easier to add new types
- ✅ Could auto-register via reflection/config

**Effort:** ~1 hour
**Impact:** ~150 lines removed

---

## 🎯 MEDIUM PRIORITY Suggestions

### 4. Split Resources.java (406 lines → 3 focused classes)

**Problem:** `Resources.java` has multiple responsibilities.

**Current responsibilities:**
- Asset loading (AssetManager)
- Texture caching
- Attribute loading
- Resolution management
- Game speed (!?)

**Solution:**
```java
// Keep: Resources.java (core asset loading)
public class Resources {
    private AssetManager manager;
    private AssetLoader assetLoader;

    public void load() { ... }
    public void dispose() { ... }
}

// New: AttributeRegistry.java
public class AttributeRegistry {
    private Map<Class, TowerAttributes> towerAttributes;
    private Map<Class, EnemyAttributes> enemyAttributes;

    public void loadFromJson() { ... }
    public TowerAttributes getTowerAttributes(Class<? extends Tower> clazz) { ... }
}

// New: TextureCache.java
public class TextureCache {
    private Map<String, TextureRegion> textures;
    private Map<String, Array<AtlasRegion>> atlasRegions;

    public TextureRegion get(String name) { ... }
    public Array<AtlasRegion> getAtlasRegions(String name) { ... }
}

// New: GameSettings.java (for game speed, etc.)
public class GameSettings {
    private float gameSpeed = 1.0f;
    public void setGameSpeed(float speed) { ... }
}
```

**Benefits:**
- ✅ Single Responsibility Principle
- ✅ Easier testing (mock TextureCache independently)
- ✅ Clearer dependencies
- ✅ Each class < 200 lines

**Effort:** ~2 hours
**Impact:** Significant maintainability improvement

---

### 5. Address Active TODOs

**Total TODOs found:** 6

#### Priority 1: `Enemy.java:162`
```java
// TODO move this to state
public void preAttack() {
    rotationBeforeAttacking = getRotation();
}

public void postAttack() {
    setRotation(rotationBeforeAttacking);
}
```

**Issue:** Attack rotation should be managed by state machine (you already use state pattern!)

**Fix:**
```java
// In EnemyAttackingState.java
@Override
public void enter() {
    savedRotation = enemy.getRotation();
    // Attack logic
}

@Override
public void exit() {
    enemy.setRotation(savedRotation);
}
```

**Effort:** ~30 minutes

---

#### Priority 2: `LDGame.java:52`
```java
// TODO remove this
public LDGame() {
    // Needed for launcher without play services
}
```

**Issue:** Empty constructor for testing/special launchers

**Fix:** Null Object Pattern
```java
public LDGame() {
    this(new NullGooglePlayServices(),
         new NullAdController(),
         new NullEventLogger(),
         new NullPurchaseManager(),
         new NullErrorReporter());
}
```

**Effort:** ~30 minutes (create null implementations)

---

#### Priority 3: `TowerPlacement.java:92`
```java
//TODO this is here mostly for testing. Can probably be removed for production
if (condition) {
    // testing code
}
```

**Fix:** Use `DebugOptions` properly
```java
if (DebugOptions.ALLOW_OVERLAPPING_TOWERS) {
    // testing code
}
```

**Effort:** ~10 minutes

---

#### Priority 4: `EnlistView.java:106`
Already covered in suggestion #2

---

### 6. Improve Static Field Usage

**Found:** 181 static field usages across 48 files

**Problematic patterns:**

#### `CollisionDetection.java:26-28`
```java
private static Rectangle clickRect = new Rectangle(0, 0, 0, 0);
private static Polygon rectPoly = new Polygon();
private static float rectanglePoints[] = new float[8];
```

**Issue:** Shared mutable state - not thread-safe, harder to test

**Fix:** Make instance-based or use ThreadLocal
```java
public class CollisionDetection {
    // Instance fields
    private final Rectangle clickRect = new Rectangle();
    private final Polygon rectPoly = new Polygon();
    private final float[] rectanglePoints = new float[8];

    // Methods become instance methods
    public boolean towerHit(...) { ... }
}
```

**Similar issues in:**
- `Damage.java:23` - `private static Circle aoeRadius`
- `Apache.java` - Multiple static constants
- `AirStrike.java` - Multiple static constants

**Benefits:**
- ✅ Thread-safe
- ✅ Easier to test (can mock)
- ✅ No hidden shared state

**Effort:** ~1-2 hours for CollisionDetection and Damage classes

---

### 7. Reduce Large View Classes

**Files over 300 lines:** 13 files

**Largest offenders:**
- `CombatActorFactory.java` - 495 lines (covered in #3)
- `Resources.java` - 406 lines (covered in #4)
- `HUDView.java` - 406 lines
- `OptionsView.java` - 404 lines
- `EnlistView.java` - 359 lines

**Problem:** View classes mix layout creation and event handling

**Solution:** Extract Layout Builders
```java
// EnlistView.java - simplified
public class EnlistView extends Group {
    private EnlistViewLayout layout;
    private EnlistPresenter presenter;

    public void init(Skin skin) {
        layout = new EnlistViewLayout(resources);
        layout.build(this, skin);
        bindEventHandlers();
    }

    private void bindEventHandlers() {
        layout.getTowerButton(TowerType.RIFLE).addListener(
            new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    presenter.towerButtonPressed(TowerType.RIFLE);
                }
            }
        );
        // Other bindings
    }
}

// New: EnlistViewLayout.java (~150 lines)
public class EnlistViewLayout {
    private Map<TowerType, ImageButton> towerButtons = new EnumMap<>(TowerType.class);

    public void build(Group parent, Skin skin) {
        Table enlistTable = createEnlistTable(skin);
        addTowerButtons(enlistTable, skin);
        parent.addActor(enlistTable);
    }

    public ImageButton getTowerButton(TowerType type) {
        return towerButtons.get(type);
    }

    private Table createEnlistTable(Skin skin) { ... }
    private void addTowerButtons(Table table, Skin skin) { ... }
}
```

**Benefits:**
- ✅ Separation of layout and logic
- ✅ Reusable layouts
- ✅ Easier to test views
- ✅ Each file < 250 lines

**Effort:** ~30 minutes per view (incremental)

---

## 📊 LOW PRIORITY (Polish)

### 8. Improve Exception Handling

**Issue:** Generic `Exception` catches found in 8 places

**Example:** `GooglePlayServicesHelper.java:186,196,209`
```java
} catch (Exception e) {
    // Silent or generic handling
}
```

**Fix:** Catch specific exceptions
```java
} catch (ActivityNotFoundException e) {
    Logger.error("Activity not found for Play Games", e);
    errorReporter.report(e);
    showUserMessage("Google Play Games not available");
} catch (SecurityException e) {
    Logger.error("Permission denied for Play Games", e);
    requestPermission();
}
```

**Effort:** ~10-15 mins per location (incremental)

---

### 9. Remove Direct System.out Usage

**Found:** 2 files still using System.out
- `Resources.java:88-89`
- `AbstractScreen.java:49-55`

**Fix:** Use Logger instead
```java
// Before
System.out.println("Screen Width: " + Gdx.graphics.getWidth());

// After
Logger.info("Screen Width: " + Gdx.graphics.getWidth());
```

**Effort:** ~5 minutes

---

### 10. Enhance DebugOptions

**Current:** `DebugOptions.java` (11 lines)
```java
public class DebugOptions {
    public static boolean showFPS;
    public static boolean showTextureBoundaries;
}
```

**Enhancement:**
```java
public class DebugOptions {
    // Visual Debug
    public static boolean showFPS = false;
    public static boolean showTextureBoundaries = false;
    public static boolean showCollisionShapes = false;
    public static boolean showAITargets = false;

    // Gameplay Debug
    public static boolean unlimitedMoney = false;
    public static boolean unlimitedLives = false;
    public static boolean allowOverlappingTowers = false;
    public static boolean skipToWave = -1; // -1 = disabled

    // Performance Debug
    public static boolean logFrameTime = false;
    public static boolean logActorCounts = false;

    // Load from preferences or debug menu
    public static void loadFromPreferences() { ... }
}
```

**Benefits:**
- ✅ Better debugging/testing
- ✅ QA can use debug builds
- ✅ Can toggle at runtime

**Effort:** ~1 hour

---

### 11. Consider Kotlin Migration (Future)

**Status:** Kotlin already in build.gradle - ready for gradual migration

**Benefits of Kotlin:**
- ✅ Null safety (no more NullPointerExceptions)
- ✅ Data classes (automatic equals/hashCode/toString)
- ✅ Sealed classes (perfect for state machines)
- ✅ Extension functions
- ✅ Coroutines for async operations

**Example - Before (Java):**
```java
public <T extends Tower> T loadTower(String type, boolean addToGroup) {
    Logger.info("Combat Actor Factory: loading tower: " + type);
    TowerPool<? extends Tower> towerPool = null;
    switch (type) {
        case "Rifle":
            towerPool = towerRiflePool;
            break;
        // ... many cases
    }
    @SuppressWarnings("unchecked")
    T tower = (T) towerPool.obtain();
    return tower;
}
```

**After (Kotlin):**
```kotlin
fun <T : Tower> loadTower(type: TowerType, addToGroup: Boolean = false): T {
    Logger.info("Loading tower: $type")

    val pool = when (type) {
        TowerType.RIFLE -> towerRiflePool
        TowerType.TANK -> towerTankPool
        // ... compiler enforces exhaustiveness
    }

    return pool.obtain() as T
}
```

**Recommendation:** Start with new features, gradually migrate hot paths

**Effort:** Incremental, no deadline needed

---

## 📈 Performance Considerations

### 12. Profile Object Pooling

**Current:** Good object pooling already in place!
- `TowerPool`, `EnemyPool`
- `SpawningEnemyPool`
- `UtilPool` for Vector2
- `LDPoolable` interface

**Recommendation:** Profile to verify effectiveness
```bash
# Check object allocation during gameplay
./gradlew :core:test --profile
```

**Consider:** Pool for frequently created objects:
- Projectiles (already pooled? check)
- Effects (check WaveOverCoinEffect, etc.)
- UI elements if frequently created/destroyed

---

### 13. Optimize Null Checks

**Found:** 57 null checks across 38 files

**Most are fine, but some patterns to watch:**
```java
// Repeated checks in loops
for (Actor actor : actors) {
    if (actor != null && !actor.isDead()) {
        // process
    }
}
```

**Consider:** Ensure collections never contain nulls
- Use `Objects.requireNonNull()` when adding
- Document null expectations clearly

---

## 🎯 Summary & Roadmap

### Quick Wins (< 2 hours total)
1. ✅ Create TowerType and EnemyType enums (~2 hours)
2. ✅ Externalize display config (~1 hour)

**Impact:** Eliminates entire class of bugs, addresses TODO

---

### Medium Effort (4-6 hours total)
3. ✅ Refactor CombatActorFactory (~1 hour)
4. ✅ Split Resources.java (~2 hours)
5. ✅ Address TODOs (~1.5 hours)
6. ✅ Fix static field issues in Collision/Damage (~1.5 hours)

**Impact:** Significantly improves maintainability

---

### Ongoing/Incremental
7. ✅ Extract view layouts (per view, as touched)
8. ✅ Improve exception handling (as touched)
9. ✅ Consider Kotlin for new features

---

## 🏆 Overall Assessment

**Your code is EXCELLENT for an indie game!**

**Strengths:**
- ✅ Proper design patterns (State, Observer, Factory, Strategy, Pooling)
- ✅ Clean separation of concerns (MVP pattern)
- ✅ Good testing infrastructure (102 test files!)
- ✅ Performance-conscious (object pooling)
- ✅ Multi-platform support working
- ✅ Proper event-driven architecture

**These suggestions are refinements, not fixes.**

The codebase shows maturity and professional architecture. The wave system refactoring we just completed is exactly the kind of improvement that takes good code to great code.

---

## 📝 Notes

- **Don't rush:** Implement gradually as you touch related code
- **Test thoroughly:** Your test suite is a strength - use it!
- **Keep it fun:** These are suggestions, not requirements
- **You're doing great:** This is a well-architected game project

**Questions or want help implementing any of these?** Just ask!

---

*Document created: October 17, 2025*
*Last updated: October 17, 2025*
