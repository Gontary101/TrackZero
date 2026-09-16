package app.organicmaps.trackzero.ui

import android.content.res.ColorStateList
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import app.organicmaps.R
import app.organicmaps.sdk.location.LocationState

/**
 * Controller managing the TrackZero circular floating map controls
 * (Zoom In, Zoom Out, My Location).
 */
class TrackZeroMapControls(val root: View) {
    val btnZoomIn: View = root.findViewById(R.id.btn_trackzero_zoom_in)
    val btnZoomOut: View = root.findViewById(R.id.btn_trackzero_zoom_out)
    val btnMyLocation: View = root.findViewById(R.id.btn_trackzero_my_location)
    val ivMyLocation: ImageView = root.findViewById(R.id.iv_trackzero_my_location)

    private val colorWhite: Int = ContextCompat.getColor(root.context, R.color.trackzero_primary_white)
    private val colorMint: Int = ContextCompat.getColor(root.context, R.color.trackzero_mint_accent)
    private val colorMuted: Int = ContextCompat.getColor(root.context, R.color.trackzero_muted_text)

    /**
     * Updates the location button icon and tint according to the Organic Maps LocationState mode.
     */
    fun updateLocationMode(mode: Int) {
        when (mode) {
            LocationState.PENDING_POSITION -> {
                ivMyLocation.setImageResource(R.drawable.ic_menu_location_pending)
                ImageViewCompat.setImageTintList(ivMyLocation, ColorStateList.valueOf(colorMint))
                val rotate = RotateAnimation(
                    0f,
                    360f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                ).apply {
                    duration = 1000
                    repeatCount = Animation.INFINITE
                    interpolator = LinearInterpolator()
                }
                ivMyLocation.startAnimation(rotate)
            }

            LocationState.FOLLOW -> {
                ivMyLocation.clearAnimation()
                ivMyLocation.setImageResource(R.drawable.ic_follow)
                ImageViewCompat.setImageTintList(ivMyLocation, ColorStateList.valueOf(colorMint))
            }

            LocationState.FOLLOW_AND_ROTATE -> {
                ivMyLocation.clearAnimation()
                ivMyLocation.setImageResource(R.drawable.ic_follow_and_rotate)
                ImageViewCompat.setImageTintList(ivMyLocation, ColorStateList.valueOf(colorMint))
            }

            LocationState.NOT_FOLLOW_NO_POSITION -> {
                ivMyLocation.clearAnimation()
                ivMyLocation.setImageResource(R.drawable.ic_location_off)
                ImageViewCompat.setImageTintList(ivMyLocation, ColorStateList.valueOf(colorMuted))
            }

            else -> {
                ivMyLocation.clearAnimation()
                ivMyLocation.setImageResource(R.drawable.ic_location_crosshair)
                ImageViewCompat.setImageTintList(ivMyLocation, ColorStateList.valueOf(colorWhite))
            }
        }
    }
}
