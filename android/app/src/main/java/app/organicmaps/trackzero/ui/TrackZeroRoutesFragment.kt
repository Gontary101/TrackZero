package app.organicmaps.trackzero.ui

import android.content.Context
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
import app.organicmaps.sdk.bookmarks.data.BookmarkManager
import app.organicmaps.sdk.util.log.Logger
import app.organicmaps.trackzero.data.TrackZeroRouteItem
import app.organicmaps.util.WindowInsetUtils
import java.io.File

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
            id = "route-clermont-71km",
            title = "Clermont-Ferrand Loop",
            distanceKm = 70.5,
            elevationGainM = 1491,
            thumbnailResId = R.drawable.trackzero_ic_route_clermont_71km,
            isFavorite = true,
            isDownloaded = true,
            trackId = 1L,
            lat = 45.7882,
            lon = 2.9632,
            zoom = 11,
            gpxAssetPath = "routes/clermont_71km.gpx",
        ),
        TrackZeroRouteItem(
            id = "route-puy-de-dome",
            title = "Puy de Dôme Summit Climb",
            distanceKm = 69.5,
            elevationGainM = 1520,
            thumbnailResId = R.drawable.trackzero_ic_route_puy_de_dome_70km,
            isFavorite = true,
            isDownloaded = true,
            trackId = 2L,
            lat = 45.7882,
            lon = 2.9632,
            zoom = 11,
            gpxAssetPath = "routes/puy_de_dome_70km.gpx",
        ),
        TrackZeroRouteItem(
            id = "route-lac-aydat-78km",
            title = "Lac d'Aydat Grand Circuit",
            distanceKm = 77.7,
            elevationGainM = 1786,
            thumbnailResId = R.drawable.trackzero_ic_route_lac_aydat_78km,
            isFavorite = true,
            isDownloaded = true,
            trackId = 3L,
            lat = 45.6993,
            lon = 3.0485,
            zoom = 11,
            gpxAssetPath = "routes/lac_aydat_78km.gpx",
        ),
        TrackZeroRouteItem(
            id = "route-circuit-volcans-67km",
            title = "Circuit des Volcans d'Auvergne",
            distanceKm = 67.4,
            elevationGainM = 1350,
            thumbnailResId = R.drawable.trackzero_ic_route_circuit_volcans_67km,
            isFavorite = true,
            isDownloaded = true,
            trackId = 4L,
            lat = 45.7884,
            lon = 2.9633,
            zoom = 11,
            gpxAssetPath = "routes/circuit_volcans_67km.gpx",
        ),
        TrackZeroRouteItem(
            id = "route-gorges-aydat-65km",
            title = "Gorges & Lac d'Aydat",
            distanceKm = 64.5,
            elevationGainM = 1452,
            thumbnailResId = R.drawable.trackzero_ic_route_route_aydat_65km,
            isFavorite = false,
            isDownloaded = true,
            trackId = 5L,
            lat = 45.7090,
            lon = 3.0504,
            zoom = 11,
            gpxAssetPath = "routes/route_aydat_65km.gpx",
        ),
        TrackZeroRouteItem(
            id = "route-saint-nectaire-52km",
            title = "Clermont – Saint-Nectaire",
            distanceKm = 52.5,
            elevationGainM = 890,
            thumbnailResId = R.drawable.trackzero_ic_route_saint_nectaire_52km,
            isFavorite = false,
            isDownloaded = true,
            trackId = 6L,
            lat = 45.6836,
            lon = 3.0381,
            zoom = 11,
            gpxAssetPath = "routes/saint_nectaire_52km.gpx",
        ),
        TrackZeroRouteItem(
            id = "route-limagne-50km",
            title = "Plaine de la Limagne — Est",
            distanceKm = 49.8,
            elevationGainM = 320,
            thumbnailResId = R.drawable.trackzero_ic_route_clermont_50km,
            isFavorite = false,
            isDownloaded = true,
            trackId = 7L,
            lat = 45.7530,
            lon = 3.1738,
            zoom = 11,
            gpxAssetPath = "routes/clermont_50km.gpx",
        ),
        TrackZeroRouteItem(
            id = "route-chateaux-44km",
            title = "Boucle des Châteaux & Limagne",
            distanceKm = 44.2,
            elevationGainM = 339,
            thumbnailResId = R.drawable.trackzero_ic_route_clermont_44km,
            isFavorite = false,
            isDownloaded = true,
            trackId = 8L,
            lat = 45.7411,
            lon = 3.1740,
            zoom = 11,
            gpxAssetPath = "routes/clermont_44km.gpx",
        ),
        TrackZeroRouteItem(
            id = "route-volcans-domicile-42km",
            title = "Boucle des Volcans — Domicile",
            distanceKm = 42.7,
            elevationGainM = 1404,
            thumbnailResId = R.drawable.trackzero_ic_route_circuit_volcans_42km,
            isFavorite = false,
            isDownloaded = true,
            trackId = 9L,
            lat = 45.7924,
            lon = 2.9738,
            zoom = 11,
            gpxAssetPath = "routes/circuit_volcans_42km.gpx",
        ),
        TrackZeroRouteItem(
            id = "route-ceyssat-durtol-30km",
            title = "Col de Ceyssat & Durtol",
            distanceKm = 30.0,
            elevationGainM = 744,
            thumbnailResId = R.drawable.trackzero_ic_route_ceyssat_durtol_30km,
            isFavorite = true,
            isDownloaded = true,
            trackId = 10L,
            lat = 45.7988,
            lon = 3.0460,
            zoom = 12,
            gpxAssetPath = "routes/ceyssat_durtol_30km.gpx",
        ),
        TrackZeroRouteItem(
            id = "route-ceyrat-plateau-28km",
            title = "Vallée du Ceyrat & Plateau",
            distanceKm = 28.2,
            elevationGainM = 296,
            thumbnailResId = R.drawable.trackzero_ic_route_clermont_28km,
            isFavorite = false,
            isDownloaded = true,
            trackId = 11L,
            lat = 45.7519,
            lon = 3.1601,
            zoom = 12,
            gpxAssetPath = "routes/clermont_28km.gpx",
        ),
        TrackZeroRouteItem(
            id = "route-plateaux-beaumont-24km",
            title = "Tour des Plateaux de Beaumont",
            distanceKm = 24.0,
            elevationGainM = 380,
            thumbnailResId = R.drawable.trackzero_ic_route_plateaux_beaumont,
            isFavorite = false,
            isDownloaded = true,
            trackId = 12L,
            lat = 45.7340,
            lon = 3.0871,
            zoom = 12,
            gpxAssetPath = "routes/plateaux_beaumont.gpx",
        ),
        TrackZeroRouteItem(
            id = "route-cotes-chamalieres-16km",
            title = "Côtes de Clermont & Chamalières",
            distanceKm = 16.4,
            elevationGainM = 421,
            thumbnailResId = R.drawable.trackzero_ic_route_clermont_16km,
            isFavorite = false,
            isDownloaded = true,
            trackId = 13L,
            lat = 45.7967,
            lon = 3.0831,
            zoom = 13,
            gpxAssetPath = "routes/clermont_16km.gpx",
        ),
        TrackZeroRouteItem(
            id = "route-aubiere-sprint-6km",
            title = "Aubière City Sprint",
            distanceKm = 6.6,
            elevationGainM = 53,
            thumbnailResId = R.drawable.trackzero_ic_route_aubiere_clermont,
            isFavorite = false,
            isDownloaded = true,
            trackId = 14L,
            lat = 45.7690,
            lon = 3.1123,
            zoom = 13,
            gpxAssetPath = "routes/aubiere_clermont.gpx",
        ),
        TrackZeroRouteItem(
            id = "route-clermont-aubiere-3km",
            title = "Clermont – Aubière Liaison",
            distanceKm = 3.2,
            elevationGainM = 66,
            thumbnailResId = R.drawable.trackzero_ic_route_clermont_aubiere,
            isFavorite = false,
            isDownloaded = true,
            trackId = 15L,
            lat = 45.7717,
            lon = 3.1040,
            zoom = 14,
            gpxAssetPath = "routes/clermont_aubiere.gpx",
        ),
        TrackZeroRouteItem(
            id = "route-marseille-epic-489km",
            title = "Clermont to Marseille Grand Tour",
            distanceKm = 488.8,
            elevationGainM = 5493,
            thumbnailResId = R.drawable.trackzero_ic_route_clermont_marseille_489km,
            isFavorite = true,
            isDownloaded = true,
            trackId = 16L,
            lat = 44.5399,
            lon = 4.2331,
            zoom = 8,
            gpxAssetPath = "routes/clermont_marseille_489km.gpx",
        ),
        TrackZeroRouteItem(
            id = "route-gtmc-gravel-1437km",
            title = "Grande Traversée Massif Central",
            distanceKm = 1436.8,
            elevationGainM = 30512,
            thumbnailResId = R.drawable.trackzero_ic_route_gtmc_gravel_1437km,
            isFavorite = true,
            isDownloaded = true,
            trackId = 17L,
            lat = 45.3812,
            lon = 3.5368,
            zoom = 7,
            gpxAssetPath = "routes/gtmc_gravel_1437km.gpx",
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

        ensureInitialRouteImported()
        setFilter(RouteFilter.LOCAL)
    }

    private fun ensureInitialRouteImported() {
        val ctx = context ?: return
        if (allRoutes.isNotEmpty()) {
            ensureGpxRouteImported(ctx, allRoutes.first())
        }
    }

    fun setFilter(filter: RouteFilter) {
        currentFilter = filter
        updateFilterTabsUI()
        val filteredList = when (filter) {
            RouteFilter.LOCAL -> allRoutes.filter { it.distanceKm < 100.0 }
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

        @JvmStatic
        fun ensureGpxRouteImported(context: Context, route: TrackZeroRouteItem) {
            val assetPath = route.gpxAssetPath ?: return
            val prefs = context.getSharedPreferences("trackzero_prefs", Context.MODE_PRIVATE)
            val key = "gpx_imported_" + route.id
            if (!prefs.getBoolean(key, false)) {
                try {
                    context.assets.open(assetPath).use { input ->
                        val cacheFile = File(context.cacheDir, route.id + ".gpx")
                        cacheFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                        BookmarkManager.INSTANCE.loadBookmarksFile(cacheFile.absolutePath, false)
                        BookmarkManager.INSTANCE.setAllCategoriesVisibility(true)
                        prefs.edit().putBoolean(key, true).apply()
                    }
                } catch (e: Exception) {
                    Logger.e(TAG, "Failed to import bundled GPX route: $assetPath", e)
                }
            }
        }
    }
}
