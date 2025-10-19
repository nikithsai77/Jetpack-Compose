package com.android.compose.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.compose.R
import com.android.compose.adapters.ShoppingItemAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView

class ShoppingFragment(
    val shoppingItemAdapter: ShoppingItemAdapter,
    var viewModel: ShoppingViewModel? = null
) : Fragment(), View.OnClickListener {
    private lateinit var layoutView: View
    lateinit var testNavController: NavController
    private lateinit var fab: FloatingActionButton
    private lateinit var rvShoppingItems: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = viewModel ?: ViewModelProvider(owner = requireActivity())[ShoppingViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutView = inflater.inflate(R.layout.fragment_shopping, container, false)
        fab = layoutView.findViewById(R.id.fabAddShoppingItem)
        fab.setOnClickListener(this@ShoppingFragment)
        return layoutView
    }

    private fun subscribeToObservers() {
        val tvShoppingItemPrice = view?.findViewById<MaterialTextView>(R.id.tvShoppingItemPrice)
        viewModel?.shoppingItems?.observe(viewLifecycleOwner, Observer {
            shoppingItemAdapter.shoppingItems = it
            shoppingItemAdapter.notifyDataSetChanged()
        })
        viewModel?.totalPrice?.observe(viewLifecycleOwner, Observer {
            val price = it ?: 0f
            val priceText = "Total Price: $price€"
            tvShoppingItemPrice?.text = priceText
        })
    }

    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
        0, LEFT or RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(
            viewHolder: RecyclerView.ViewHolder,
            direction: Int
        ) {
            val pos = viewHolder.layoutPosition
            val item = shoppingItemAdapter.shoppingItems[pos]
            viewModel?.deleteShoppingItem(shoppingItem = item)
            Snackbar.make(
                requireView(),
                "Successfully Deleted Item",
                Snackbar.LENGTH_LONG
            ).apply {
                setAction("Undo") {
                    viewModel?.insertShoppingItemIntoDb(shoppingItem = item)
                }
            }.show()
        }

    }

    private fun setupRecyclerView() {
        rvShoppingItems = view?.findViewById<RecyclerView>(R.id.rvShoppingItems)!!
        rvShoppingItems.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
            adapter = shoppingItemAdapter
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == fab.id) {
            testNavController.navigate(
                directions = ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment()
            )
        }
    }

}
