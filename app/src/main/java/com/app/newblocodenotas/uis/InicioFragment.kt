package com.app.newblocodenotas.uis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.app.newblocodenotas.R
import com.app.newblocodenotas.adapters.SliderAdapter
import com.app.newblocodenotas.databinding.FragmentInicioBinding
import com.app.newblocodenotas.models.SliderItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InicioFragment : Fragment() {
    private lateinit var binding: FragmentInicioBinding
    val slideItems: MutableList<SliderItem> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInicioBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        init()
        return binding.root
    }

    private fun init() {

        slideItems.add(SliderItem(R.drawable.ic_slide1))
        slideItems.add(SliderItem(R.drawable.ic_slide2ui))
        slideItems.add(SliderItem(R.drawable.ic_slide3hjjj))

        binding.viewPagerImage.adapter = SliderAdapter(slideItems, binding.viewPagerImage)

        binding.btnCriarConta.setOnClickListener {
            findNavController().navigate(
                R.id.action_inicioFragment_to_signUpFragment
            )
        }

        binding.entrar.setOnClickListener {
            findNavController().navigate(
                R.id.action_inicioFragment_to_loginFragment
            )
        }

    }

}