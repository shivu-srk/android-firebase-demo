package com.example.helloworld

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewRequestListAdapter: RecyclerView.Adapter<ViewRequestListAdapter.ViewRequestViewHolder>() {
    inner class ViewRequestViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewRequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_view_request_item, parent, false)
        return ViewRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewRequestViewHolder, position: Int) {
        holder.textView.text = "Hi"
    }

    override fun getItemCount(): Int = 0


}