package com.example.fotozabawa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.fotozabawa.databinding.FragmentMenuBinding
import androidx.fragment.app.Fragment
import kotlinx.coroutines.NonDisposableHandle.parent


class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val tryby = arrayOf("1 zdjęcie", "2 zdjęcia", "3 zdjęcia", "4 zdjęcia", "5 zdjęć")
    private val czas = arrayOf("1 sekunda", "3 sekundy", "5 sekund", "10 sekund")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //----------------------lista(nie działa hehe)--------------------
//        val spinner_tryb = view.findViewById<Spinner>(R.id.spinner_tryb)
//        spinner_tryb?.adapter=
//            activity?.let { ArrayAdapter<String>(it.applicationContext,R.layout.fragment_menu, tryby) }
//        spinner_tryb?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//
//                //tryby[p2]
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//
//            }
//
//        }
//        val spinner_czas = view.findViewById<Spinner>(R.id.spinner_czas)

        val myButton = view.findViewById<Button>(R.id.button_start)
        myButton.setOnClickListener{
            val fragment : Fragment = StronaGlownaFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayout, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            requireActivity().title = "Strona Główna"
        }
    }
}