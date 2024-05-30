package com.app.newblocodenotas.uis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.newblocodenotas.R
import com.app.newblocodenotas.databinding.FragmentNotesPrivatesBinding


class NotesPrivatesFragment : Fragment() {
    private lateinit var binding: FragmentNotesPrivatesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesPrivatesBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }

        return binding.root
    }


}