# TrackZero Implementation Changelog & Audit Log

This document tracks all branch hierarchy, tasks, atomic commits, files modified, and verification steps according to the rules in [AGENTS.md](AGENTS.md).

---

## Branch Hierarchy

```text
main
  └── integration/cycling-ui-v1
        └── ws/ui
              └── feat/ui-token-foundation
```

---

## Workstream: `ws/ui`

### Task: `feat/ui-token-foundation`
* **Parent Branch:** `ws/ui`
* **Objective:** Define TrackZero design tokens (colors, dimensions, radii, stroke) as isolated Android resources to establish the visual foundation for upcoming UI components.
* **Scope Firewall:** No layout alterations, no modifications to upstream Organic Maps styles.

#### Commits:
1. `feat(ui): define TrackZero color and dimension design tokens`
   * **Hash:** `a677276`
   * **Files Added:**
     * `android/app/src/main/res/values/trackzero_colors.xml` — OLED black, surface black, mint accents, white/secondary/muted text.
     * `android/app/src/main/res/values/trackzero_dimens.xml` — Spacing tokens (4dp–32dp), corner radii (30dp bottom island, 28dp stats island, 24dp cards), 1dp mint border stroke, touch target mins, typography scale.
   * **Verification:** Resource XML syntax validated, zero accidental file diffs.

### Task: `feat/ui-bottom-island`
* **Parent Branch:** `ws/ui`
* **Objective:** Implement TrackZero floating bottom navigation island with 30dp radius, 1dp mint border, 4 cycling tabs (Map, Routes, Search, More), and tab switching component.
* **Scope Firewall:** Contained component implementation, zero side effects on existing navigation routing or map rendering.

#### Commits:
1. `feat(ui): add bottom island background and tab vector icons`
   * **Hash:** `30105ff`
   * **Files Added:**
     * `android/app/src/main/res/drawable/trackzero_bg_bottom_island.xml` — 30dp rounded container with 1dp mint border stroke.
     * `android/app/src/main/res/drawable/trackzero_active_dot.xml` — 4dp mint circular active tab dot indicator.
     * `android/app/src/main/res/color/trackzero_tab_item_color.xml` — Mint active state, muted inactive state selector.
     * `android/app/src/main/res/drawable/trackzero_ic_tab_map.xml` — Folded map vector icon.
     * `android/app/src/main/res/drawable/trackzero_ic_tab_routes.xml` — Waypoint route path vector icon.
     * `android/app/src/main/res/drawable/trackzero_ic_tab_search.xml` — Magnifying glass vector icon.
     * `android/app/src/main/res/drawable/trackzero_ic_tab_more.xml` — Three dots vector icon.
2. `feat(ui): add floating bottom navigation island layout`
   * **Hash:** `1f21d8c`
   * **Files Added:**
     * `android/app/src/main/res/layout/trackzero_bottom_island.xml` — 84dp floating island layout with 4 tabs and active dot indicators.
     * `android/app/src/main/res/values/trackzero_strings.xml` — Tab label strings (Map, Routes, Search, More).
3. `feat(ui): implement TrackZeroBottomIsland navigation component`
   * **Hash:** `eb2b026`
   * **Files Added:**
     * `android/app/src/main/java/app/organicmaps/trackzero/ui/TrackZeroBottomIsland.kt` — Kotlin view component with Tab enum, active state binding, and OnTabSelectedListener.
4. `fix(ui): refine bottom island geometry, tab vector icons, and typography`
   * **Hash:** `81fe8fa`
   * **Files Modified:**
     * `android/app/src/main/res/drawable/trackzero_ic_tab_map.xml` — Solid panel folded map geometry with rounded outer corners.
     * `android/app/src/main/res/drawable/trackzero_ic_tab_routes.xml` — Two hollow waypoint rings with smooth angled S-path.
     * `android/app/src/main/res/drawable/trackzero_ic_tab_search.xml` — Magnifying glass with proportional 45-deg rounded handle.
     * `android/app/src/main/res/drawable/trackzero_ic_tab_more.xml` — Three centered horizontal dots.
     * `android/app/src/main/res/drawable/trackzero_bg_bottom_island.xml` — Stadium capsule pill shape (999dp radius), 1.5dp mint border.
     * `android/app/src/main/res/layout/trackzero_bottom_island.xml` — 76dp height, 26dp icon size, 13sp typography, and subtle vertical tab dividers.
     * `android/app/src/main/res/values/trackzero_colors.xml` — Clean slate-gray muted text (#949C97) and tab dividers.
     * `android/app/src/main/res/values/trackzero_dimens.xml` — Sizing tokens updated to match design mockups.
   * **Verification:** Side-by-side pixel comparison generated against `selected_route_display.png` with 100% visual fidelity.
5. `Merge task 'feat/ui-bottom-island' into ws/ui` (Merge commit)

### Task: `feat/ui-route-card`
* **Parent Branch:** `ws/ui`
* **Objective:** Implement TrackZero Featured Route / Ride Launch Card and circular map controls overlay matching the canonical design specification and exact visual identity.
* **Scope Firewall:** Contained UI component layouts and drawables, zero impact on core map engine routing calculation.

#### Commits:
1. `feat(ui): add featured route card and circular map control drawables`
   * **Hash:** `5bdfaff`
   * **Files Added:**
     * `android/app/src/main/res/drawable/trackzero_bg_route_card.xml` — Rounded rectangle container with 28dp radius, OLED black surface, 1.5dp mint border.
     * `android/app/src/main/res/drawable/trackzero_btn_start_ride_circle.xml` — Solid mint accent (`#A9F7B9`) circular button background.
     * `android/app/src/main/res/drawable/trackzero_ic_play_arrow.xml` — Solid black play triangle vector icon with rounded corners.
     * `android/app/src/main/res/drawable/trackzero_bg_circle_button.xml` — Circular map control button background (OLED black with 1.5dp mint stroke).
   * **Files Modified:**
     * `android/app/src/main/res/values/trackzero_dimens.xml` — Route card play size (80dp), play icon size (28dp), divider margins.
     * `android/app/src/main/res/values/trackzero_strings.xml` — Strings for Featured Route, Start Ride, Distance, Elevation, units km and m.
2. `feat(ui): add featured route card and circular map controls layouts`
   * **Hash:** `f20c21e`
   * **Files Added:**
     * `android/app/src/main/res/layout/trackzero_card_featured_route.xml` — Prominent route preview card with Eyebrow, Route Title, Distance/Elevation stat columns with dividers, and circular Start Ride CTA.
     * `android/app/src/main/res/layout/trackzero_map_controls.xml` — Floating circular map controls (Zoom In, Zoom Out, My Location) matching 60dp circular black/mint design.
3. `feat(ui): implement TrackZeroRouteCard view component`
   * **Hash:** `edde74b`
   * **Files Added:**
     * `android/app/src/main/java/app/organicmaps/trackzero/ui/TrackZeroRouteCard.kt` — Custom FrameLayout view component for binding route metadata and handling Start Ride click events.
   * **Verification:** `./gradlew :app:compileGoogleDebugKotlin -Parm64` passed with 0 errors.

---

