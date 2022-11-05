package ru.podtynnikov.stepmeter.database

import android.database.sqlite.SQLiteDatabase
import android.annotation.SuppressLint
import android.database.DatabaseUtils
import ru.podtynnikov.stepmeter.Day
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.github.mikephil.charting.data.Entry
import ru.podtynnikov.stepmeter.Target
import java.util.ArrayList

class DataBaseAdapter(context: Context) {
    private val dbHelper: DataBaseHelper
    private var database: SQLiteDatabase? = null

    init {
        dbHelper = DataBaseHelper(context.applicationContext)
    }

    fun open(): DataBaseAdapter {
        database = dbHelper.writableDatabase
        return this
    }

    fun close() {
        dbHelper.close()
    }

    private val allEntriesDays: Cursor
        get() {
            val columns = arrayOf(
                DataBaseHelper.COLUMN_ID,
                DataBaseHelper.COLUMN_DATE, DataBaseHelper.COLUMN_STEPS
            )
            return database!!.query(
                DataBaseHelper.TABLE_DAY,
                columns,
                null,
                null,
                null,
                null,
                DataBaseHelper.COLUMN_DATE + " ASC"
            )
        }
    private val allEntriesTargets: Cursor
        get() {
            val columns = arrayOf(
                DataBaseHelper.COLUMN_ID, DataBaseHelper.COLUMN_TITLE,
                DataBaseHelper.COLUMN_ICON, DataBaseHelper.COLUMN_STEPS, DataBaseHelper.COLUMN_READY
            )
            return database!!.query(
                DataBaseHelper.TABLE_TARGET,
                columns,
                null,
                null,
                null,
                null,
                null
            )
        }
    val daysEntry: ArrayList<Entry>
        get() {
            var i = 1
            val entries = ArrayList<Entry>()
            val cursor = allEntriesDays
            while (cursor.moveToNext()) {
                @SuppressLint("Range") val steps =
                    cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_STEPS))
                entries.add(
                    Entry(
                        i.toFloat(), steps.toInt().toFloat()
                    )
                )
                i++
            }
            cursor.close()
            return entries
        }
    val targets: ArrayList<Target>
        get() {
            val targets = ArrayList<Target>()
            val cursor = allEntriesTargets
            while (cursor.moveToNext()) {
                @SuppressLint("Range") val id =
                    cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COLUMN_ID))
                @SuppressLint("Range") val title =
                    cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_TITLE))
                @SuppressLint("Range") val icon =
                    cursor.getBlob(cursor.getColumnIndex(DataBaseHelper.COLUMN_ICON))
                @SuppressLint("Range") val steps =
                    cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COLUMN_STEPS))
                @SuppressLint("Range") val ready =
                    cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COLUMN_READY)) > 0
                targets.add(Target(id, title, icon, steps, ready))
            }
            cursor.close()
            return targets
        }
    val countDays: Long
        get() = DatabaseUtils.queryNumEntries(database, DataBaseHelper.TABLE_DAY)
    val lastDay: Int
        get() {
            var day = -1
            val query = String.format(
                "SELECT %s,%s FROM %s",
                DataBaseHelper.COLUMN_ID,
                DataBaseHelper.COLUMN_DATE,
                DataBaseHelper.TABLE_DAY
            )
            val cursor = database!!.rawQuery(query, null)
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") val id =
                    cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COLUMN_ID))
                day = id
            }
            cursor.close()
            return day
        }

    fun insertDay(day: Day): Long {
        val cv = ContentValues()
        cv.put(DataBaseHelper.COLUMN_DATE, day.date.toString())
        cv.put(DataBaseHelper.COLUMN_STEPS, day.steps)
        return database!!.insert(DataBaseHelper.TABLE_DAY, null, cv)
    }

    fun deleteDay(dayID: Long): Long {
        val whereClause = "id = ?"
        val whereArgs = arrayOf(dayID.toString())
        return database!!.delete(DataBaseHelper.TABLE_DAY, whereClause, whereArgs).toLong()
    }

    fun updateTarget(target: Target): Long {
        val whereClause = DataBaseHelper.COLUMN_ID + "=" + target.id
        val cv = ContentValues()
        cv.put(DataBaseHelper.COLUMN_STEPS, target.steps)
        cv.put(DataBaseHelper.COLUMN_ICON, target.icon)
        cv.put(DataBaseHelper.COLUMN_TITLE, target.title)
        cv.put(DataBaseHelper.COLUMN_READY, target.isReady)
        return database!!.update(DataBaseHelper.TABLE_TARGET, cv, whereClause, null).toLong()
    }
}