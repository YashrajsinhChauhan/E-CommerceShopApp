package com.example.e_commerceshopapp.ui.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceshopapp.databinding.ItemAddressLayoutBinding
import com.example.e_commerceshopapp.models.Address
import com.example.e_commerceshopapp.ui.activities.AddEditAddressActivity
import com.example.e_commerceshopapp.ui.activities.CheckoutActivity
import com.example.e_commerceshopapp.utils.Constants

open class AddressAdapter(
    private val context: Context,
    private var list: ArrayList<Address>,
    private val selectAddress: Boolean
) : RecyclerView.Adapter<AddressAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding : ItemAddressLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ItemAddressLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        holder.binding.tvAddressFullName.text = model.name
        holder.binding.tvAddressType.text = model.type
        holder.binding.tvAddressDetails.text = "${model.address}, ${model.zipCode}"
        holder.binding.tvAddressMobileNumber.text = model.mobileNumber

        if (selectAddress) {
            holder.itemView.setOnClickListener {

                val intent = Intent(context, CheckoutActivity::class.java)
                intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS, model)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun notifyEditItem(activity: Activity, position: Int) {
        val intent = Intent(context, AddEditAddressActivity::class.java)
        intent.putExtra(Constants.EXTRA_ADDRESS_DETAILS, list[position])
        activity.startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
        notifyItemChanged(position)
    }
}