package app.organicmaps.trackzero.ui

import android.view.View
import app.organicmaps.sdk.MapView
import app.organicmaps.trackzero.data.TrackZeroRouteItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class TrackZeroMapOverlayControllerTest {

    private lateinit var mockRoot: View
    private lateinit var mockBridge: TrackZeroMapOverlayController.MapActionsBridge
    private lateinit var controller: TrackZeroMapOverlayController

    @Before
    fun setUp() {
        mockRoot = mock(View::class.java)
        mockBridge = mock(TrackZeroMapOverlayController.MapActionsBridge::class.java)
        controller = TrackZeroMapOverlayController(mockRoot, mockBridge)
    }

    @Test
    fun initialStateIsBrowsing() {
        assertEquals(MapOverlayState.Browsing, controller.state)
    }

    @Test
    fun showRouteCardTransitionsToRoutePreview() {
        val sampleRoute = TrackZeroRouteItem(
            id = "test-1",
            title = "Puy de Dôme Loop",
            distanceKm = 78.4,
            elevationGainM = 1420,
        )

        controller.showRouteCard(sampleRoute)

        val state = controller.state
        assertTrue(state is MapOverlayState.RoutePreview)
        assertEquals(sampleRoute, (state as MapOverlayState.RoutePreview).route)
    }

    @Test
    fun hideRouteCardTransitionsToBrowsing() {
        val sampleRoute = TrackZeroRouteItem(
            id = "test-1",
            title = "Puy de Dôme Loop",
            distanceKm = 78.4,
            elevationGainM = 1420,
        )

        controller.showRouteCard(sampleRoute)
        assertEquals(MapOverlayState.RoutePreview(sampleRoute), controller.state)

        controller.hideRouteCard()
        assertEquals(MapOverlayState.Browsing, controller.state)
    }

    @Test
    fun mapInteractionMinimizesRouteCardDuringPreview() {
        val sampleRoute = TrackZeroRouteItem(
            id = "test-1",
            title = "Puy de Dôme Loop",
            distanceKm = 78.4,
            elevationGainM = 1420,
        )

        controller.showRouteCard(sampleRoute)
        controller.onMapInteractionStarted()

        val state = controller.state
        assertTrue(state is MapOverlayState.MapInteracting)
        assertEquals(MapOverlayState.RoutePreview(sampleRoute), (state as MapOverlayState.MapInteracting).previous)

        controller.onMapInteractionEnded()
        assertEquals(MapOverlayState.RoutePreview(sampleRoute), controller.state)
    }

    @Test
    fun mapInteractionWhileBrowsingIsNoOp() {
        controller.onMapInteractionStarted()
        assertEquals(MapOverlayState.Browsing, controller.state)

        controller.onMapInteractionEnded()
        assertEquals(MapOverlayState.Browsing, controller.state)
    }

    @Test
    fun attachAndDetachMapView() {
        val mockMapView = mock(MapView::class.java)

        controller.attachMapView(mockMapView)
        controller.detachMapView()
    }

    @Test
    fun selectTabDelegatesToBottomIsland() {
        controller.selectTab(TrackZeroBottomIsland.Tab.MAP, notify = false)
        controller.selectTab(TrackZeroBottomIsland.Tab.ROUTES, notify = false)
    }
}
