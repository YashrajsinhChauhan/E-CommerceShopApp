package com.example.e_commerceshopapp.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceshopapp.R
import com.example.e_commerceshopapp.databinding.ItemCartLayoutBinding
import com.example.e_commerceshopapp.firestore.FireStoreClass
import com.example.e_commerceshopapp.models.Cart
import com.example.e_commerceshopapp.ui.activities.CartActivity
import com.example.e_commerceshopapp.utils.Constants
import com.example.e_commerceshopapp.utils.GlideLoader

open class CartItemsAdapter(
    private val context: Context,
    private var list: ArrayList<Cart>,
    private val updateCartItems: Boolean
) : RecyclerView.Adapter<CartItemsAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding : ItemCartLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ItemCartLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        GlideLoader(context).loadProductPicture(model.image, holder.binding.ivCartItemImage)

        holder.binding.tvCartItemTitle.text = model.title
        holder.binding.tvCartItemPrice.text = "$${model.price}"
        holder.binding.tvCartQuantity.text = model.cart_quantity

        if (model.cart_quantity == "0") {
            holder.binding.ibRemoveCartItem.visibility = View.GONE
            holder.binding.ibAddCartItem.visibility = View.GONE

            if (updateCartItems) {
                holder.binding.ibDeleteCartItem.visibility = View.VISIBLE
            } else {
                holder.binding.ibDeleteCartItem.visibility = View.GONE
            }

            holder.binding.tvCartQuantity.text = context.resources.getString(R.string.lbl_out_of_stock)
            holder.binding.tvCartQuantity.setTextColor(ContextCompat.getColor(context, R.color.color_error))

        } else {

            if (updateCartItems) {
                holder.binding.ibRemoveCartItem.visibility = View.VISIBLE
                holder.binding.ibAddCartItem.visibility = View.VISIBLE
                holder.binding.ibDeleteCartItem.visibility = View.VISIBLE
            } else {

                holder.binding.ibRemoveCartItem.visibility = View.GONE
                holder.binding.ibAddCartItem.visibility = View.GONE
                holder.binding.ibDeleteCartItem.visibility = View.GONE
            }

            holder.binding.tvCartQuantity.setTextColor(ContextCompat.getColor(context, R.color.sec_text))
        }

        holder.binding.ibRemoveCartItem.setOnClickListener {

            if (model.cart_quantity == "1") {
                FireStoreClass().removeItemFromCart(context, model.id)
            } else {

                val cartQuantity: Int = model.cart_quantity.toInt()
                val itemHashMap = HashMap<String, Any>()

                itemHashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()

                if (context is CartActivity) {
                    context.showProgressDialog()
                }

                FireStoreClass().updateMyCart(context, model.id, itemHashMap)
            }
        }

        holder.binding.ibAddCartItem.setOnClickListener {

            val cartQuantity: Int = model.cart_quantity.toInt()

            if (cartQuantity < model.stock_quantity.toInt()) {

                val itemHashMap = HashMap<String, Any>()

                itemHashMap[Constants.CART_QUANTITY] = (cartQuantity + 1).toString()

                if (context is CartActivity) {
                    context.showProgressDialog()
                }

                FireStoreClass().updateMyCart(context, model.id, itemHashMap)
            } else {
                if (context is CartActivity) {
                    context.showErrorSnackBar(context.resources.getString(R.string.msg_for_available_stock, model.stock_quantity), true)
                }
            }
        }

        holder.binding.ibDeleteCartItem.setOnClickListener {

            when (context) {
                is CartActivity -> {
                    context.showProgressDialog()
                }
            }

            FireStoreClass().removeItemFromCart(context, model.id)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}