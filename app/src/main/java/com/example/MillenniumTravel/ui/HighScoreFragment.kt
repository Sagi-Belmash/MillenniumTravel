package com.example.MillenniumTravel.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.MillenniumTravel.R
import com.example.MillenniumTravel.interfaces.Callback_HighScoreClicked
import com.example.MillenniumTravel.models.HighScore
import com.example.MillenniumTravel.utilities.Constants
import com.example.MillenniumTravel.utilities.SharedPreferencesManager
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class HighScoreFragment : Fragment() {
    companion object {
        private lateinit var hs_BTN_items: Array<MaterialButton>
        private var coordinates : MutableList<LatLng> = mutableListOf()
        var highScoreItemClicked: Callback_HighScoreClicked? = null
    }

    object HighScoreClickListener : View.OnClickListener {
        override fun onClick(v: View?) {
            val index: Int = hs_BTN_items.indexOf(v)
            val lat: Double = coordinates[index].latitude
            val lon: Double = coordinates[index].longitude

            highScoreItemClicked?.highScoreItemClicked(lat, lon)
        }
    }

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
        val sp = SharedPreferencesManager.getInstance()
        val jsonString = sp.getString(Constants.SP_KEYS.HIGHSCORES_KEY, "[]")
        val type = object : TypeToken<List<HighScore>>() {}.type
        val savedHighScores: List<HighScore> = Gson().fromJson(jsonString, type)

        var scoreString: String
        for (i in (0..<savedHighScores.size)) {
            scoreString = if (savedHighScores[i].score < 10) "${savedHighScores[i].score * 100}m" else "${savedHighScores[i].score.toFloat() / 10}km"
            hs_BTN_items[i].text = scoreString
            hs_BTN_items[i].visibility = View.VISIBLE
            hs_BTN_items[i].setOnClickListener(HighScoreClickListener)
            coordinates.add(savedHighScores[i].location)
        }
    }
}