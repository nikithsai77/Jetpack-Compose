package com.android.compose.ui

import com.android.compose.R
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.compose.adapters.ImageAdapter
import javax.inject.Inject

class ImagePickFragment @Inject constructor(val imageAdapter: ImageAdapter) :
    Fragment(R.layout.fragment_image_pick) {
    lateinit var navController: NavController
    lateinit var viewModel: ShoppingViewModel
    lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvImages)
        imageAdapter.setOnItemClickListener {
            viewModel.setImageUrl(it)
            navController.popBackStack()
        }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireActivity(), 4)
            adapter = imageAdapter
        }
    }

}
