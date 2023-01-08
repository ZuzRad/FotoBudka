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
import android.widget.TextView
import android.widget.Toast
import com.example.fotozabawa.databinding.FragmentMenuBinding
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MenuFragment : Fragment() {
    private lateinit var appDatabase: AppDatabase
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val tryby = arrayOf("1 zdjęcie", "2 zdjęcia", "3 zdjęcia", "6 zdjęć")
    private val czas = arrayOf("1 sekunda", "3 sekundy", "5 sekund", "10 sekund")
    private var tryb_selected = "1 zdjęcie"
    private var czas_selected = "1 sekunda"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        appDatabase = AppDatabase.getDatabase(requireContext())
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //----------------------lista--------------------
        val spinner_tryb = view.findViewById<Spinner>(R.id.spinner_tryb)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tryby)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_tryb.adapter = adapter
        spinner_tryb.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinner_tryb.setSelection(position)
                spinner_tryb.setPrompt(tryby[position])
                tryb_selected=tryby[position]

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val spinner_czas = view.findViewById<Spinner>(R.id.spinner_czas)
        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, czas)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_czas.adapter = adapter2
        spinner_czas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinner_czas.setSelection(position)
                spinner_czas.setPrompt(czas[position])
                czas_selected=czas[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //----------------------powrót z menu------------------

        val myButton = view.findViewById<Button>(R.id.button_start)
        myButton.setOnClickListener{

//            GlobalScope.launch(Dispatchers.IO) {
//                val ustawienie = Ustawienia(tryb_selected, czas_selected)
//                appDatabase.ustawieniaDao().insert(ustawienie)
//                val text = view.findViewById<TextView>(R.id.textView_tryb)
//                text.setText(appDatabase.ustawieniaDao().getAll().toString())
//            }

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