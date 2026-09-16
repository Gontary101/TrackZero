package app.organicmaps.trackzero.ui

import android.view.View
import app.organicmaps.R

/**
 * Controller managing presentation and user interactions for the TrackZero map overlay.
 *
 * Adheres strictly to the architectural boundary:
 * - Organic Maps owns core map behavior and routing calculations.
 * - TrackZero owns all visual overlay presentation, states, and design systems.
 */
class TrackZeroMapOverlayController(private val root: View, private val mapActions: MapActionsBridge) {
    /**
     * Thin bridge interface to invoke native Organic Maps actions.
     */
    interface MapActionsBridge {
        fun zoomIn()
        fun zoomOut()
        fun myPosition()
    }

    val mapControls: View? = root.findViewById(R.id.trackzero_map_controls)
    val routeCard: TrackZeroRouteCard? = root.findViewById(R.id.trackzero_route_card)
    val bottomIsland: TrackZeroBottomIsland? = root.findViewById(R.id.trackzero_bottom_island)

    var state: MapOverlayState = MapOverlayState.Browsing
        private set

    init {
        // Wire circular map controls to native Organic Maps bridge
        root.findViewById<View>(R.id.btn_trackzero_zoom_in)?.setOnClickListener {
            mapActions.zoomIn()
        }
        root.findViewById<View>(R.id.btn_trackzero_zoom_out)?.setOnClickListener {
            mapActions.zoomOut()
        }
        root.findViewById<View>(R.id.btn_trackzero_my_location)?.setOnClickListener {
            mapActions.myPosition()
        }

        renderState(state)
    }

    /**
     * Updates the overlay presentation to the specified state.
     */
    fun setState(newState: MapOverlayState) {
        state = newState
        renderState(newState)
    }

    private fun renderState(state: MapOverlayState) {
        when (state) {
            is MapOverlayState.Browsing -> {
                routeCard?.visibility = View.GONE
            }

            is MapOverlayState.RoutePreview -> {
                routeCard?.setRouteInfo(
                    title = state.route.title,
                    distanceKm = state.route.distanceKm,
                    elevationM = state.route.elevationGainM,
                )
                routeCard?.visibility = View.VISIBLE
            }

            is MapOverlayState.MapInteracting -> {
                // To be enhanced in subsequent task (feat/ui-map-route-card-hook)
            }
        }
    }
}
