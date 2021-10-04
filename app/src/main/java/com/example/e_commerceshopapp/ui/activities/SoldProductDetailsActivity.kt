package com.example.e_commerceshopapp.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.e_commerceshopapp.R
import com.example.e_commerceshopapp.databinding.ActivitySoldProductDetailsBinding
import com.example.e_commerceshopapp.models.SoldProducts
import com.example.e_commerceshopapp.utils.Constants
import com.example.e_commerceshopapp.utils.GlideLoader
import java.text.SimpleDateFormat
import java.util.*

class SoldProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySoldProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoldProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var productDetails: SoldProducts = SoldProducts()

        if (intent.hasExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)) {
            productDetails = intent.getParcelableExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)!!
        }

        setupActionBar()
        setupUI(productDetails)
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarSoldProductDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24)
        }

        binding.toolbarSoldProductDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI(productDetails: SoldProducts) {

        binding.tvSoldProductDetailsId.text = productDetails.order_id

        val dateFormat = "dd MMM yyyy HH:mm"

        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = productDetails.order_date
        binding.tvSoldProductDetailsDate.text = formatter.format(calendar.time)

        GlideLoader(this@SoldProductDetailsActivity).loadProductPicture(productDetails.image, binding.ivProductItemImage)

        binding.tvProductItemName.text = productDetails.title
        binding.tvProductItemPrice.text ="$${productDetails.price}"
        binding.tvSoldProductQuantity.text = productDetails.sold_quantity

        binding.tvSoldDetailsAddressType.text = productDetails.address.type
        binding.tvSoldDetailsFullName.text = productDetails.address.name
        binding.tvSoldDetailsAddress.text = "${productDetails.address.address}, ${productDetails.address.zipCode}"
        binding.tvSoldDetailsAdditionalNote.text = productDetails.address.additionalNote

        if (productDetails.address.otherDetails.isNotEmpty()) {

            binding.tvSoldDetailsOtherDetails.visibility = View.VISIBLE
            binding.tvSoldDetailsOtherDetails.text = productDetails.address.otherDetails
        } else {

            binding.tvSoldDetailsOtherDetails.visibility = View.GONE
        }

        binding.tvSoldDetailsMobileNumber.text = productDetails.address.mobileNumber
        binding.tvSoldProductSubTotal.text = productDetails.sub_total_amount
        binding.tvSoldProductShippingCharge.text = productDetails.shipping_charge
        binding.tvSoldProductTotalAmount.text = productDetails.total_amount
    }
}