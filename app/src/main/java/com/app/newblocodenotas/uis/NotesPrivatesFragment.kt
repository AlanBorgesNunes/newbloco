package com.app.newblocodenotas.uis

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.newblocodenotas.R
import com.app.newblocodenotas.adapters.AdapterNotas
import com.app.newblocodenotas.databinding.FragmentNotesPrivatesBinding
import com.app.newblocodenotas.models.Anotation
import com.app.newblocodenotas.utils.UiState
import com.app.newblocodenotas.utils.createDialog
import com.app.newblocodenotas.utils.recyclerInit
import com.app.newblocodenotas.utils.toast
import com.app.newblocodenotas.viewModels.viewModelAnotation
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesPrivatesFragment : Fragment(), AdapterNotas.ClickDelete, AdapterNotas.ClickEdit {
    private lateinit var binding: FragmentNotesPrivatesBinding
    private val viewModelAnotation: viewModelAnotation by viewModels()
    private val list: ArrayList<Anotation> = arrayListOf()

    private lateinit var dialog: Dialog
    private lateinit var linearLayout: LinearLayout

    private lateinit var receiveNota: TextView
    private lateinit var progress: ProgressBar
    private lateinit var btnSim: Button
    private lateinit var btnNao: Button

    private val adapter by lazy {
        AdapterNotas(
            list,
            requireContext(),
            this,
            this
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesPrivatesBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        backHome()
        getNotesPrivates()
        createFirstNotePrivate()
        observer()
        return binding.root
    }

    private fun observer(){
        viewModelAnotation.getNotePrivate.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    binding.progressNotesPrivate.visibility = View.GONE
                    binding.llNotesEmpty.visibility = View.VISIBLE
                    binding.rvNotesPrivates.visibility = View.GONE
                   toast(state.error)
                }
                UiState.Loading -> {
                    list.clear()
                    binding.progressNotesPrivate.visibility = View.VISIBLE
                    binding.llNotesEmpty.visibility = View.GONE
                    binding.rvNotesPrivates.visibility = View.GONE
                }
                is UiState.Success -> {
                    list.clear()
                    binding.progressNotesPrivate.visibility = View.GONE
                    binding.llNotesEmpty.visibility = View.GONE
                    binding.rvNotesPrivates.visibility = View.VISIBLE

                    list.addAll(state.data)
                    recyclerInit(binding.rvNotesPrivates, adapter)

                }
            }
        }

        viewModelAnotation.deleteNote.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    progress.visibility = View.GONE
                    linearLayout.visibility = View.VISIBLE
                    toast(state.error)
                }
                UiState.Loading -> {
                    progress.visibility = View.VISIBLE
                    linearLayout.visibility = View.GONE
                }
                is UiState.Success -> {
                    progress.visibility = View.GONE
                    linearLayout.visibility = View.VISIBLE
                    dialog.dismiss()
                    toast(state.data)
                }
            }
        }
    }

    private fun getNotesPrivates(){
        viewModelAnotation.getNotePrivate(
            FirebaseAuth.getInstance().currentUser?.uid!!
        )
    }

    private fun backHome() {
        binding.backHome.setOnClickListener {
            findNavController().navigate(
                R.id.action_notesPrivatesFragment_to_homeFragment
            )
        }
    }

    private fun createFirstNotePrivate(){
        binding.createFisrtNotePrivate.setOnClickListener {
            findNavController().navigate(
                R.id.action_notesPrivatesFragment_to_createNoteFragment
            )
        }
    }

    override fun clickDelete(anotation: Anotation) {
        dialog = requireContext().createDialog(R.layout.dialog_delete_nota, true)
        receiveNota = dialog.findViewById<TextView>(R.id.dialog_receive_nota)
        progress = dialog.findViewById<ProgressBar>(R.id.progress_dialog_delete)
        linearLayout = dialog.findViewById<LinearLayout>(R.id.ll_sim_or_nao)
        btnSim = dialog.findViewById<Button>(R.id.btn_dialog_delete_sim)
        btnNao = dialog.findViewById<Button>(R.id.btn_dialog_delete_nao)

        receiveNota.text = "${anotation.anotation}..."

        btnSim.setOnClickListener {
            viewModelAnotation.deleteNote(
                FirebaseAuth.getInstance().currentUser?.uid!!,
                anotation
            )
        }

        btnNao.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun clickEdite(anotation: Anotation) {
        TODO("Not yet implemented")
    }


}