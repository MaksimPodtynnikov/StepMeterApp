package ru.podtynnikov.stepmeter
import android.widget.ArrayAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class TargetAdapter(context: Context?, private val layout: Int, private val targets: List<Target>) :
    ArrayAdapter<Target?>(
        context!!, layout, targets
    ) {
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        @SuppressLint("ViewHolder") val view = inflater.inflate(layout, parent, false)
        val avatarView = view.findViewById<ImageView>(R.id.avatarTarget)
        val titleView = view.findViewById<TextView>(R.id.titleTarget)
        val stepsView = view.findViewById<TextView>(R.id.stepsTarget)
        val checkedView = view.findViewById<ImageView>(R.id.targetChecked)
        val target = targets[position]
        avatarView.setImageBitmap(target.bitmap)
        titleView.text = target.title
        stepsView.text = target.steps.toString()+ " шагов"
        if (target.isReady) checkedView.visibility = View.VISIBLE
        return view
    }
}