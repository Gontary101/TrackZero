package app.organicmaps.trackzero.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import app.organicmaps.MwmActivity
import app.organicmaps.R
import app.organicmaps.maplayer.MapButtonsController
import app.organicmaps.trackzero.data.TrackZeroRouteItem
import app.organicmaps.util.WindowInsetUtils

/**
 * TrackZero Routes Browse Screen Fragment.
 *
 * Implements full-screen browsing of curated cycling routes with category filter pills,
 * route list presentation, and bottom island tab navigation.
 */
class TrackZeroRoutesFragment : Fragment() {

    enum class RouteFilter {
        LOCAL,
        FAVORITES,
        DOWNLOADED,
    }

    private var currentFilter: RouteFilter = RouteFilter.LOCAL
    private lateinit var adapter: TrackZeroRoutesAdapter
    private lateinit var rvRoutes: RecyclerView
    private lateinit var tabFilterLocal: TextView
    private lateinit var tabFilterFavorites: TextView
    private lateinit var tabFilterDownloaded: TextView
    private lateinit var bottomIsland: TrackZeroBottomIsland

    private val allRoutes = listOf(
        TrackZeroRouteItem(
            id = "route-1",
            title = "Puy de Dôme Loop",
            distanceKm = 78.4,
            elevationGainM = 1420,
            thumbnailResId = R.drawable.trackzero_ic_route_silhouette_1,
            isFavorite = true,
            isDownloaded = true,
        ),
        TrackZeroRouteItem(
            id = "route-2",
            title = "Col du Galibier Climb",
            distanceKm = 42.6,
            elevationGainM = 2150,
            thumbnailResId = R.drawable.trackzero_ic_route_silhouette_1,
            isFavorite = true,
            isDownloaded = false,
        ),
        TrackZeroRouteItem(
            id = "route-3",
            title = "Mont Ventoux Challenge",
            distanceKm = 56.2,
            elevationGainM = 1980,
            thumbnailResId = R.drawable.trackzero_ic_route_silhouette_1,
            isFavorite = false,
            isDownloaded = true,
        ),
        TrackZeroRouteItem(
            id = "route-4",
            title = "Lake Annecy Circuit",
            distanceKm = 38.0,
            elevationGainM = 450,
            thumbnailResId = R.drawable.trackzero_ic_route_silhouette_1,
            isFavorite = false,
            isDownloaded = false,
        ),
        TrackZeroRouteItem(
            id = "route-5",
            title = "Côte de la Redoute",
            distanceKm = 64.5,
            elevationGainM = 920,
            thumbnailResId = R.drawable.trackzero_ic_route_silhouette_1,
            isFavorite = true,
            isDownloaded = true,
        ),
        TrackZeroRouteItem(
            id = "route-6",
            title = "Alpe d'Huez Ascent",
            distanceKm = 32.1,
            elevationGainM = 1180,
            thumbnailResId = R.drawable.trackzero_ic_route_silhouette_1,
            isFavorite = false,
            isDownloaded = true,
        ),
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.trackzero_fragment_routes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Safe area insets: padding across all sides
        ViewCompat.setOnApplyWindowInsetsListener(
            view,
            WindowInsetUtils.PaddingInsetsListener.allSides(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout(),
            ),
        )

        tabFilterLocal = view.findViewById(R.id.tab_filter_local)
        tabFilterFavorites = view.findViewById(R.id.tab_filter_favorites)
        tabFilterDownloaded = view.findViewById(R.id.tab_filter_downloaded)
        rvRoutes = view.findViewById(R.id.rv_routes_list)
        bottomIsland = view.findViewById(R.id.trackzero_bottom_island)

        adapter = TrackZeroRoutesAdapter { route ->
            (activity as? MwmActivity)?.onTrackZeroRouteSelected(route)
        }
        rvRoutes.adapter = adapter

        // Setup filter pills
        tabFilterLocal.setOnClickListener { setFilter(RouteFilter.LOCAL) }
        tabFilterFavorites.setOnClickListener { setFilter(RouteFilter.FAVORITES) }
        tabFilterDownloaded.setOnClickListener { setFilter(RouteFilter.DOWNLOADED) }

        // Setup bottom island with Routes active
        bottomIsland.selectTab(TrackZeroBottomIsland.Tab.ROUTES, notify = false)
        bottomIsland.listener = TrackZeroBottomIsland.OnTabSelectedListener { tab ->
            when (tab) {
                TrackZeroBottomIsland.Tab.MAP -> {
                    (activity as? MwmActivity)?.hideTrackZeroRoutes()
                }

                TrackZeroBottomIsland.Tab.ROUTES -> {
                    // Already on routes
                }

                TrackZeroBottomIsland.Tab.SEARCH -> {
                    (activity as? MwmActivity)?.let {
                        it.hideTrackZeroRoutes()
                        it.onMapButtonClick(MapButtonsController.MapButtons.search)
                    }
                }

                TrackZeroBottomIsland.Tab.MORE -> {
                    (activity as? MwmActivity)?.let {
                        it.hideTrackZeroRoutes()
                        it.onMapButtonClick(MapButtonsController.MapButtons.menu)
                    }
                }
            }
        }

        setFilter(RouteFilter.LOCAL)
    }

    fun setFilter(filter: RouteFilter) {
        currentFilter = filter
        updateFilterTabsUI()
        val filteredList = when (filter) {
            RouteFilter.LOCAL -> allRoutes
            RouteFilter.FAVORITES -> allRoutes.filter { it.isFavorite }
            RouteFilter.DOWNLOADED -> allRoutes.filter { it.isDownloaded }
        }
        adapter.submitList(filteredList)
    }

    private fun updateFilterTabsUI() {
        val selectedBg = R.drawable.trackzero_bg_routes_filter_pill_selected
        val unselectedColor = ContextCompat.getColor(requireContext(), R.color.trackzero_primary_white)
        val selectedColor = ContextCompat.getColor(requireContext(), R.color.trackzero_oled_black)

        tabFilterLocal.setBackgroundResource(if (currentFilter == RouteFilter.LOCAL) selectedBg else 0)
        tabFilterLocal.setTextColor(if (currentFilter == RouteFilter.LOCAL) selectedColor else unselectedColor)

        tabFilterFavorites.setBackgroundResource(if (currentFilter == RouteFilter.FAVORITES) selectedBg else 0)
        tabFilterFavorites.setTextColor(if (currentFilter == RouteFilter.FAVORITES) selectedColor else unselectedColor)

        tabFilterDownloaded.setBackgroundResource(if (currentFilter == RouteFilter.DOWNLOADED) selectedBg else 0)
        tabFilterDownloaded.setTextColor(
            if (currentFilter ==
                RouteFilter.DOWNLOADED
            ) {
                selectedColor
            } else {
                unselectedColor
            },
        )
    }

    companion object {
        const val TAG = "TrackZeroRoutesFragment"

        fun newInstance(): TrackZeroRoutesFragment = TrackZeroRoutesFragment()
    }
}
