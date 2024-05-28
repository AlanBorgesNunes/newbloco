package com.app.newblocodenotas.uis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.newblocodenotas.R
import com.app.newblocodenotas.databinding.FragmentLoginBinding
import com.app.newblocodenotas.models.User
import com.app.newblocodenotas.utils.UiState
import com.app.newblocodenotas.utils.toast
import com.app.newblocodenotas.viewModels.ViewModelAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModelAuth: ViewModelAuth by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        init()
        login()
        observer()
        return binding.root
    }

    private fun init() {
        binding.tvCriarConta.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_signUpFragment
            )
        }
    }

    private fun login(){
        binding.login.setOnClickListener {
            if (validate()){
                viewModelAuth.authUser(
                    userObj()
                )
            }
        }
    }

    private fun userObj(): User {
        return User(
            email = binding.edtEmailLogin.text.toString(),
            senha = binding.edtSenhaLogin.text.toString()
        )
    }

    private fun validate(): Boolean{
        var valid = false
        if (binding.edtEmailLogin.text.toString().isBlank() ||
            binding.edtSenhaLogin.text.toString().isBlank()){
            valid = false
            toast("Campos de texto em branco!")
        }else{
            valid = true
        }
        return valid
    }

    private fun observer(){
        viewModelAuth.authUser.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    binding.progressLogin.visibility = View.GONE
                    binding.txtEntrar.visibility = View.VISIBLE
                    toast(state.error)
                }
                UiState.Loading -> {
                    binding.txtEntrar.visibility = View.GONE
                    binding.progressLogin.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    binding.progressLogin.visibility = View.GONE
                    binding.txtEntrar.visibility = View.VISIBLE
                    findNavController().navigate(
                        R.id.action_loginFragment_to_homeFragment
                    )
                }
            }
        }
    }
}