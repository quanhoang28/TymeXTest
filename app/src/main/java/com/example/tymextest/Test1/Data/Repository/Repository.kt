import com.example.tymextest.Test1.Data.Model.LastestRateResponse
import com.example.tymextest.Test1.Data.Model.SymbolsResponse
import retrofit2.Call

class ExchangeRateRepository() {
    private val apiService = ApiClient.apiService

    fun getSymbols(apiKey: String): Call<SymbolsResponse> {
        return apiService.getSymbols(apiKey)
    }

    fun getLatestRates(apiKey: String, symbols: String): Call<LastestRateResponse> {
        return apiService.getLatestRates(apiKey, symbols)
    }

}
