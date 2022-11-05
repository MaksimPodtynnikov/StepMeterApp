package ru.podtynnikov.stepmeter.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.progressindicator.CircularProgressIndicator
import ru.podtynnikov.stepmeter.PreferenceAdapter
import ru.podtynnikov.stepmeter.R


class HomeFragment:Fragment(R.layout.fragment_home) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref= this.context?.let { PreferenceAdapter(it) }
        if (pref != null) {
            setData(pref.daySteps,pref.targetSteps)
        }
    }

    @SuppressLint("SetTextI18n")
    fun setData(daySteps:Int,targetSteps:Int)
    {
        val tvStepsTaken = view?.findViewById<TextView>(R.id.stepsCount)
        val kkal = view?.findViewById<TextView>(R.id.kkalCount)
        val km = view?.findViewById<TextView>(R.id.kmCount)
        if (tvStepsTaken != null && kkal!= null && km!=null) {
            tvStepsTaken.text = ("${daySteps}/${targetSteps}")
            kkal.text =  String.format("%.1f",(daySteps.times(0.05))) + " ккал"
            km.text =  (daySteps.times(2)).toString() + " м"
            view?.findViewById<CircularProgressIndicator>(R.id.progress)?.setProgressCompat(
                (daySteps.toFloat() / targetSteps.toFloat() * 100).toInt(),
                true
            )
        }
    }





}