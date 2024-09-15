package com.example.belanjaku

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {

    private lateinit var exampleArray: ArrayList<String>
    private lateinit var customAdapter: CustomAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var textData : EditText
    private lateinit var textSum :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        loadData()

//        exampleArray = ArrayList()
        customAdapter = CustomAdapter(exampleArray)
        recyclerView = findViewById(R.id.listView)
        textData = findViewById(R.id.editInput)
        textSum = findViewById(R.id.textAnswer)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = customAdapter

        customAdapter.onItemClick = {
            removeItem(it)
        }

        val buttonAdd : Button = findViewById(R.id.add)
        val buttonMinus : Button = findViewById(R.id.minus)

        buttonAdd.setOnClickListener(){
            actionButton("+")

        }
        buttonMinus.setOnClickListener(){
            actionButton("-")
        }

    }

    /**
     * Remove the item and updated the view
     * (custom ViewHolder)
     */
    private fun removeItem(position:Int) {
        if(position >= 0){
            exampleArray.removeAt(position)
            customAdapter.notifyItemRemoved(position)
            customAdapter.notifyItemRangeChanged(position,exampleArray.count());

            sumAmount()
            saveData()
        }
    }

    /**
     * Do a calculation and view the total amount and display changes for total amount
     */
    private fun sumAmount() {
        val dec = DecimalFormat("0.00")
        var sum = BigDecimal.ZERO
        for(s:String in exampleArray){
            sum += s.toBigDecimal()
        }

        textSum.setText(dec.format(sum)).toString()
        if(sum >= BigDecimal.ZERO){
            textSum.setTextColor(Color.parseColor("#00ff00"))
        }else{
            textSum.setTextColor(Color.parseColor("#FF0000"))
        }
    }

    /**
     * Handle button on click based on positive and negative integer
     * Handle user input and hiding the soft keyboard after button on click
     * Inserted value and display the view
     */
    private fun actionButton(symbol : String) {
        if(textData.text.toString().isNotEmpty()){
            val dec = DecimalFormat("0.00")
            var input = dec.format(textData.text.toString().toBigDecimal())

            if(symbol == "-"){
                input = "-$input"
            }

            val item = exampleArray.count()

            exampleArray += input
            customAdapter.notifyItemInserted(item)
            sumAmount()
            saveData()

            textData.setText("")
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)

        }else{
            Toast.makeText(this, "Please insert amount", LENGTH_SHORT).show()
        }
    }

    /**
     * Check the data at local preferences for previous record
     * Using share preferences and gson
     */
    private fun loadData() {
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        // creating a variable for gson.
        val gson = Gson()
        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        val json = sharedPreferences.getString("courses", null)
        // below line is to get the type of our array list.
        val type = object : TypeToken<ArrayList<String>>(){}.type
        // in below line we are getting data from gson
        // and saving it to our array list
        if(type.toString().isNotEmpty()){
            exampleArray = gson.fromJson(json, object : TypeToken<ArrayList<String>>() {}.type)
        }else{
            exampleArray = ArrayList()
        }
    }

    /**
     * Save and update data to local shared preferences
     */
    private fun saveData() {
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val example = exampleArray
        val json = gson.toJson(example)
        editor.putString("courses", json)
        editor.apply()
    }


}