package evans18.skymeet.data.model

import com.google.gson.annotations.SerializedName

@LocalhostModel
class Operator constructor(
    @SerializedName("name")
    val name: String
)