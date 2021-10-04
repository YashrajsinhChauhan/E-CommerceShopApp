package com.example.e_commerceshopapp.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceshopapp.R
import com.example.e_commerceshopapp.databinding.ItemListLayoutBinding
import com.example.e_commerceshopapp.models.Order
import com.example.e_commerceshopapp.ui.activities.MyOrderDetailsActivity
import com.example.e_commerceshopapp.utils.Constants
import com.example.e_commerceshopapp.utils.GlideLoader

open class MyOrdersListAdapter(
    private val context: Context,
    private var list: ArrayList<Order>
) : RecyclerView.Adapter<MyOrdersListAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ItemListLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ItemListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        GlideLoader(context).loadProductPicture(model.image, holder.itemView.findViewById(R.id.iv_item_image))

        holder.binding.tvItemName.text = model.title
        holder.binding.tvItemPrice.text = "$${model.total_amount}"

        holder.binding.ibDeleteProduct.visibility = View.GONE

        holder.itemView.setOnClickListener {
            val intent = Intent(context, MyOrderDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_MY_ORDER_DETAILS, model)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}