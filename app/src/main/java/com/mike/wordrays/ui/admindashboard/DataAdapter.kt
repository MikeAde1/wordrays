package com.mike.wordrays.ui.admindashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mike.wordrays.R
import com.mike.wordrays.data.model.Member
import kotlinx.android.synthetic.main.adapter_data.view.*

class DataAdapter(val context: Context, var memberlist: List<Member>): RecyclerView.Adapter<DataAdapter.Dataholder>() {
    lateinit var onClick: ((Member) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Dataholder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_data, parent, false)
        return Dataholder(view)
    }

    override fun getItemCount(): Int {
        return memberlist.size
    }

    override fun onBindViewHolder(holder: Dataholder, position: Int) {
        holder.bind(memberlist[position])
    }

    fun setList(data: List<Member>) {
        memberlist = data.toMutableList()
        notifyDataSetChanged()
    }

    inner class Dataholder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(data: Member) {
            itemView.fullName.text = data.full_name
            itemView.address.text = data.phone_number

            itemView.setOnClickListener {
                onClick.invoke(data)
            }
        }
    }
}