package com.snhu.inventoryplus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.snhu.inventoryplus.databinding.LoginBinding

class Login : Fragment() {

    private var _binding: LoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: InventoryDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseHelper = InventoryDatabaseHelper(requireContext())

        binding.buttonSignIn.setOnClickListener {
            signIn()
        }

        binding.buttonCreateAccount.setOnClickListener {
            createAccount()
        }
    }

    private fun signIn() {
        val credentials = validateCredentials() ?: return

        if (databaseHelper.authenticateUser(credentials.username, credentials.password)) {
            openInventoryDashboard()
        } else {
            binding.inputPassword.error = getString(R.string.invalid_username_or_password)
        }
    }

    private fun createAccount() {
        val credentials = validateCredentials() ?: return

        if (databaseHelper.createUser(credentials.username, credentials.password)) {
            openInventoryDashboard()
        } else {
            binding.inputUsername.error = getString(R.string.username_taken)
        }
    }

    private fun validateCredentials(): Credentials? {
        val username = binding.editTextUsername.text?.toString()?.trim().orEmpty()
        val password = binding.editTextPassword.text?.toString().orEmpty()

        binding.inputUsername.error = null
        binding.inputPassword.error = null

        var isValid = true
        if (username.isEmpty()) {
            binding.inputUsername.error = getString(R.string.username_required)
            isValid = false
        }

        if (password.isEmpty()) {
            binding.inputPassword.error = getString(R.string.password_required)
            isValid = false
        }

        return if (isValid) Credentials(username, password) else null
    }

    private fun openInventoryDashboard() {
        findNavController().navigate(R.id.action_LoginFragment_to_InventoryFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private data class Credentials(
        val username: String,
        val password: String
    )
}
