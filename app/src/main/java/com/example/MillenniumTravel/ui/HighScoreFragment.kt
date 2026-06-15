package com.example.MillenniumTravel.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.MillenniumTravel.R
import com.example.MillenniumTravel.interfaces.Callback_HighScoreClicked
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText


class HighScoreFragment : Fragment() {

    private lateinit var hs_BTN_items: Array<MaterialButton>
    private lateinit var coordinatesStrings : Array<String> // Delete later
    // private lateinit var coordinates : List<List<String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.fragment_high_score,
            container,
            false
        )
        findViews(view)
        initViews()

        return view
    }

    private fun findViews(view: View) {
        hs_BTN_items = arrayOf(
            view.findViewById(R.id.hs_BTN_0),
            view.findViewById(R.id.hs_BTN_1),
            view.findViewById(R.id.hs_BTN_2),
            view.findViewById(R.id.hs_BTN_3),
            view.findViewById(R.id.hs_BTN_4),
            view.findViewById(R.id.hs_BTN_5),
            view.findViewById(R.id.hs_BTN_6),
            view.findViewById(R.id.hs_BTN_7),
            view.findViewById(R.id.hs_BTN_8),
            view.findViewById(R.id.hs_BTN_9)
        )
    }

    private fun initViews() {
        object a: View.OnClickListener {
            override fun onClick(p0: View?) {
                var coordinates = coordinatesStrings[i].split(",")
                var lat: Double = coordinates?.getOrNull(0)?.toDoubleOrNull() ?: 0.0
                var lng: Double = coordinates?.getOrNull(1)?.toDoubleOrNull() ?: 0.0
            }

        }
        for (i in (0..9)) {
            hs_BTN_items[i].setOnClickListener
        }

        { v ->

        }
    }
}