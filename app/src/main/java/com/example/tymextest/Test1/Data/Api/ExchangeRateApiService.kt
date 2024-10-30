//import com.example.tymextest.Test1.Data.Model.ConvertResponse
import com.example.tymextest.Test1.Data.Model.LastestRateResponse
import com.example.tymextest.Test1.Data.Model.SymbolsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ExchangeRateApiService {
    @GET("symbols")
    fun getSymbols(@Query("access_key") apiKey: String): Call<SymbolsResponse>

    @GET("latest")
    fun getLatestRates(
        @Query("access_key") apiKey: String,
        @Query("symbols") symbols: String
    ): Call<LastestRateResponse>
}
