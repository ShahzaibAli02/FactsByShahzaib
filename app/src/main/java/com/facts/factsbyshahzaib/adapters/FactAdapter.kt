package com.facts.factsbyshahzaib.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.facts.factsbyshahzaib.databinding.ItemFactBinding
import com.facts.factsbyshahzaib.model.Fact

class FactAdapter() : RecyclerView.Adapter<FactAdapter.ViewHolder>() {
    private var factList: List<Fact>  = ArrayList()
    fun setList(factList: List<Fact> )
    {
        this.factList=factList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(factList[position])
    }

    override fun getItemCount(): Int {
        return factList.size
    }

    inner class ViewHolder(private val binding: ItemFactBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(fact: Fact) {
            binding.txtFactNo.text = "Fact #${adapterPosition + 1}"
            binding.txtFactDesc.text = fact.text
        }
    }
}
