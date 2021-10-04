package com.example.e_commerceshopapp.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.e_commerceshopapp.R
import com.example.e_commerceshopapp.databinding.ActivitySettingsBinding
import com.example.e_commerceshopapp.firestore.FireStoreClass
import com.example.e_commerceshopapp.models.Users
import com.example.e_commerceshopapp.utils.Constants
import com.example.e_commerceshopapp.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var mUserDetails : Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        binding.tvEdit.setOnClickListener(this@SettingsActivity)
        binding.btnLogout.setOnClickListener(this@SettingsActivity)
        binding.llAddress.setOnClickListener(this@SettingsActivity)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.tv_edit -> {
                    val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                }

                R.id.ll_address -> {
                    startActivity(Intent(this@SettingsActivity, AddressActivity::class.java))
                }

                R.id.btn_logout -> {

                    FirebaseAuth.getInstance().signOut()

                    val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun setupActionBar(){

        setSupportActionBar(binding.toolbarSettingsActivity)

        val actionBar = supportActionBar
        if(actionBar != null){

            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24)
        }

        binding.toolbarSettingsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getUserDetails(){

        showProgressDialog()
        FireStoreClass().getUserDetails(this)
    }

    @SuppressLint("SetTextI18n")
    fun userDetailsSuccess(user : Users){

        mUserDetails = user

        hideProgressDialog()
        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, binding.ivUserPhoto)
        binding.tvName.text = "${user.firstName} ${user.lastName}"
        binding.tvGender.text = user.gender
        binding.tvEmail.text = user.email
        binding.tvMobileNumber.text = "${user.mobile}"
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }
}