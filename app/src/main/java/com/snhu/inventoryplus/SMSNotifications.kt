package com.snhu.inventoryplus

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.snhu.inventoryplus.databinding.SmsNotificationsBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SMSNotifications : Fragment() {

    private var _binding: SmsNotificationsBinding? = null
    private val requestSmsPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                binding.textSmsStatus.text = getString(R.string.sms_permission_granted)
            } else {
                binding.textSmsStatus.text = getString(R.string.sms_permission_denied)
            }
        }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = SmsNotificationsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateSmsPermissionStatus()

        binding.buttonRequestSmsPermission.setOnClickListener {
            checkSmsPermissionBeforeSending()
        }

        binding.buttonSkipSms.setOnClickListener {
            findNavController().navigate(R.id.action_SMSNotificationsFragment_to_InventoryFragment)
        }
    }

    private fun checkSmsPermissionBeforeSending() {
        if (hasSmsPermission()) {
            binding.textSmsStatus.text = getString(R.string.sms_permission_granted)
        } else {
            binding.textSmsStatus.text = getString(R.string.sms_permission_requesting)
            requestSmsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
        }
    }

    private fun updateSmsPermissionStatus() {
        binding.textSmsStatus.text = if (hasSmsPermission()) {
            getString(R.string.sms_permission_granted)
        } else {
            getString(R.string.sms_permission_required)
        }
    }

    private fun hasSmsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
