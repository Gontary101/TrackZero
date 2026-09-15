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

---
