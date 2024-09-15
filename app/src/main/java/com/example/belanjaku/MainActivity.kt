package com.example.belanjaku

import android.graphics.Color
import android.os.Bundle
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

        exampleArray = ArrayList()
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

//            Toast.makeText(this, "Add", LENGTH_SHORT).show()

            textData.setText("")
//            textData.clearFocus()

            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)

        }else{
            Toast.makeText(this, "Please insert amount", LENGTH_SHORT).show()
        }
    }
}