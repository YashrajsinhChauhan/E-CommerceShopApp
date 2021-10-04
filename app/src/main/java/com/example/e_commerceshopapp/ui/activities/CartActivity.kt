package com.example.e_commerceshopapp.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerceshopapp.R
import com.example.e_commerceshopapp.databinding.ActivityCartBinding
import com.example.e_commerceshopapp.firestore.FireStoreClass
import com.example.e_commerceshopapp.models.Cart
import com.example.e_commerceshopapp.models.Product
import com.example.e_commerceshopapp.ui.adapters.CartItemsAdapter
import com.example.e_commerceshopapp.utils.Constants

class CartActivity : BaseActivity() {

    private lateinit var binding : ActivityCartBinding
    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartItems: ArrayList<Cart>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        binding.btnCheckout.setOnClickListener {
            val intent = Intent(this@CartActivity, AddressActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS, true)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getProductList()
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarCartActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24)
        }
        binding.toolbarCartActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getProductList() {

        showProgressDialog()
        FireStoreClass().getAllProductsList(this@CartActivity)
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        mProductsList = productsList
        getCartItemsList()
    }

    private fun getCartItemsList() {

        FireStoreClass().getCartList(this@CartActivity)
    }

    @SuppressLint("SetTextI18n")
    fun successCartItemsList(cartList: ArrayList<Cart>) {

        hideProgressDialog()

        for (product in mProductsList) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {

                    cart.stock_quantity = product.stock_quantity

                    if (product.stock_quantity.toInt() == 0) {
                        cart.cart_quantity = product.stock_quantity
                    }
                }
            }
        }

        mCartItems = cartList

        if (mCartItems.size > 0) {

            binding.rvCartList.visibility = View.VISIBLE
            binding.llCheckout.visibility = View.VISIBLE
            binding.tvNoCartFound.visibility = View.GONE

            binding.rvCartList.layoutManager = LinearLayoutManager(this@CartActivity)
            binding.rvCartList.setHasFixedSize(true)

            val cartListAdapter = CartItemsAdapter(this@CartActivity, mCartItems, true)
            binding.rvCartList.adapter = cartListAdapter

            var subTotal: Double = 0.0

            for (item in mCartItems) {

                val availableQuantity = item.stock_quantity.toInt()

                if (availableQuantity > 0) {
                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()

                    subTotal += (price * quantity)
                }
            }

            binding.tvSubTotal.text = "$$subTotal"
            binding.tvShippingCharge.text = "$10.0"

            if (subTotal > 0) {
                binding.llCheckout.visibility = View.VISIBLE

                val total = subTotal + 10
                binding.tvTotalAmount.text = "$$total"
            } else {
                binding.llCheckout.visibility = View.GONE
            }

        } else {
            binding.rvCartList.visibility = View.GONE
            binding.llCheckout.visibility = View.GONE
            binding.tvNoCartFound.visibility = View.VISIBLE
        }
    }

    fun itemRemovedSuccess() {

        hideProgressDialog()
        Toast.makeText(this@CartActivity, resources.getString(R.string.msg_item_removed_successfully), Toast.LENGTH_SHORT).show()
        getCartItemsList()
    }

    fun itemUpdateSuccess() {

        hideProgressDialog()
        getCartItemsList()
    }
}