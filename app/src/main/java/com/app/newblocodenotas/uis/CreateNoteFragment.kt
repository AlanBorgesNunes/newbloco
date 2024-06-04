package com.app.newblocodenotas.uis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.newblocodenotas.R
import com.app.newblocodenotas.databinding.FragmentCreateNoteBinding
import com.app.newblocodenotas.models.Anotation
import com.app.newblocodenotas.utils.UiState
import com.app.newblocodenotas.utils.toast
import com.app.newblocodenotas.viewModels.ViewModelAdmob
import com.app.newblocodenotas.viewModels.viewModelAnotation
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class CreateNoteFragment : Fragment() {
    private lateinit var binding: FragmentCreateNoteBinding
    private val viewModelAnotation: viewModelAnotation by viewModels()
    private val viewModelAdmob: ViewModelAdmob by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateNoteBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }

        viewModelAdmob.inter(requireContext())
        backToHome()
        observer()
        binding.btnSalvarAnotacao.setOnClickListener {
            if (validate()){
                viewModelAnotation.newNote(
                    FirebaseAuth.getInstance().currentUser?.uid!!,
                    getSelectedRadioButtonText(binding.radioGroup),
                    anotationObj()
                )
            }

        }

        return binding.root
    }

    private fun observer(){
        viewModelAnotation.newNote.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    binding.tvSalvar.visibility = View.VISIBLE
                    binding.progressSalvar.visibility = View.GONE
                   toast(state.error)
                }
                UiState.Loading -> {
                    binding.tvSalvar.visibility = View.GONE
                    binding.progressSalvar.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    binding.tvSalvar.visibility = View.VISIBLE
                    binding.progressSalvar.visibility = View.GONE
                    viewModelAdmob.interShow(requireActivity())
                    toast(state.data)
                }
            }
        }
    }

    private fun anotationObj(): Anotation{
        return Anotation(
            title = binding.titleNote.text.toString(),
            anotation = binding.descricaoNote.text.toString(),
            privateOrPublic = getSelectedRadioButtonText(binding.radioGroup),
            id = Date().time.toString()
        )
    }

    private fun validate():Boolean{
        var valid = false

        if (binding.titleNote.text.toString().isBlank() ||
            binding.descricaoNote.text.toString().isBlank()){
            valid = false
            toast("Campos em branco!")
        }else{
            valid = true
        }

        return valid
    }

    private fun getSelectedRadioButtonText(radioGroup: RadioGroup): String {
        val selectedRadioButtonId = radioGroup.checkedRadioButtonId

        if (selectedRadioButtonId == -1) {
            return ""
        }
        val selectedRadioButton = radioGroup.findViewById<RadioButton>(selectedRadioButtonId)
        return selectedRadioButton.text.toString()
    }

    private fun backToHome(){
        binding.backToHome.setOnClickListener {
            findNavController().navigate(
                R.id.action_createNoteFragment_to_homeFragment
            )
        }
    }
}