package com.example.e_commerceshopapp.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerceshopapp.R
import com.example.e_commerceshopapp.databinding.FragmentMyProductsBinding
import com.example.e_commerceshopapp.firestore.FireStoreClass
import com.example.e_commerceshopapp.models.Product
import com.example.e_commerceshopapp.ui.activities.AddProductActivity
import com.example.e_commerceshopapp.ui.adapters.MyProductsListAdapter

class MyProductsFragment : BaseFragment() {

    private var _binding: FragmentMyProductsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMyProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.add_product_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem):   Boolean {

        when(item.itemId){

            R.id.action_add_product -> {

                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }

    private fun getProductListFromFireStore() {

        showProgressDialog()
        FireStoreClass().getProductsList(this@MyProductsFragment)
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        hideProgressDialog()

        if (productsList.size > 0) {

            binding.rvMyProductItems.visibility = View.VISIBLE
            binding.tvNoProductsFound.visibility = View.GONE

            binding.rvMyProductItems.layoutManager = LinearLayoutManager(activity)
            binding.rvMyProductItems.setHasFixedSize(true)

            val adapterProducts = MyProductsListAdapter(requireActivity(), productsList, this@MyProductsFragment)
            binding.rvMyProductItems.adapter = adapterProducts
        } else {
            binding.rvMyProductItems.visibility = View.GONE
            binding.tvNoProductsFound.visibility = View.VISIBLE
        }
    }

    fun deleteProduct(productID: String) {

        showAlertDialogToDeleteProduct(productID)
    }

    fun productDeleteSuccess() {

        hideProgressDialog()

        Toast.makeText(requireActivity(), resources.getString(R.string.product_delete_success_message), Toast.LENGTH_SHORT).show()
        getProductListFromFireStore()
    }

    private fun showAlertDialogToDeleteProduct(productID: String) {

        val builder = AlertDialog.Builder(requireActivity())

        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->

            showProgressDialog()

            FireStoreClass().deleteProduct(this@MyProductsFragment, productID)

            dialogInterface.dismiss()
        }

        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->

            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}