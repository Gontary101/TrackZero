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
4. `fix(ui): increase typography scale for route card labels and start ride cta`
   * **Hash:** `9b06aa0`
   * **Files Modified:**
     * `android/app/src/main/res/values/trackzero_dimens.xml` — Added `trackzero_text_route_card_eyebrow` (14sp), `trackzero_text_route_card_metric_label` (13sp), and `trackzero_text_route_card_cta` (15sp).
     * `android/app/src/main/res/layout/trackzero_card_featured_route.xml` — Scaled up text sizes for Eyebrow, Distance/Elevation labels, and Start Ride action text.
   * **Verification:** `./gradlew :app:compileGoogleDebugKotlin -Parm64` passed with 0 errors.
5. `Merge task 'fix/ui-route-card-typography' into ws/ui` (Merge commit: `16455e1`)

### Task: `feat/ui-routes-screen`
* **Parent Branch:** `ws/ui`
* **Objective:** Implement TrackZero Routes browse screen, filter capsule pill bar (Local, Favorites, Downloaded), route item list cards with silhouette thumbnails, and TrackZeroRoutesAdapter.
* **Scope Firewall:** Contained UI browse layout, RecyclerView adapter, and vector drawables.

#### Commits:
1. `feat(ui): add drawables and tokens for routes browse screen`
   * **Hash:** `63dd7ba`
   * **Files Added:**
     * `android/app/src/main/res/drawable/trackzero_bg_routes_filter_bar.xml` — Stadium capsule pill container with 1.5dp mint border.
     * `android/app/src/main/res/drawable/trackzero_bg_routes_filter_pill_selected.xml` — Solid mint pill for active filter tab.
     * `android/app/src/main/res/drawable/trackzero_bg_route_list_card.xml` — 24dp rounded rectangle card with 1.5dp mint border.
     * `android/app/src/main/res/drawable/trackzero_bg_circle_chevron.xml` — 42dp circular button background.
     * `android/app/src/main/res/drawable/trackzero_ic_chevron_right.xml` — Crisp right chevron vector icon.
   * **Files Modified:**
     * `android/app/src/main/res/values/trackzero_dimens.xml` — Route list card height (102dp), thumbnail width (96dp), chevron size (42dp), typography scale.
     * `android/app/src/main/res/values/trackzero_strings.xml` — Filter tab strings (Local, Favorites, Downloaded) and ROUTES header title.
2. `feat(ui): add route list card layout and route item data model`
   * **Hash:** `cc062c0`
   * **Files Added:**
     * `android/app/src/main/res/drawable/trackzero_ic_route_silhouette_1.xml` — Sample route silhouette vector.
     * `android/app/src/main/res/layout/trackzero_item_route_card.xml` — 102dp route list card layout with thumbnail, divider, route title, Distance & Elevation stats with units/labels, and circular chevron affordance.
     * `android/app/src/main/java/app/organicmaps/trackzero/data/TrackZeroRouteItem.kt` — Data class representing cycling route item with distance, elevation, and flags.
3. `feat(ui): add routes browse screen layout and TrackZeroRoutesAdapter`
   * **Hash:** `620604b`
   * **Files Added:**
     * `android/app/src/main/res/layout/trackzero_fragment_routes.xml` — Complete Routes screen layout with header, filter bar, RecyclerView, and bottom island.
     * `android/app/src/main/java/app/organicmaps/trackzero/ui/TrackZeroRoutesAdapter.kt` — RecyclerView ListAdapter binding route items with DiffCallback.
4. `feat(ui): add dot-matrix ROUTES wordmark vector drawable`
   * **Hash:** `f5407db`
   * **Files Added:**
     * `android/app/src/main/res/drawable/trackzero_wordmark_routes.xml` — 122-dot vector wordmark matching the canonical dot-matrix identity.
   * **Files Modified:**
     * `android/app/src/main/res/layout/trackzero_fragment_routes.xml` — Header uses dot-matrix wordmark vector drawable.
   * **Verification:** `./gradlew :app:compileGoogleDebugKotlin -Parm64` passed with 0 errors. Side-by-side comparison verified against `routes_display.png`.
5. `Merge task 'feat/ui-routes-screen' into ws/ui` (Merge commit: `d993c55`)

### Task: `cleanup/ui-dead-code`
* **Parent Branch:** `ws/ui`
* **Objective:** Audit and eliminate all dead code, unused resource tokens, phantom view IDs, and unreferenced fields introduced across previous UI tasks.
* **Scope Firewall:** Zero modifications to existing UI appearance, behavior, or layouts; only removing unreferenced and dead code.

#### Commits:
1. `refactor(ui): remove unused tokens, dead fields, and invalid constraints`
   * **Hash:** `a9ba3b2`
   * **Files Modified:**
     * `android/app/src/main/res/values/trackzero_colors.xml` — Removed 7 unused speculative colors (`trackzero_surface_black`, `trackzero_deep_surface`, `trackzero_secondary_text`, `trackzero_mint_dim`, `trackzero_mint_translucent_10`, `trackzero_mint_translucent_20`, `trackzero_scrim_black_60`).
     * `android/app/src/main/res/values/trackzero_dimens.xml` — Removed 13 unused dimension tokens (`trackzero_space_base`, `trackzero_radius_maneuver_card`, `trackzero_radius_chip`, `trackzero_radius_circle`, `trackzero_touch_target_min`, `trackzero_text_hero_numeric`, `trackzero_text_hero_turn`, `trackzero_text_display_title`, `trackzero_text_card_title`, `trackzero_text_metric_large`, `trackzero_text_label`, `trackzero_text_meta`, `trackzero_text_routes_title`).
     * `android/app/src/main/res/layout/trackzero_fragment_routes.xml` — Fixed dangling constraint reference pointing to nonexistent ID `tv_routes_header_title` to correctly reference `iv_routes_header_title`.
     * `android/app/src/main/java/app/organicmaps/trackzero/ui/TrackZeroRouteCard.kt` — Removed unused private fields `tvDistanceUnit` and `tvElevationUnit`.
     * `android/app/src/main/java/app/organicmaps/trackzero/ui/TrackZeroBottomIsland.kt` — Formatted with official Android ktlint configuration.
     * `android/app/src/main/java/app/organicmaps/trackzero/ui/TrackZeroRoutesAdapter.kt` — Formatted with official Android ktlint configuration.
     * `android/app/src/main/java/app/organicmaps/trackzero/data/TrackZeroRouteItem.kt` — Formatted with official Android ktlint configuration.
   * **Verification:** Zero unused tokens remaining (verified via AST scan: 6 colors, 34 dimens, 14 strings, 17 drawables all active with 0 unused). `ktlintCheck`, `detektCheck`, Kotlin compilation, and unit tests all passed cleanly (`BUILD SUCCESSFUL`).
2. `Merge task 'cleanup/ui-dead-code' into ws/ui` (Merge commit: `127541b`)

### Integration: `int/ui-map-overlay`
* **Parent Branch:** `ws/ui`
* **Objective:** Mount TrackZero UI shell over Organic Maps MapView with clean ownership boundaries, thin map action bridges, and deterministic state rendering.

#### Task: `feat/ui-map-overlay-host`
* **Parent Branch:** `int/ui-map-overlay`
* **Objective:** Mount TrackZero map overlay host into Organic Maps map button hierarchy and replace visible right-side map controls while preserving native map behavior.
* **Scope Firewall:** No route-card animations, no fragment navigation, no route start, no map-drag collapse, minimal overlay host hook only.

##### Commits:
1. `feat(ui): mount TrackZero map overlay host and wire native bridge`
   * **Hash:** `c85636d`
   * **Files Added:**
     * `android/app/src/main/res/layout/trackzero_map_overlay.xml` — TrackZero overlay container hosting circular map controls, route card (hidden by default), and bottom island.
     * `android/app/src/main/java/app/organicmaps/trackzero/ui/MapOverlayState.kt` — Explicit sealed state model (`Browsing`, `RoutePreview`, `MapInteracting`).
     * `android/app/src/main/java/app/organicmaps/trackzero/ui/TrackZeroMapOverlayController.kt` — Presentation controller binding TrackZero controls to native Organic Maps action bridge.
   * **Files Modified:**
     * `android/app/src/main/res/layout/map_buttons_layout_regular.xml` — Mounted TrackZero overlay host and set superseded legacy buttons to `gone`.
     * `android/app/src/main/java/app/organicmaps/maplayer/MapButtonsController.java` — Thin native action bridge forwarding zoom and location events to `mMapButtonClickListener`.
   * **Verification:** `./gradlew app:ktlintCheck -Parm64`, Kotlin/Java compilation, and unit tests all passed cleanly (`BUILD SUCCESSFUL`).
2. `Merge task 'feat/ui-map-overlay-host' into int/ui-map-overlay` (Merge commit: `06bb971`)

#### Task: `feat/ui-map-controls`
* **Parent Branch:** `int/ui-map-overlay`
* **Objective:** Encapsulate TrackZero map controls into TrackZeroMapControls, wire GPS location state updates (follow, rotate, pending, not follow) to the circular location button, and apply safe vertical constraints to avoid collision on all screen aspect ratios.
* **Scope Firewall:** Contained controls component and presentation updates, zero modifications to native Organic Maps GPS logic.

##### Commits:
1. `feat(ui): encapsulate map controls and wire dynamic location state updates`
   * **Hash:** `7a84eef`
   * **Files Added:**
     * `android/app/src/main/java/app/organicmaps/trackzero/ui/TrackZeroMapControls.kt` — Controller managing circular map controls and GPS mode icon/tint updates.
   * **Files Modified:**
     * `android/app/src/main/res/layout/trackzero_map_controls.xml` — Added `iv_trackzero_my_location` ID for dynamic icon updates.
     * `android/app/src/main/res/layout/trackzero_map_overlay.xml` — Added safe top constraint and vertical bias to map controls include.
     * `android/app/src/main/java/app/organicmaps/trackzero/ui/TrackZeroMapOverlayController.kt` — Integrated `TrackZeroMapControls` and exposed `updateMyPositionMode()`.
     * `android/app/src/main/java/app/organicmaps/maplayer/MapButtonsController.java` — Forwarded `updateNavMyPositionButton` to `mTrackZeroOverlayController`.
   * **Verification:** `./gradlew app:ktlintCheck -Parm64`, Kotlin/Java compilation, and unit tests all passed cleanly (`BUILD SUCCESSFUL`).
2. `Merge task 'feat/ui-map-controls' into int/ui-map-overlay` (Merge commit: `e159b85`)

#### Task: `feat/ui-map-insets`
* **Parent Branch:** `int/ui-map-overlay`
* **Objective:** Refine bottom navigation island geometry by eliminating redundant internal margins and enforcing explicit 76dp height, preventing double-inset/double-margin displacement over Organic Maps MapView.
* **Scope Firewall:** Layout dimensions and insets refinement only, zero functional logic changes.

##### Commits:
1. `feat(ui): refine bottom island safe-area geometry and eliminate redundant margins`
   * **Hash:** `a97ffb1`
   * **Files Modified:**
     * `android/app/src/main/res/layout/trackzero_bottom_island.xml` — Removed internal margins (`layout_marginStart`, `layout_marginEnd`, `layout_marginBottom`) from root LinearLayout.
     * `android/app/src/main/res/layout/trackzero_map_overlay.xml` — Enforced explicit `layout_height="@dimen/trackzero_bottom_island_height"` with outer margins.
     * `android/app/src/main/res/layout/trackzero_fragment_routes.xml` — Enforced explicit `layout_height="@dimen/trackzero_bottom_island_height"` with outer margins.
   * **Verification:** `./gradlew app:ktlintCheck -Parm64`, Kotlin/Java compilation, and unit tests all passed cleanly (`BUILD SUCCESSFUL`).
2. `Merge task 'feat/ui-map-insets' into int/ui-map-overlay` (Merge commit: `68656de`)

---

