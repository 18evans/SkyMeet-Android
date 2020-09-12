package evans18.skymeet.data.remote

import io.reactivex.Completable
import retrofit2.http.POST
import retrofit2.http.Path

interface FirebaseTokenApiService {

    private companion object {
        const val PREFIX_ENDPOINT_TOKEN = "firebase/tokens"
    }

    @POST("${PREFIX_ENDPOINT_TOKEN}/{token}")
    fun createToken(
        @Path("token") token: String
    ): Completable
}