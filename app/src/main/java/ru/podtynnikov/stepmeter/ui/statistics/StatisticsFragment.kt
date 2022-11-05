package ru.podtynnikov.stepmeter.ui.statistics

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import ru.podtynnikov.stepmeter.R
import ru.podtynnikov.stepmeter.database.DataBaseAdapter


class StatisticsFragment:Fragment(R.layout.fragment_statistics) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chart=view.findViewById<LineChart>(R.id.chart)
        val dataBaseAdapter= this.context?.let { DataBaseAdapter(it) }
        dataBaseAdapter?.open()
        val entries= dataBaseAdapter?.daysEntry
        dataBaseAdapter?.close()
        val dataset = LineDataSet(entries, "График ходьбы")
        val data = LineData(dataset)
        chart.data = data
        chart.description.text="Количество шагов в день"
        chart.invalidate()
    }
}