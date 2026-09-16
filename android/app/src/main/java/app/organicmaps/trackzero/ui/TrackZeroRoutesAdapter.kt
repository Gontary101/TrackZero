package app.organicmaps.trackzero.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.organicmaps.R
import app.organicmaps.trackzero.data.TrackZeroRouteItem
import java.util.Locale

/**
 * RecyclerView Adapter for displaying cycling route cards in the TrackZero Routes screen.
 */
class TrackZeroRoutesAdapter(private val onRouteClickListener: (TrackZeroRouteItem) -> Unit) :
    ListAdapter<TrackZeroRouteItem, TrackZeroRoutesAdapter.RouteViewHolder>(RouteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.trackzero_item_route_card, parent, false)
        return RouteViewHolder(view, onRouteClickListener)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RouteViewHolder(itemView: View, private val onRouteClickListener: (TrackZeroRouteItem) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val ivThumbnail: ImageView = itemView.findViewById(R.id.iv_route_thumbnail)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_route_title)
        private val tvDistanceValue: TextView = itemView.findViewById(R.id.tv_distance_value)
        private val tvElevationValue: TextView = itemView.findViewById(R.id.tv_elevation_value)
        private val btnChevron: View = itemView.findViewById(R.id.btn_route_chevron)

        fun bind(item: TrackZeroRouteItem) {
            tvTitle.text = item.title
            tvDistanceValue.text = String.format(Locale.US, "%.1f", item.distanceKm)
            tvElevationValue.text = String.format(Locale.US, "%,d", item.elevationGainM)

            if (item.thumbnailResId != null) {
                ivThumbnail.setImageResource(item.thumbnailResId)
            } else {
                ivThumbnail.setImageResource(R.drawable.trackzero_ic_route_silhouette_1)
            }

            itemView.setOnClickListener { onRouteClickListener(item) }
            btnChevron.setOnClickListener { onRouteClickListener(item) }
        }
    }

    private class RouteDiffCallback : DiffUtil.ItemCallback<TrackZeroRouteItem>() {
        override fun areItemsTheSame(oldItem: TrackZeroRouteItem, newItem: TrackZeroRouteItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TrackZeroRouteItem, newItem: TrackZeroRouteItem): Boolean =
            oldItem == newItem
    }
}
