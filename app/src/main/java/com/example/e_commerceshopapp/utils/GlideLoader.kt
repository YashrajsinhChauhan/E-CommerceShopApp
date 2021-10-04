package com.example.e_commerceshopapp.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.e_commerceshopapp.R
import java.io.IOException

class GlideLoader(val context : Context) {

    fun loadUserPicture(image : Any, imageview : ImageView){

        try{

            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_placeholder)
                .into(imageview)

        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    fun loadProductPicture(image: Any, imageView: ImageView) {
        try {

            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}