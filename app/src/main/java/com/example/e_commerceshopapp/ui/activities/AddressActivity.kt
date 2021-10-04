package com.example.e_commerceshopapp.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceshopapp.R
import com.example.e_commerceshopapp.databinding.ActivityAddressBinding
import com.example.e_commerceshopapp.firestore.FireStoreClass
import com.example.e_commerceshopapp.models.Address
import com.example.e_commerceshopapp.ui.adapters.AddressAdapter
import com.example.e_commerceshopapp.utils.Constants
import com.example.e_commerceshopapp.utils.SwipeToDeleteCallback
import com.example.e_commerceshopapp.utils.SwipeToEditCallback

class AddressActivity : BaseActivity() {

    private lateinit var binding: ActivityAddressBinding
    private var mSelectAddress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS)) {
            mSelectAddress = intent.getBooleanExtra(Constants.EXTRA_SELECT_ADDRESS, false)
        }

        if (mSelectAddress) {
            binding.tvTitle.text = resources.getString(R.string.title_select_address)
        }

        binding.tvAddAddress.setOnClickListener {
            val intent = Intent(this@AddressActivity, AddEditAddressActivity::class.java)
            startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
        }

        setupActionBar()
        getAddressList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.ADD_ADDRESS_REQUEST_CODE) {

                getAddressList()
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {

            Log.e("Request Cancelled", "To add the address.")
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarAddressActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24)
        }

        binding.toolbarAddressActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getAddressList() {

        showProgressDialog()
        FireStoreClass().getAddressesList(this@AddressActivity)
    }

    fun successAddressListFromFirestore(addressList: ArrayList<Address>) {

        hideProgressDialog()

        if (addressList.size > 0) {

            binding.rvAddress.visibility = View.VISIBLE
            binding.tvNoAddressFound.visibility = View.GONE

            binding.rvAddress.layoutManager = LinearLayoutManager(this@AddressActivity)
            binding.rvAddress.setHasFixedSize(true)

            val addressAdapter = AddressAdapter(this@AddressActivity, addressList, mSelectAddress)
            binding.rvAddress.adapter = addressAdapter

            if (!mSelectAddress) {
                val editSwipeHandler = object : SwipeToEditCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                        val adapter = binding.rvAddress.adapter as AddressAdapter

                        adapter.notifyEditItem(this@AddressActivity, viewHolder.adapterPosition)
                    }
                }
                val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
                editItemTouchHelper.attachToRecyclerView(binding.rvAddress)

                val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                        showProgressDialog()

                        FireStoreClass().deleteAddress(this@AddressActivity, addressList[viewHolder.adapterPosition].id)
                    }
                }
                val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
                deleteItemTouchHelper.attachToRecyclerView(binding.rvAddress)
            }
        } else {
            binding.rvAddress.visibility = View.GONE
            binding.tvNoAddressFound.visibility = View.VISIBLE
        }
    }

    fun deleteAddressSuccess() {

        hideProgressDialog()
        Toast.makeText(this@AddressActivity, resources.getString(R.string.err_your_address_deleted_successfully), Toast.LENGTH_SHORT).show()
        getAddressList()
    }
}
