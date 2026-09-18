package app.organicmaps.trackzero.data

import androidx.annotation.DrawableRes

/**
 * Data model representing a cycling route item displayed in the TrackZero Routes screen.
 */
data class TrackZeroRouteItem(
    val id: String,
    val title: String,
    val distanceKm: Double,
    val elevationGainM: Int,
    @get:DrawableRes val thumbnailResId: Int? = null,
    val isFavorite: Boolean = false,
    val isDownloaded: Boolean = false,
    val trackId: Long = -1L,
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val zoom: Int = 0,
    val gpxAssetPath: String? = null,
)
