package ru.podtynnikov.stepmeter

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

class PreferenceAdapter(context: Context) {
    var period = 7
        get(): Int {
            if (sharedPreferences!!.contains("period")) period = sharedPreferences!!.getInt("period", 0)
            return field
        }
        set(period) {
            val editor = sharedPreferences!!.edit()
            if (editor != null) {
                editor.putInt("period", period)
                field = period
                editor.apply()
            }
        }
    var daySteps = 0
        get(): Int {
            if (sharedPreferences!!.contains("steps")) daySteps = sharedPreferences!!.getInt("steps", 0)
            return field
        }
        set(steps) {
            val editor = sharedPreferences!!.edit()
            if (editor != null) {
                editor.putInt("steps", steps)
                field = steps
                editor.apply()
            }
        }
    var previousSteps = 0
        get(): Int {
            if (sharedPreferences!!.contains("previousSteps")) previousSteps =
                sharedPreferences!!.getInt("previousSteps", 0)
            return field
        }
        set(previousSteps) {
            val editor = sharedPreferences!!.edit()
            if (editor != null) {
                editor.putInt("previousSteps", previousSteps)
                field = previousSteps
                editor.apply()
            }
        }
    var targetSteps = 6000
        get(): Int {
            if (sharedPreferences!!.contains("targetSteps")) targetSteps =
                sharedPreferences!!.getInt("targetSteps", 0)
            return field
        }
        set(targetSteps) {
            val editor = sharedPreferences!!.edit()
            if (editor != null) {
                editor.putInt("targetSteps", targetSteps)
                field = targetSteps
                editor.apply()
            }
        }
    var lastDate: LocalDate? = null
        @RequiresApi(Build.VERSION_CODES.O)
        get(): LocalDate? {
        if (sharedPreferences!!.contains("lastDate") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lastDate = LocalDate.parse(sharedPreferences!!.getString("lastDate", ""))
        }
        else field= LocalDate.now()
        return field
    }
    set(lastDate) {
        val editor = sharedPreferences!!.edit()
        if (editor != null) {
            editor.putString("lastDate", lastDate.toString())
            field = lastDate
            editor.apply()
        }
    }

    private var sharedPreferences: SharedPreferences?

    init {
        sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        if (sharedPreferences != null) {
            if (sharedPreferences!!.contains("steps")) daySteps =
                sharedPreferences!!.getInt("steps", 0)
            if (sharedPreferences!!.contains("targetSteps")) targetSteps =
                sharedPreferences!!.getInt("targetSteps", 0)
            if (sharedPreferences!!.contains("period")) period =
                sharedPreferences!!.getInt("period", 0)
            if (sharedPreferences!!.contains("previousSteps")) previousSteps =
                sharedPreferences!!.getInt("previousSteps", 0)
            if (sharedPreferences!!.contains("lastDate") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                lastDate = LocalDate.parse(sharedPreferences!!.getString("lastDate", "1.1.1971"))
            }
        }
    }
}