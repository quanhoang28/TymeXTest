import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tymextest.Test1.Data.Model.LastestRateResponse
import com.example.tymextest.Test1.Data.Model.SymbolsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExchangeRateViewModel : ViewModel() {

    private val repository = ExchangeRateRepository()

    private val _symbols = MutableLiveData<List<String>>()
    val symbols: LiveData<List<String>> get() = _symbols

    val latestRates = MutableLiveData<Map<String, Double>>() // Map of currency symbol to rate
    val convertedValue = MutableLiveData<Double>()
    val exchangeRate = MutableLiveData<String>()

    val fromCurrency = MutableLiveData<String>()
    val toCurrency = MutableLiveData<String>()

    init {
        fromCurrency.value = "USD"
        toCurrency.value = "USD"
    }

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchSymbols(apiKey: String) {
        repository.getSymbols(apiKey).enqueue(object : Callback<SymbolsResponse> {
            override fun onResponse(call: Call<SymbolsResponse>, response: Response<SymbolsResponse>) {
                if (response.isSuccessful) {
                    val symbolsMap = response.body()?.symbols
                    _symbols.value = symbolsMap?.keys?.toList() ?: emptyList()
                } else {
                    _errorMessage.value = "Failed to fetch symbols: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<SymbolsResponse>, t: Throwable) {
                _errorMessage.value = "Error fetching symbols: ${t.message}"
                _symbols.value = emptyList() // Clear list on error
            }
        })
    }

    fun fetchLatestRates(apiKey: String, from: String, to: String, amount: Double) {
        repository.getLatestRates(apiKey, "$from,$to").enqueue(object : Callback<LastestRateResponse> {
            override fun onResponse(call: Call<LastestRateResponse>, response: Response<LastestRateResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    latestRates.value = response.body()?.rates
                    Log.v("outputTest", latestRates.value.toString())
                    convertCurrency(from, to, amount)
                } else {
                    _errorMessage.value = "Failed to fetch rates: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<LastestRateResponse>, t: Throwable) {
                _errorMessage.value = "Error fetching rates: ${t.message}"
            }
        })
    }

    fun convertCurrency(from: String, to: String, amount: Double) {
        val rates = latestRates.value ?: run {
            _errorMessage.value = "No exchange rates available."
            return
        }

        Log.v("outputTest", "${rates[from]} / ${rates[to]} / $amount")

        if (rates.containsKey(from) && rates.containsKey(to)) {
            val converted = amount * (rates[to]!! / rates[from]!!)
            Log.v("outputTest", converted.toString())
            val rate: Double = converted / amount
            exchangeRate.value = "1 $from = ${String.format("%.4f", rate)} $to"
            convertedValue.value = converted
        } else {
            _errorMessage.value = "Exchange rates for $from or $to are not available."
        }
    }
}
