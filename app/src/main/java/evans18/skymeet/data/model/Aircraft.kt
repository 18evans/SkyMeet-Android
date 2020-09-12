package evans18.skymeet.data.model

import com.google.gson.annotations.SerializedName

@LocalhostModel
data class Aircraft constructor(

    //localhost models
    @SerializedName("aircraftId")
    val aircraftId: Int,
    @SerializedName("callsign")
    val callSign: String?,
    @SerializedName("tailsign")
    val tailSign: String

    /*flightsStats model
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val flightId: Int,
    @SerializedName("blockNumber")
    val blockNumber: String,
    @SerializedName("orderId")
    val orderId: Int,
    @SerializedName("registration")
    val registration: String,
    @SerializedName("serialNumber")
    val serialNumber: String,
    @Embedded(prefix = "status_")
    @SerializedName("status")
    val status: Status,
    @Embedded(prefix = "manager_")
    @SerializedName("manager")
    val manager: Manager
) {

    data class Status constructor(
        @SerializedName("status")
        val status: String,
        @SerializedName("changeDate")
        val changeDate: String,
        @SerializedName("durationInYear")
        val durationInYear: Double
    )

    data class Manager constructor(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("category")
        val category: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("region")
        val region: String,
        @SerializedName("country")
        val country: String,
        @SerializedName("state")
        val state: String
    )
}*/

)
