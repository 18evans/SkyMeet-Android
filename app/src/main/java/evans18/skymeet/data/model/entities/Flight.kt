package evans18.skymeet.data.model.entities

import androidx.room.*
import com.google.gson.annotations.SerializedName
import evans18.skymeet.data.database.FlightPositionListConverter
import evans18.skymeet.data.model.Aircraft
import evans18.skymeet.data.model.FlightPosition
import evans18.skymeet.data.model.LocalhostModel
import evans18.skymeet.data.model.Operator

@Entity(tableName = "flights")
@LocalhostModel
data class Flight constructor(

    //localhost models
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val dbId: Int?,
    @Embedded(prefix = "aircraft_")
    @SerializedName("aircraft")
    val aircraft: Aircraft,
    @Embedded(prefix = "operator_")
    @SerializedName("operatedBy")
    val operatedBy: Operator,
    @SerializedName("flightPositions")
    @TypeConverters(FlightPositionListConverter::class)
    val flightPositions: List<FlightPosition>?


    //flight stats model
    /*    @SerializedName("flightId")
    val flightId: String,
    @SerializedName("callsign")
    val callSign: String,
    @SerializedName("tailNumber")
    val tailNumber: String,
    //last recorded positions of this flight
    //Latest position is last in list. Usually from 5 minutes before the request was made
    @SerializedName("positions")
    val positions: List<FlightPosition>
    */
)