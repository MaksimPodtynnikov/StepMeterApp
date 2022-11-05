package ru.podtynnikov.stepmeter.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.podtynnikov.stepmeter.PreferenceAdapter
import ru.podtynnikov.stepmeter.R

class SettingsFragment:Fragment(R.layout.fragment_settings) {
    private var pref: PreferenceAdapter? =null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val save=view.findViewById<Button>(R.id.save)
        val targetStepsView = view.findViewById<TextView>(R.id.targetSteps)
        val month= view.findViewById<RadioButton>(R.id.monthPeriod)
        val week= view.findViewById<RadioButton>(R.id.weekPeriod)
        pref= this.context?.let { PreferenceAdapter(it) }
        if(pref!!.period==30) {
            month?.isChecked=true
        }
        month.setOnClickListener{
            pref!!.period = 30
        }
        week.setOnClickListener{
            pref!!.period = 7
        }
        targetStepsView.text = pref!!.targetSteps.toString()
        save.setOnClickListener{
            pref!!.targetSteps = targetStepsView.text.toString().toInt()
        }
    }

}