package ru.podtynnikov.stepmeter.ui.achievements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import ru.podtynnikov.stepmeter.R
import ru.podtynnikov.stepmeter.TargetAdapter
import ru.podtynnikov.stepmeter.database.DataBaseAdapter


class AchievementsFragment : Fragment(R.layout.fragment_achievements){

    var targets: ArrayList<ru.podtynnikov.stepmeter.Target>? = ArrayList()
    private var adapter: DataBaseAdapter? =null
    private var targetAdapter: TargetAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = this.activity?.let { DataBaseAdapter(it) }
        targetAdapter = targets?.let { TargetAdapter(this.activity, R.layout.list_element, it) }
        view.findViewById<ListView>(R.id.targetList)?.adapter = targetAdapter
    }

    override fun onResume() {
        super.onResume()
        adapter?.open()
        targets?.clear()
        adapter?.let { targets?.addAll(it.targets) }
        adapter?.close()
        targetAdapter?.notifyDataSetChanged();
    }
}