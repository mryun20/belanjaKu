package com.example.belanjaku


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.math.BigDecimal

class CustomAdapter (private val dataSet: ArrayList<String>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    var onItemClick: ((Int) ->Unit)? = null
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textView: TextView
        val textCount : TextView
        val logo: ImageView
        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.textData)
            textCount = view.findViewById(R.id.textCount)
            logo = view.findViewById(R.id.textDelete)
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_view, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textCount.text = position.toString()
        viewHolder.textView.text = dataSet[position]
        viewHolder.logo.setImageResource(R.drawable.baseline_clear_24)


        if(item.toBigDecimal() >= BigDecimal.ZERO){
            viewHolder.textView.setTextColor(Color.parseColor("#00ff00"))
        }else{
            viewHolder.textView.setTextColor(Color.parseColor("#FF0000"))
        }

        viewHolder.logo.setOnClickListener(){
            onItemClick?.invoke(position)
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}