package app.organicmaps.trackzero.ui

import app.organicmaps.trackzero.data.TrackZeroRouteItem

/**
 * Explicit state model for the TrackZero map overlay.
 *
 * All overlay presentation (route card visibility, map control positioning,
 * and interaction states) is rendered deterministically from this state.
 */
sealed interface MapOverlayState {
    data object Browsing : MapOverlayState

    data class RoutePreview(val route: TrackZeroRouteItem) : MapOverlayState

    data class MapInteracting(val previous: MapOverlayState) : MapOverlayState
}
