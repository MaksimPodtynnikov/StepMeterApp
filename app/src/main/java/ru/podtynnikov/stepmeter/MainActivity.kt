package ru.podtynnikov.stepmeter

import android.Manifest
import android.Manifest.permission.*
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.podtynnikov.stepmeter.database.DataBaseAdapter
import ru.podtynnikov.stepmeter.ui.achievements.AchievementsFragment
import ru.podtynnikov.stepmeter.ui.home.HomeFragment
import ru.podtynnikov.stepmeter.ui.map.MapFragment
import ru.podtynnikov.stepmeter.ui.settings.SettingsFragment
import ru.podtynnikov.stepmeter.ui.statistics.StatisticsFragment
import java.sql.Date
import java.time.LocalDate


class MainActivity : AppCompatActivity(), SensorEventListener {

    @RequiresApi(Build.VERSION_CODES.O)
    private var sensorManager: SensorManager? = null
    private var pref: PreferenceAdapter? =null
    private var currentFragment: Int=0

    var fragment: Fragment=HomeFragment()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        pref=  PreferenceAdapter(this)
        checkTargets()
        setCurrentFragment(fragment)
        if(ActivityCompat.checkSelfPermission(
                this,
                WRITE_EXTERNAL_STORAGE
            )!=PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION),
                104)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.selectedItemId = R.id.steps
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.steps->{
                    fragment=HomeFragment()
                    setCurrentFragment(fragment)
                    currentFragment=0
                }
                R.id.statistic->{
                    fragment=StatisticsFragment()
                    setCurrentFragment(fragment)
                    currentFragment=1
                }
                R.id.settings->{
                    fragment=SettingsFragment()
                    setCurrentFragment(fragment)
                    currentFragment=2
                }
                R.id.map->{
                    fragment=MapFragment()
                    setCurrentFragment(fragment)
                    currentFragment=3
                }
                R.id.targets->{
                    fragment=AchievementsFragment()
                    setCurrentFragment(fragment)
                    currentFragment=4
                }

            }
            true
        }
        loadDays()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent?) {
        val totalSteps = event!!.values[0].toInt()
        if(pref!!.previousSteps!=0)
            pref!!.daySteps = totalSteps - pref!!.previousSteps
        else pref!!.previousSteps=totalSteps
        checkTargets()
        loadDays()
        if(currentFragment==0)
            (fragment as HomeFragment).setData(pref!!.daySteps,pref!!.targetSteps)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun loadDays()
    {
        pref= PreferenceAdapter(this)
        if(pref?.lastDate!! < LocalDate.now()){
            val dataBaseAdapter = DataBaseAdapter(this)
            var i= pref!!.lastDate
            dataBaseAdapter.open()
            dataBaseAdapter.insertDay(Day(-1, Date.valueOf(i.toString()),pref!!.daySteps))
            pref!!.previousSteps=pref!!.daySteps+pref!!.previousSteps
            if (i != null) {
                i=i.plusDays(1)
                while(i!!< LocalDate.now()){
                    dataBaseAdapter.insertDay(Day(-1, Date.valueOf(i.toString()),0))
                    i= i.plusDays(1)
                }
            }
            while(dataBaseAdapter.countDays>pref!!.period)
                dataBaseAdapter.deleteDay(dataBaseAdapter.lastDay.toLong())
            dataBaseAdapter.close()
        }
        pref!!.lastDate = LocalDate.now()
    }
    private fun checkTargets()
    {
        val dataBaseAdapter = DataBaseAdapter(this)
        dataBaseAdapter.open()
        for(target in dataBaseAdapter.targets) {
            if (target.title == "Из варяг в греки") {
                if (target.steps <= pref!!.daySteps + pref!!.previousSteps) {
                    target.isReady = true
                    dataBaseAdapter.updateTarget(target)
                    sendNotify(target.title)
                }
            }
            else if (target.steps <= pref!!.daySteps) {
                    target.isReady = true
                    dataBaseAdapter.updateTarget(target)
                sendNotify(target.title)
            }
        }
        dataBaseAdapter.close()
    }
    fun sendNotify(text: String)
    {
        val builder = NotificationCompat.Builder(this, "channelID")
            .setSmallIcon(R.drawable.ic_baseline_celebration_24)
            .setContentTitle("Новое достижение")
            .setContentText("Вы получили достижение '$text'")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(101, builder.build())
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }
    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment_activity_main,fragment)
            commit()
        }



}