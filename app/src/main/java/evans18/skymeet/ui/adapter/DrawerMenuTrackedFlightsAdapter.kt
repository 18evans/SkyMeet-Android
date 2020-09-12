package evans18.skymeet.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import evans18.skymeet.R
import evans18.skymeet.data.model.entities.Flight
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.tracked_airplane_item.*

class DrawerMenuTrackedFlightsAdapter(
    var data: List<Flight>, //todo DiffUtil
    private val header: String
) :
    RecyclerView.Adapter<DrawerMenuTrackedFlightsAdapter.ViewHolder>() {

    private companion object {
        const val ITEM_TYPE_HEADER = 0
        const val ITEM_TYPE_ITEM = 1
    }

    /* @StringDef(
         TrackedFlightListTypeHeader.INBOUND,
         TrackedFlightListTypeHeader.LANDED
     )
     @Retention(AnnotationRetention.SOURCE)
     annotation class TrackedFlightListTypeHeader {
         companion object {
             const val INBOUND = "Inbound"
             const val LANDED = "Landed"
         }
     }*/

    fun setItems(listFlight: List<Flight>) {
        this.data = listFlight
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                (if (viewType == ITEM_TYPE_HEADER)
                    R.layout.tracked_airplane_list_type_header
                else //ITEM_TYPE_ITEM
                    R.layout.tracked_airplane_item
                        )
                ,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flight = data[position]
        holder.name_company_and_aircraft_tail_number.text =
            holder.containerView.resources.getString(
                R.string.tracked_aircraft_text_template,
                flight.operatedBy.name,
                flight.aircraft.tailSign
            )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_TYPE_ITEM
//        if (position == 0) ITEM_TYPE_HEADER //todo: how to add extra item as first item in a RecyclerView Adapter
//        else
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer

}