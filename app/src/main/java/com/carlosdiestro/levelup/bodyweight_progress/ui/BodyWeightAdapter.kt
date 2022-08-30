package com.carlosdiestro.levelup.bodyweight_progress.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carlosdiestro.levelup.bodyweight_progress.ui.models.BodyWeightPLO
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.core.ui.resources.toText
import com.carlosdiestro.levelup.databinding.ItemBodyWeightBinding

class BodyWeightAdapter :
    ListAdapter<BodyWeightPLO, BodyWeightAdapter.ViewHolder>(BodyWeightPLO.BodyWeightDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemBodyWeightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(private val binding: ItemBodyWeightBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BodyWeightPLO) {
            binding.apply {
                tvDate.text = bindDate(item)
                tvWeight.text =
                    StringResource.Kg.toText(binding.root.context, item.weight.toString())
            }
        }

        private fun bindDate(item: BodyWeightPLO): String {
            return if (item.dateLabelId != -1) binding.root.context.getString(item.dateLabelId)
            else item.date
        }
    }
}