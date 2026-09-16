package app.organicmaps.trackzero.ui

import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import app.organicmaps.R
import app.organicmaps.sdk.MapView
import app.organicmaps.trackzero.data.TrackZeroRouteItem
import kotlin.math.abs

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

    /**
     * Listener for route card actions (e.g. Start Ride).
     */
    fun interface OnRouteActionListener {
        fun onStartRide(route: TrackZeroRouteItem)
    }

    var onRouteActionListener: OnRouteActionListener? = null

    val controlsView: View? = root.findViewById(R.id.trackzero_map_controls)
    val mapControls: TrackZeroMapControls? = controlsView?.let { TrackZeroMapControls(it) }
    val routeCard: TrackZeroRouteCard? = root.findViewById(R.id.trackzero_route_card)
    val bottomIsland: TrackZeroBottomIsland? = root.findViewById(R.id.trackzero_bottom_island)

    var state: MapOverlayState = MapOverlayState.Browsing
        private set

    private var attachedMapView: MapView? = null
    private val touchSlop: Int = runCatching {
        ViewConfiguration.get(root.context).scaledTouchSlop
    }.getOrDefault(16)
    private var initialTouchX: Float = 0f
    private var initialTouchY: Float = 0f
    private var isDraggingMap: Boolean = false

    private val mapTouchEventListener = MapView.OnTouchEventListener { event ->
        handleMapTouch(event)
    }

    init {
        // Wire circular map controls to native Organic Maps bridge
        mapControls?.btnZoomIn?.setOnClickListener {
            mapActions.zoomIn()
        }
        mapControls?.btnZoomOut?.setOnClickListener {
            mapActions.zoomOut()
        }
        mapControls?.btnMyLocation?.setOnClickListener {
            mapActions.myPosition()
        }

        // Wire route card Start Ride button
        routeCard?.onStartRideClickListener = TrackZeroRouteCard.OnStartRideClickListener {
            val currentRoute = when (val currentState = state) {
                is MapOverlayState.RoutePreview -> currentState.route
                is MapOverlayState.MapInteracting -> (currentState.previous as? MapOverlayState.RoutePreview)?.route
                else -> null
            }
            if (currentRoute != null) {
                onRouteActionListener?.onStartRide(currentRoute)
            }
        }

        renderState(state)
    }

    /**
     * Attaches to MapView to observe gesture and pan motions for state transitions.
     */
    fun attachMapView(mapView: MapView) {
        if (attachedMapView === mapView) return
        detachMapView()
        attachedMapView = mapView
        mapView.addOnTouchEventListener(mapTouchEventListener)
    }

    /**
     * Detaches from MapView.
     */
    fun detachMapView() {
        attachedMapView?.removeOnTouchEventListener(mapTouchEventListener)
        attachedMapView = null
    }

    /**
     * Updates the location button mode according to Organic Maps LocationState.
     */
    fun updateMyPositionMode(mode: Int) {
        mapControls?.updateLocationMode(mode)
    }

    /**
     * Shows the featured route card with the given route item.
     */
    fun showRouteCard(route: TrackZeroRouteItem) {
        setState(MapOverlayState.RoutePreview(route))
    }

    /**
     * Hides the route card and returns to free browsing state.
     */
    fun hideRouteCard() {
        setState(MapOverlayState.Browsing)
    }

    /**
     * Invoked when user map interaction (drag, pan, pinch) begins.
     */
    fun onMapInteractionStarted() {
        val currentState = state
        if (currentState is MapOverlayState.RoutePreview) {
            setState(MapOverlayState.MapInteracting(currentState))
        }
    }

    /**
     * Invoked when user map interaction ends.
     */
    fun onMapInteractionEnded() {
        val currentState = state
        if (currentState is MapOverlayState.MapInteracting) {
            setState(currentState.previous)
        }
    }

    /**
     * Updates the overlay presentation to the specified state.
     */
    fun setState(newState: MapOverlayState) {
        state = newState
        renderState(newState)
    }

    private fun handleMapTouch(event: MotionEvent) {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                initialTouchX = event.x
                initialTouchY = event.y
                isDraggingMap = false
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                // Multi-finger gesture (pinch zoom, rotate) is always a map interaction
                if (!isDraggingMap) {
                    isDraggingMap = true
                    onMapInteractionStarted()
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (!isDraggingMap) {
                    val dx = abs(event.x - initialTouchX)
                    val dy = abs(event.y - initialTouchY)
                    if (dx >= touchSlop || dy >= touchSlop) {
                        isDraggingMap = true
                        onMapInteractionStarted()
                    }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isDraggingMap) {
                    isDraggingMap = false
                    onMapInteractionEnded()
                }
            }
        }
    }

    private fun renderState(state: MapOverlayState) {
        when (state) {
            is MapOverlayState.Browsing -> {
                routeCard?.animate()?.cancel()
                routeCard?.visibility = View.GONE
                routeCard?.alpha = 1f
                routeCard?.translationY = 0f
            }

            is MapOverlayState.RoutePreview -> {
                routeCard?.animate()?.cancel()
                routeCard?.setRouteInfo(
                    title = state.route.title,
                    distanceKm = state.route.distanceKm,
                    elevationM = state.route.elevationGainM,
                )
                routeCard?.visibility = View.VISIBLE
                routeCard?.isClickable = true
                routeCard?.animate()
                    ?.translationY(0f)
                    ?.alpha(1f)
                    ?.setDuration(200)
                    ?.start()
            }

            is MapOverlayState.MapInteracting -> {
                if (state.previous is MapOverlayState.RoutePreview) {
                    routeCard?.animate()?.cancel()
                    val slideDistance = (routeCard?.height?.takeIf { it > 0 } ?: 150).toFloat()
                    routeCard?.isClickable = false
                    routeCard?.animate()
                        ?.translationY(slideDistance)
                        ?.alpha(0f)
                        ?.setDuration(150)
                        ?.start()
                }
            }
        }
    }
}
