package com.android.compose.ui

import android.os.Bundle
import android.view.View
import com.android.compose.R
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.android.compose.other.Status
import com.bumptech.glide.RequestManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class AddShoppingItemFragment(private val glide: RequestManager) :
    Fragment(R.layout.fragment_add_shopping_item) {
    lateinit var navController: NavController
    lateinit var viewModel: ShoppingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!::viewModel.isInitialized) {
//            viewModel = ViewModelProvider(owner = requireActivity())[ShoppingViewModel::class.java]
        }
        observer()

        val etShoppingItemName = view.findViewById<TextInputEditText>(R.id.etShoppingItemName)
        val etShoppingItemAmount = view.findViewById<TextInputEditText>(R.id.etShoppingItemAmount)
        val etShoppingItemPrice = view.findViewById<TextInputEditText>(R.id.etShoppingItemPrice)

        val btnAddShoppingItem = view.findViewById<MaterialButton>(R.id.btnAddShoppingItem)

        btnAddShoppingItem.setOnClickListener {
            viewModel.insertShoppingItem(
                name = etShoppingItemName.text.toString(),
                amountString = etShoppingItemAmount.text.toString(),
                priceString = etShoppingItemPrice.text.toString()
            )
        }

        view.findViewById<ImageView>(R.id.ivImageView).setOnClickListener {
            navController.navigate(
                AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
            )
        }

        val callback = object : OnBackPressedCallback(enabled = true) {
            override fun handleOnBackPressed() {
                viewModel.setImageUrl(url = "")
                navController.popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback = callback)
    }

    private fun observer() {
        val imageView = requireView().findViewById<ImageView>(R.id.ivImageView)
        viewModel.imageUrl.observe(viewLifecycleOwner) {
            glide.load(it).into(imageView)
        }
        viewModel.insertShoppingItemStatus.observe(viewLifecycleOwner) {
            it.getContent()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        Snackbar.make(
                            requireView().rootView,
                            "Added Shopping Item",
                            Snackbar.LENGTH_LONG
                        ).show()
                        navController.popBackStack()
                    }

                    Status.ERROR -> {
                        Snackbar.make(
                            requireView().rootView,
                            it.getContent()?.error ?: "error occurred",
                            Snackbar.LENGTH_LONG
                        ).show()
                        navController.popBackStack()
                    }

                    Status.LOADING -> {

                    }
                }
            }
        }
    }

}
