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
)
