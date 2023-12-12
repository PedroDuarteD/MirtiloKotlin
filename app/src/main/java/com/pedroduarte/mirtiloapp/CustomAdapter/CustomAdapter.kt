package com.pedroduarte.mirtiloapp.CustomAdapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.pedroduarte.mirtiloapp.R
import com.pedroduarte.mirtiloapp.model.RecordModel

class CustomAdapter(private val list: ArrayList<RecordModel>, private val event: OnTapScreen) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    inner class MyViewHolder(item: View) : RecyclerView.ViewHolder(item), View.OnClickListener {

        val txt_mirtilo_type: TextView = item.findViewById(R.id.txt_mirtilo_type)
        val txt_date_initial: TextView = item.findViewById(R.id.txt_date_initial)
        val txt_amount: TextView = item.findViewById(R.id.txt_mirtilo_amount)
        val txt_state: TextView = item.findViewById(R.id.txt_state)
        val txt_box: TextView = item.findViewById(R.id.txt_box)


        init {
            item.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            event.onClickItem(adapterPosition)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.MyViewHolder {
       val v = LayoutInflater.from(parent.context).inflate(R.layout.card_mirtilo, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: CustomAdapter.MyViewHolder, position: Int) {
       val model = list.get(position)
        holder.txt_mirtilo_type.setText(model.type)
        holder.txt_amount.setText(model.box.toString())
        holder.txt_date_initial.setText(model.date_initial)

        if(model.box>1){
            holder.txt_box.setText("caixas")
        }else{
            holder.txt_box.setText("caixa")

        }
        if(model.state==0){
            holder.txt_state.setText("PENDING")
            holder.txt_state.setTextColor(Color.BLUE)
        }else{
            holder.txt_state.setText("Completed")
            holder.txt_state.setTextColor(Color.GREEN)

        }

    }

    override fun getItemCount(): Int {
       return list.size
    }
    interface OnTapScreen{
        fun onClickItem(position: Int)
    }
}