package evans18.skymeet.data.database

import androidx.room.TypeConverter
import evans18.skymeet.data.model.FlightPosition
import evans18.skymeet.util.GenericsTypeConverter

class FlightPositionListConverter {

    @TypeConverter
    fun fromFlightPositionListString(flightPositionsList: List<FlightPosition>): String {
        return GenericsTypeConverter.fromAnyObject(flightPositionsList)
    }

    @TypeConverter
    fun toFlightPositionList(flightPositionsListString: String): List<FlightPosition>? {
        return GenericsTypeConverter.toAnyObject<List<FlightPosition>>(flightPositionsListString)
    }
}