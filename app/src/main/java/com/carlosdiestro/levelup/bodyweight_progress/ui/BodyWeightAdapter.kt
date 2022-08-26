package com.carlosdiestro.levelup.bodyweight_progress.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carlosdiestro.levelup.core.ui.StringValue
import com.carlosdiestro.levelup.core.ui.toText
import com.carlosdiestro.levelup.databinding.ItemBodyWeightBinding

class BodyWeightAdapter :
    ListAdapter<BodyWeightPLO, BodyWeightAdapter.ViewHolder>(BodyWeightPLO.BodyWeightDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent) {

        private val binding = ItemBodyWeightBinding.bind(itemView)

        fun bind(item: BodyWeightPLO) {
            binding.apply {
                tvDate.text = bindDate(item)
                tvWeight.text = StringValue.Placeholder.Kg.toText(binding.root.context, item.weight)
            }
        }

        private fun bindDate(item: BodyWeightPLO): String {
            return if(item.dateLabelId != -1) binding.root.context.getString(item.dateLabelId)
            else item.date
        }
    }
}