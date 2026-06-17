package com.snhu.inventoryplus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.snhu.inventoryplus.databinding.InventoryDashboardBinding

class InventoryDashboard : Fragment() {

    private var _binding: InventoryDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: InventoryDatabaseHelper
    private lateinit var inventoryAdapter: InventoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = InventoryDashboardBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseHelper = InventoryDatabaseHelper(requireContext())
        inventoryAdapter = InventoryAdapter(
            onItemClick = { item -> openEditItem(item.id) },
            onDeleteClick = { item -> deleteInventoryItem(item) }
        )

        binding.recyclerInventory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = inventoryAdapter
        }

        binding.buttonOpenAddItem.setOnClickListener {
            findNavController().navigate(R.id.action_InventoryFragment_to_AddInventoryFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        if (_binding != null) {
            displayInventoryItems()
        }
    }

    private fun displayInventoryItems() {
        val items = databaseHelper.getAllInventoryItems()
        inventoryAdapter.submitList(items)
        binding.recyclerInventory.isVisible = items.isNotEmpty()
        binding.textInventoryEmpty.isVisible = items.isEmpty()
    }

    private fun deleteInventoryItem(item: InventoryItem) {
        databaseHelper.deleteInventoryItem(item.id)
        displayInventoryItems()
        Snackbar.make(
            binding.root,
            getString(R.string.inventory_item_deleted),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun openEditItem(itemId: Long) {
        val arguments = Bundle().apply {
            putLong(EditInventory.ARG_ITEM_ID, itemId)
        }
        findNavController().navigate(
            R.id.action_InventoryFragment_to_EditInventoryFragment,
            arguments
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerInventory.adapter = null
        _binding = null
    }
}
