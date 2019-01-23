package com.example.happyemi.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyemi.R
import com.example.happyemi.Utils
import com.example.happyemi.models.ForecastDay
import com.example.happyemi.views.viewHolders.ForecastViewHolder

class RvForecastAdapter(val forecastDays : MutableList<ForecastDay>) : RecyclerView.Adapter<ForecastViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.forecast_item, parent, false)
        return ForecastViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return forecastDays.size
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.tvDay.text = Utils.getWeekDayName(forecastDays[position].date)
        holder.tvTemp.text = forecastDays[position].day.avgtemp_c.toInt().toString()+ " C"
    }

}