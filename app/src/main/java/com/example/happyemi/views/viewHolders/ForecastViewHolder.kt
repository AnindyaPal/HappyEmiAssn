package com.example.happyemi.views.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.happyemi.R

class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvDay : TextView = itemView.findViewById(R.id.tvDay)
    var tvTemp : TextView = itemView.findViewById(R.id.tvTemp)
}