package com.snhu.inventoryplus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.snhu.inventoryplus.databinding.AddInventoryBinding

class AddInventory : Fragment() {

    private var _binding: AddInventoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: InventoryDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseHelper = InventoryDatabaseHelper(requireContext())

        binding.buttonSaveItem.setOnClickListener {
            saveItem()
        }

        binding.buttonCancelAddItem.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun saveItem() {
        val itemName = binding.editTextItemName.text?.toString()?.trim().orEmpty()
        val quantityText = binding.editTextQuantity.text?.toString()?.trim().orEmpty()
        val lowAlertText = binding.editTextLowAlert.text?.toString()?.trim().orEmpty()

        binding.inputItemName.error = null
        binding.inputQuantity.error = null
        binding.inputLowAlert.error = null

        var isValid = true
        if (itemName.isEmpty()) {
            binding.inputItemName.error = getString(R.string.item_name_required)
            isValid = false
        }

        val quantity = quantityText.toIntOrNull()
        if (quantityText.isEmpty()) {
            binding.inputQuantity.error = getString(R.string.quantity_required)
            isValid = false
        } else if (quantity == null) {
            binding.inputQuantity.error = getString(R.string.quantity_must_be_number)
            isValid = false
        }

        val lowAlert = lowAlertText.toIntOrNull()
        if (lowAlertText.isEmpty()) {
            binding.inputLowAlert.error = getString(R.string.low_alert_required)
            isValid = false
        } else if (lowAlert == null) {
            binding.inputLowAlert.error = getString(R.string.low_alert_must_be_number)
            isValid = false
        }

        if (!isValid || quantity == null || lowAlert == null) {
            return
        }

        databaseHelper.insertInventoryItem(itemName, quantity, lowAlert)
        SMSNotificationSender.sendLowInventoryAlert(requireActivity(), itemName, quantity, lowAlert)
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
