package com.app.newblocodenotas.uis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.newblocodenotas.R
import com.app.newblocodenotas.databinding.FragmentSignUpBinding
import com.app.newblocodenotas.models.User
import com.app.newblocodenotas.utils.UiState
import com.app.newblocodenotas.utils.toast
import com.app.newblocodenotas.viewModels.ViewModelAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModelAuth: ViewModelAuth by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        navigateLogin()
        observer()
        binding.btnCadastrar.setOnClickListener {
            if (verifique()){
              viewModelAuth.createUSer(
                  userObj()
              )
            }
        }

        return binding.root
    }

    private fun userObj():User{
        return  User(
            email = binding.edtEmail.text.toString(),
            senha = binding.edtSenha.text.toString(),
        )
    }

    private fun verifique():Boolean{
        var verific = false
        if (binding.edtEmail.text.toString().isBlank() ||
            binding.edtSenha.text.toString().isBlank()){
            verific = false
            toast("Campos vazios!")
        }else{
            verific = true
        }

        return verific
    }

    private fun observer(){
        viewModelAuth.createUser.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    binding.txtCadastrar.visibility = View.VISIBLE
                    binding.progressSignup.visibility = View.GONE
                    toast(state.error)
                }
                UiState.Loading -> {
                    binding.txtCadastrar.visibility = View.GONE
                    binding.progressSignup.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    binding.txtCadastrar.visibility = View.VISIBLE
                    binding.progressSignup.visibility = View.GONE
                    findNavController().navigate(
                        R.id.action_signUpFragment_to_homeFragment
                    )
                    toast(state.toString())
                }
            }
        }
    }

    private fun navigateLogin(){
        binding.tvJaTemConta.setOnClickListener {
            findNavController().navigate(
                R.id.action_signUpFragment_to_loginFragment
            )
        }
    }

}