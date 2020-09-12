package evans18.skymeet.data.model

import com.google.gson.annotations.SerializedName

@Deprecated("Use localhost models instead")
data class AircraftResponse constructor(

    @SerializedName("aircraft")
    val aircraft: Aircraft
)