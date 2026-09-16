package app.organicmaps.trackzero.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import app.organicmaps.R

/**
 * TrackZero Floating Bottom Island Navigation View.
 *
 * Implements the canonical 4-tab bottom navigation island (Map, Routes, Search, More)
 * matching the TrackZero Brand and Visual Identity System v2.0.
 */
class TrackZeroBottomIsland @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    enum class Tab {
        MAP,
        ROUTES,
        SEARCH,
        MORE,
    }

    fun interface OnTabSelectedListener {
        fun onTabSelected(tab: Tab)
    }

    var listener: OnTabSelectedListener? = null
    var currentTab: Tab = Tab.MAP
        private set

    private class TabViewHolder(val container: View, val icon: ImageView, val label: TextView, val dot: ImageView)

    private val tabHolders: Map<Tab, TabViewHolder>

    init {
        LayoutInflater.from(context).inflate(R.layout.trackzero_bottom_island, this, true)

        tabHolders = mapOf(
            Tab.MAP to TabViewHolder(
                container = findViewById(R.id.trackzero_tab_map),
                icon = findViewById(R.id.trackzero_tab_map_icon),
                label = findViewById(R.id.trackzero_tab_map_label),
                dot = findViewById(R.id.trackzero_tab_map_dot),
            ),
            Tab.ROUTES to TabViewHolder(
                container = findViewById(R.id.trackzero_tab_routes),
                icon = findViewById(R.id.trackzero_tab_routes_icon),
                label = findViewById(R.id.trackzero_tab_routes_label),
                dot = findViewById(R.id.trackzero_tab_routes_dot),
            ),
            Tab.SEARCH to TabViewHolder(
                container = findViewById(R.id.trackzero_tab_search),
                icon = findViewById(R.id.trackzero_tab_search_icon),
                label = findViewById(R.id.trackzero_tab_search_label),
                dot = findViewById(R.id.trackzero_tab_search_dot),
            ),
            Tab.MORE to TabViewHolder(
                container = findViewById(R.id.trackzero_tab_more),
                icon = findViewById(R.id.trackzero_tab_more_icon),
                label = findViewById(R.id.trackzero_tab_more_label),
                dot = findViewById(R.id.trackzero_tab_more_dot),
            ),
        )

        tabHolders.forEach { (tab, holder) ->
            holder.container.setOnClickListener {
                selectTab(tab, notify = true)
            }
        }

        // Default to MAP tab
        selectTab(Tab.MAP, notify = false)
    }

    /**
     * Selects the specified tab and updates active visual states.
     *
     * @param tab The tab to select.
     * @param notify Whether to notify the registered listener.
     */
    fun selectTab(tab: Tab, notify: Boolean = true) {
        currentTab = tab
        tabHolders.forEach { (t, holder) ->
            val isSelected = (t == tab)
            holder.container.isSelected = isSelected
            holder.icon.isSelected = isSelected
            holder.label.isSelected = isSelected
            holder.dot.visibility = if (isSelected) View.VISIBLE else View.GONE
        }
        if (notify) {
            listener?.onTabSelected(tab)
        }
    }
}
