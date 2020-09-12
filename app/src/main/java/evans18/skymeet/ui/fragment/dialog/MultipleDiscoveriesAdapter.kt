package evans18.skymeet.ui.fragment.dialog

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import evans18.skymeet.R
import evans18.skymeet.data.model.entities.Flight
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_multiple_discovered_flights.*
import org.jetbrains.anko.find


class MultipleDiscoveriesAdapter(var data: List<Flight> = emptyList()) :
    RecyclerView.Adapter<MultipleDiscoveriesAdapter.ViewHolder>() {

    lateinit var onDiscoverySpecifiedListener: OnDiscoverySpecifiedListener

    interface OnDiscoverySpecifiedListener {
        fun onSpecify(flight: Flight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.item_multiple_discovered_flights, null)
            .apply {
                //set image  filter to grayscale
                find<ImageView>(R.id.ic_flight).apply {
                    colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
                        setSaturation(0f)
                    })
                }
            })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flight = data[position]
        holder.name_company_and_aircraft_tail_number.text = holder.containerView.resources.getString(
            R.string.tracked_aircraft_text_template,
            flight.operatedBy.name,
            flight.aircraft.tailSign
        )
        holder.containerView.setOnClickListener {
            onDiscoverySpecifiedListener.onSpecify(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer

}