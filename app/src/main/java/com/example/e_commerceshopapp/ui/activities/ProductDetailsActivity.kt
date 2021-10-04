package com.example.e_commerceshopapp.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.e_commerceshopapp.R
import com.example.e_commerceshopapp.databinding.ActivityProductDetailsBinding
import com.example.e_commerceshopapp.firestore.FireStoreClass
import com.example.e_commerceshopapp.models.Cart
import com.example.e_commerceshopapp.models.Product
import com.example.e_commerceshopapp.utils.Constants
import com.example.e_commerceshopapp.utils.GlideLoader

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var mProductDetails: Product
    private var mProductId : String = ""
    private var mProductOwnerId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            mProductOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }

        if(FireStoreClass().getCurrentUserID() == mProductOwnerId){
            binding.btnAddToCart.visibility = View.GONE
            binding.btnGoToCart.visibility = View.GONE
        }else{
            binding.btnAddToCart.visibility = View.VISIBLE
        }

        binding.btnAddToCart.setOnClickListener(this)
        binding.btnGoToCart.setOnClickListener(this)

        setupActionBar()

        getProductDetails()
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {

                R.id.btn_add_to_cart -> {
                    addToCart()
                }

                R.id.btn_go_to_cart->{
                    startActivity(Intent(this@ProductDetailsActivity, CartActivity::class.java))
                }
            }
        }
    }

    private fun addToCart() {

        val addToCart = Cart(FireStoreClass().getCurrentUserID(),
            mProductOwnerId,
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY)

        showProgressDialog()

        FireStoreClass().addCartItems(this@ProductDetailsActivity, addToCart)
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarProductDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24)
        }

        binding.toolbarProductDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getProductDetails() {

        showProgressDialog()
        FireStoreClass().getProductDetails(this@ProductDetailsActivity, mProductId)
    }

    @SuppressLint("SetTextI18n")
    fun productDetailsSuccess(product: Product) {

        mProductDetails = product

        GlideLoader(this@ProductDetailsActivity).loadProductPicture(product.image, binding.ivProductDetailImage)

        binding.tvProductDetailsTitle.text = product.title
        binding.tvProductDetailsPrice.text = "$${product.price}"
        binding.tvProductDetailsDescription.text = product.description
        binding.tvProductDetailsStockQuantity.text = product.stock_quantity

        if (product.stock_quantity.toInt() == 0) {

            hideProgressDialog()

            binding.btnAddToCart.visibility = View.GONE
            binding.tvProductDetailsStockQuantity.text = resources.getString(R.string.lbl_out_of_stock)
            binding.tvProductDetailsStockQuantity.setTextColor(
                ContextCompat.getColor(this@ProductDetailsActivity, R.color.color_error))
        }else{

            if (FireStoreClass().getCurrentUserID() == product.user_id) {
                hideProgressDialog()

            } else {
                FireStoreClass().checkIfItemExistInCart(this@ProductDetailsActivity, mProductId)
            }
        }
    }

    fun productExistsInCart() {

        hideProgressDialog()

        binding.btnAddToCart.visibility = View.GONE
        binding.btnGoToCart.visibility = View.VISIBLE
    }

    fun addToCartSuccess() {

        hideProgressDialog()

        Toast.makeText(this@ProductDetailsActivity, resources.getString(R.string.success_message_item_added_to_cart), Toast.LENGTH_SHORT).show()

        binding.btnAddToCart.visibility = View.GONE
        binding.btnGoToCart.visibility = View.VISIBLE
    }
}