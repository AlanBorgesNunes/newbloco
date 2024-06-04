package com.app.newblocodenotas.uis

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.newblocodenotas.R
import com.app.newblocodenotas.adapters.AdapterNotas
import com.app.newblocodenotas.databinding.FragmentHomeBinding
import com.app.newblocodenotas.models.Anotation
import com.app.newblocodenotas.utils.RequestConfirme
import com.app.newblocodenotas.utils.UiState
import com.app.newblocodenotas.utils.authenticateWithDevicePassword
import com.app.newblocodenotas.utils.createDialog
import com.app.newblocodenotas.utils.recyclerInit
import com.app.newblocodenotas.utils.toast
import com.app.newblocodenotas.viewModels.ViewModelAuth
import com.app.newblocodenotas.viewModels.viewModelAnotation
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), AdapterNotas.ClickDelete, AdapterNotas.ClickEdit {
    private lateinit var binding: FragmentHomeBinding
    private val viewModelAnotation: viewModelAnotation by viewModels()
    private val viewModelAuth: ViewModelAuth by viewModels()
    private val list: ArrayList<Anotation> = arrayListOf<Anotation>()

    private lateinit var titleUpdade: EditText
    private lateinit var noteUpdade: EditText
    private lateinit var llUpdade: LinearLayout
    private lateinit var btnSaveUpdade: Button
    private lateinit var btnSairUpdade: Button
    private lateinit var progressUpdade: ProgressBar


    private lateinit var dialogDelete: Dialog
    private lateinit var dialogEdite: Dialog
    private lateinit var linearLayout: LinearLayout

    private lateinit var receiveNota: TextView
    private lateinit var progress: ProgressBar
    private lateinit var btnSim: Button
    private lateinit var btnNao: Button

    private val adapterNote by lazy {
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
        binding = FragmentHomeBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        progressColor()
        viewModelAnotation.getNote(
            FirebaseAuth.getInstance().currentUser?.uid!!
        )

        binding.menuSettings.setOnClickListener {
            showMenu()
        }
        observer()
        novaNota()
        return binding.root
    }

    private fun observer() {
        viewModelAnotation.getNote.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> {
                    binding.progressList.visibility = View.GONE
                    binding.rvHome.visibility = View.GONE
                    binding.tvMensagem.visibility = View.VISIBLE
                    toast(state.error)
                }

                UiState.Loading -> {
                    list.clear()
                    binding.progressList.visibility = View.VISIBLE
                    binding.rvHome.visibility = View.GONE
                    binding.tvMensagem.visibility = View.GONE
                }

                is UiState.Success -> {
                    list.clear()
                    binding.progressList.visibility = View.GONE
                    binding.rvHome.visibility = View.VISIBLE
                    binding.tvMensagem.visibility = View.GONE

                    list.addAll(state.data)
                    recyclerInit(binding.rvHome, adapterNote)
                }
            }
        }
        viewModelAnotation.deleteNote.observe(viewLifecycleOwner) { state ->
            when (state) {
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
                    dialogDelete.dismiss()
                    toast(state.data)
                }
            }
        }

        viewModelAnotation.updateNote.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    progressUpdade.visibility = View.GONE
                    llUpdade.visibility = View.VISIBLE
                    toast(state.error)
                }
                UiState.Loading -> {
                    progressUpdade.visibility = View.VISIBLE
                    llUpdade.visibility = View.GONE
                }
                is UiState.Success -> {
                    progressUpdade.visibility = View.GONE
                    llUpdade.visibility = View.VISIBLE
                    dialogEdite.dismiss()
                    toast(state.data)
                }
            }
        }
    }


    private fun progressColor() {
        binding.progressList.progressTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.black_fosco
            )
        )
    }


    private fun novaNota() {
        binding.novaNota.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_createNoteFragment
            )
        }
    }

    private fun showMenu() {
        val menu = PopupMenu(requireContext(), binding.menuSettings)
        menu.inflate(R.menu.menu_home)
        menu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.note_private_menu -> {

                    authenticateWithDevicePassword(
                        onSuccess = {
                            findNavController().navigate(
                                R.id.action_homeFragment_to_notesPrivatesFragment
                            )
                        },
                        onFailure = {
                            toast("Falha ao tentar acessar notas privadas!")
                        }
                    )

                    true
                }

                R.id.sigOut_menu -> {
                    viewModelAuth.logout {
                        findNavController().navigate(
                            R.id.action_homeFragment_to_loginFragment
                        )
                    }
                    true
                }

                else -> {
                    false
                }
            }
        }
        menu.show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestConfirme.REQUEST_CONFIRM_DEVICE_CREDENTIALS) {
            if (resultCode == Activity.RESULT_OK) {
                findNavController().navigate(
                    R.id.action_homeFragment_to_notesPrivatesFragment
                )
            } else {
                toast("Falha na autenticação")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun clickDelete(anotation: Anotation) {
        dialogDelete = requireContext().createDialog(R.layout.dialog_delete_nota, true)
        receiveNota = dialogDelete.findViewById<TextView>(R.id.dialog_receive_nota)
        progress = dialogDelete.findViewById<ProgressBar>(R.id.progress_dialog_delete)
        linearLayout = dialogDelete.findViewById<LinearLayout>(R.id.ll_sim_or_nao)
        btnSim = dialogDelete.findViewById<Button>(R.id.btn_dialog_delete_sim)
        btnNao = dialogDelete.findViewById<Button>(R.id.btn_dialog_delete_nao)

        receiveNota.text = "${anotation.anotation}..."

        btnSim.setOnClickListener {
            viewModelAnotation.deleteNote(
                FirebaseAuth.getInstance().currentUser?.uid!!,
                anotation
            )
        }

        btnNao.setOnClickListener {
            dialogDelete.dismiss()
        }

        dialogDelete.show()
    }

    override fun clickEdite(anotation: Anotation) {
        dialogEdite = requireContext().createDialog(R.layout.dialog_edite_note, true)
        titleUpdade = dialogEdite.findViewById(R.id.title_update)
        noteUpdade = dialogEdite.findViewById(R.id.note_update)
        llUpdade = dialogEdite.findViewById(R.id.ll_save_or_exit)
        progressUpdade = dialogEdite.findViewById(R.id.progress_update)
        btnSaveUpdade = dialogEdite.findViewById(R.id.btn_save_update)
        btnSairUpdade = dialogEdite.findViewById(R.id.btn_sair_update)

        titleUpdade.setText(anotation.title)
        noteUpdade.setText(anotation.anotation)


        btnSaveUpdade.setOnClickListener {

            if (titleUpdade.text.toString().isBlank() ||noteUpdade.text.toString().isBlank()){
                toast("Campos obrigatorios em branco!")
            }else{
                val hashMap = hashMapOf<String, Any>(
                    "title" to titleUpdade.text.toString(),
                    "anotation" to noteUpdade.text.toString(),
                    "privateOrPublic" to anotation.privateOrPublic!!,
                    "id" to anotation.id!!
                )

                viewModelAnotation.updateNote(
                    FirebaseAuth.getInstance().currentUser?.uid!!,
                    hashMap,
                    anotation
                )
            }
        }

        btnSairUpdade.setOnClickListener {
            dialogEdite.dismiss()
        }

        dialogEdite.show()
    }


}