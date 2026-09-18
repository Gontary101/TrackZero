package app.organicmaps.trackzero.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import app.organicmaps.R
import java.util.Locale

/**
 * TrackZero Featured Route / Ride Launch Card View.
 *
 * Implements the prominent route preview and start ride card matching
 * the TrackZero Brand and Visual Identity System v2.0.
 */
class TrackZeroRouteCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    fun interface OnStartRideClickListener {
        fun onStartRideClick()
    }

    var onStartRideClickListener: OnStartRideClickListener? = null

    private val tvEyebrow: TextView
    private val tvTitle: TextView
    private val tvDistanceValue: TextView
    private val tvElevationValue: TextView
    private val btnStartRide: View

    init {
        isClickable = true
        isFocusable = true
        LayoutInflater.from(context).inflate(R.layout.trackzero_card_featured_route, this, true)

        tvEyebrow = findViewById(R.id.tv_route_eyebrow)
        tvTitle = findViewById(R.id.tv_route_title)
        tvDistanceValue = findViewById(R.id.tv_distance_value)
        tvElevationValue = findViewById(R.id.tv_elevation_value)
        btnStartRide = findViewById(R.id.btn_start_ride)

        btnStartRide.setOnClickListener {
            onStartRideClickListener?.onStartRideClick()
        }
    }

    /**
     * Binds the route information to the card.
     *
     * @param title The route name (e.g. "Puy de Dôme Loop")
     * @param distanceKm The distance in kilometers (e.g. 78.4)
     * @param elevationM The elevation gain in meters (e.g. 1420)
     * @param eyebrow Optional custom eyebrow text (defaults to "FEATURED ROUTE")
     */
    fun setRouteInfo(title: String, distanceKm: Double, elevationM: Int, eyebrow: String? = null) {
        tvTitle.text = title
        tvDistanceValue.text = String.format(Locale.US, "%.1f", distanceKm)
        tvElevationValue.text = String.format(Locale.US, "%,d", elevationM)

        if (eyebrow != null) {
            tvEyebrow.text = eyebrow
        }
    }
}
