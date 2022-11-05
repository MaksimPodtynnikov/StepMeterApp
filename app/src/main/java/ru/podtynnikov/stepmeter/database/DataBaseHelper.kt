package ru.podtynnikov.stepmeter.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.BitmapFactory
import androidx.core.graphics.scale
import ru.podtynnikov.stepmeter.ImageConvert.bitmapToBlob
import ru.podtynnikov.stepmeter.R
import ru.podtynnikov.stepmeter.Target

class DataBaseHelper(var context: Context) : SQLiteOpenHelper(
    context, DB_NAME, null, SCHEMA
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE " + TABLE_DAY + " (" + COLUMN_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DATE
                    + " DATE, " + COLUMN_STEPS + " INTEGER);"
        )
        db.execSQL(
            "CREATE TABLE " + TABLE_TARGET + " (" + COLUMN_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TITLE
                    + " TEXT, " + COLUMN_STEPS + " INTEGER, " + COLUMN_ICON
                    + " BLOB, " + COLUMN_READY + " BIT);"
        )
        // добавление начальных данных
        insertTarget(
            db, Target(
                1,
                "Это только начало",
                bitmapToBlob(BitmapFactory.decodeResource(context.resources, R.drawable.start).scale(200,200)),
                100,
                false
            )
        )
        insertTarget(
            db, Target(
                1,
                "Марафонец",
                bitmapToBlob(BitmapFactory.decodeResource(context.resources, R.drawable.maraphon).scale(200,200)),
                10000,
                false
            )
        )
        insertTarget(
            db, Target(
                1,
                "Настоящий медалист",
                bitmapToBlob(BitmapFactory.decodeResource(context.resources, R.drawable.medal).scale(200,200)),
                25000,
                false
            )
        )
        insertTarget(
            db, Target(
                1,
                "Из варяг в греки",
                bitmapToBlob(BitmapFactory.decodeResource(context.resources, R.drawable.earth).scale(200,200)),
                150000,
                false
            )
        )
    }

    private fun insertTarget(db: SQLiteDatabase, target: Target) {
        val cv = ContentValues()
        cv.put(COLUMN_STEPS, target.steps)
        cv.put(COLUMN_ICON, target.icon)
        cv.put(COLUMN_TITLE, target.title)
        cv.put(COLUMN_READY, target.isReady)
        db.insert(TABLE_TARGET, null, cv)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TARGET")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DAY")
        onCreate(db)
    }

    companion object {
        private const val DB_NAME = "steps.db"
        private const val SCHEMA = 1 // версия базы данных
        const val TABLE_DAY = "day" // название таблицы в бд

        // названия столбцов
        const val COLUMN_ID = "id"
        const val COLUMN_DATE = "date"
        const val COLUMN_STEPS = "steps"
        const val COLUMN_READY = "ready"
        const val TABLE_TARGET = "target" // название таблицы в бд

        // названия столбцов
        const val COLUMN_TITLE = "title"
        const val COLUMN_ICON = "icon"
    }
}