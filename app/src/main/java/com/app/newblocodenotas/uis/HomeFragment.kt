package com.app.newblocodenotas.uis

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.newblocodenotas.R
import com.app.newblocodenotas.adapters.AdapterNotas
import com.app.newblocodenotas.databinding.FragmentHomeBinding
import com.app.newblocodenotas.models.Anotation
import com.app.newblocodenotas.utils.RequestConfirme
import com.app.newblocodenotas.utils.UiState
import com.app.newblocodenotas.utils.authenticateWithDevicePassword
import com.app.newblocodenotas.utils.toast
import com.app.newblocodenotas.viewModels.viewModelAnotation
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModelAnotation: viewModelAnotation by viewModels()
    private val list: ArrayList<Anotation> = arrayListOf<Anotation>()

    private val adapterNote by lazy {
        AdapterNotas(
            list
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
                    binding.progressList.visibility = View.GONE
                    binding.rvHome.visibility = View.VISIBLE
                    binding.tvMensagem.visibility = View.GONE

                    list.addAll(state.data)
                    initRecycler(binding.rvHome)
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

    private fun initRecycler(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapterNote
    }

    private fun novaNota() {
        binding.novaNota.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_createNoteFragment
            )
        }
    }

    private fun showMenu(){
        val menu = PopupMenu(requireContext(), binding.menuSettings)
        menu.inflate(R.menu.menu_home)
        menu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.note_private_menu->{

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

                R.id.sigOut_menu->{
                    true
                }

                else->{false}
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


}