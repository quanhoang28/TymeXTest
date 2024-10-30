package com.example.tymextest
import ExchangeRateViewModel
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SearchView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.transition.Visibility
import com.example.tymextest.Test2_1.Inventory
import com.example.tymextest.Test2_1.Product
import com.example.tymextest.Test2_2.MyList
import org.w3c.dom.Text
import java.util.Objects

class MainActivity : AppCompatActivity() {
    private val apiKey = "9e25e948369a7bd6d24a97b395a8339c"
    private val exchangeRateViewModel: ExchangeRateViewModel by viewModels()
    private lateinit var mySpinner: TextView
    private lateinit var mySecondSpinner: TextView
    private lateinit var convertBtn: Button
    private lateinit var convertedText: TextView
    private lateinit var amountEditText: EditText
    private lateinit var txtExchangeRate: TextView
    private lateinit var txtIndicative: TextView
    private lateinit var btnIcon: Button
    private lateinit var cardTo: CardView
    private lateinit var cardFrom: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        installSplashScreen()
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

//        test21()//test 2.1

//        test22()//test2.2

        mySpinner = findViewById(R.id.txtFromCurrency)
        mySecondSpinner= findViewById(R.id.txtToCurrency )
        convertBtn=findViewById(R.id.btnConvert)
        convertedText= findViewById(R.id.txtConverted)
        amountEditText= findViewById(R.id.edtAmount)
        txtExchangeRate= findViewById(R.id.txtExchangeRate)
        txtIndicative= findViewById(R.id.textView)
        btnIcon= findViewById(R.id.btnIcon)
        cardFrom= findViewById(R.id.cardFrom)
        cardTo= findViewById(R.id.cardTo)

        exchangeRateViewModel.fromCurrency.observe(this, Observer { currency ->
            mySpinner.text = currency
        })

        exchangeRateViewModel.toCurrency.observe(this, Observer { currency ->
            mySecondSpinner.text = currency
        })

        // Khi người dùng thay đổi giá trị trong TextView, cập nhật vào ViewModel
        cardFrom.setOnClickListener {
            showSearchDialog(exchangeRateViewModel.symbols.value ?: listOf()) { selectedCurrency ->
                mySpinner.text = selectedCurrency
                exchangeRateViewModel.fromCurrency.value = selectedCurrency // Cập nhật ViewModel
            }
        }

        cardTo.setOnClickListener {
            showSearchDialog(exchangeRateViewModel.symbols.value ?: listOf()) { selectedCurrency ->
                mySecondSpinner.text = selectedCurrency
                exchangeRateViewModel.toCurrency.value = selectedCurrency // Cập nhật ViewModel
            }
        }

        exchangeRateViewModel.errorMessage.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        exchangeRateViewModel.convertedValue.observe(this, Observer { convertedValue->
            convertedText.text=String.format("%.4f", convertedValue)
        })

        exchangeRateViewModel.exchangeRate.observe(this, Observer { rate->
            txtExchangeRate.visibility=View.VISIBLE
            txtIndicative.visibility=View.VISIBLE
            txtExchangeRate.text=rate
        })


        exchangeRateViewModel.fetchSymbols(apiKey)

        btnIcon.setOnClickListener{
            val fromPosition = mySpinner.text
            val toPosition = mySecondSpinner.text
            mySpinner.text=toPosition
            mySecondSpinner.text=fromPosition

            convertCurrency()
        }

        convertBtn.setOnClickListener {
           convertCurrency()
        }
    }

    private fun convertCurrency() {
        val fromCurrency = mySpinner.text.toString()
        val toCurrency = mySecondSpinner.text.toString()
        val amountString = amountEditText.text.toString()

        // Check if the input amount is valid
        if (amountString.isEmpty()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountString.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Please enter a valid amount greater than zero", Toast.LENGTH_SHORT).show()
            return
        }

        // Ensure that currency symbols are not empty before converting
        if (fromCurrency.isEmpty() || toCurrency.isEmpty()) {
            Toast.makeText(this, "Please select currencies", Toast.LENGTH_SHORT).show()
            return
        }

        // Fetch the latest exchange rates if all validations are passed
        exchangeRateViewModel.fetchLatestRates(apiKey, fromCurrency, toCurrency, amount)
    }


    private fun test21(){
        //Test 2.1
        var inv1: Inventory =Inventory(listOf(
            Product("Laptop", 999.9, 5),
            Product("Smartphone", 499.9, 10),
            Product("Tablet", 299.99, 0),
            Product("Smartwatch", 199.99, 3)
        ),)

        inv1.printInventory()
        inv1.getTotalValue()
        inv1.getHighestPriceProduct()
        inv1.checkStock("Laptop")
        inv1.sortByPrice(true)
        Log.v("testInput","--------------------------")
        inv1.sortByPrice(false)
        Log.v("testInput","--------------------------")
        inv1.sortByQuantity(true)
        Log.v("testInput","--------------------------")
        inv1.sortByQuantity(false)
    }

    private fun test22(){
        Log.v("testInput", MyList.findMissingNumber(listOf(3, 7, 1, 6, 4)).toString())
    }

    fun showSearchDialog(symbols: List<String>, onItemSelected: (String) -> Unit) {
        // Create Dialog
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.search_dialog)

        // Set dialog to be cancelable
        dialog.setCancelable(true)

        // Get references to SearchView and ListView in Dialog
        val searchView = dialog.findViewById<SearchView>(R.id.searchView)
        val listView = dialog.findViewById<ListView>(R.id.listView)

        // Simulate data display in ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, symbols.toMutableList())
        listView.adapter = adapter

        // Filter data when user types in SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)  // Filter data based on keyword
                return true
            }
        })

        // Set the item click listener to update the button text
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedCurrency = adapter.getItem(position) // Lấy mục đã chọn từ adapter
            if (selectedCurrency != null) {
                onItemSelected(selectedCurrency) // Call the lambda with the selected item
                dialog.dismiss() // Dismiss the dialog
            }
        }

        // Show the dialog
        dialog.show()
    }
}