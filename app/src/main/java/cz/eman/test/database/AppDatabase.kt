package cz.eman.test.database

import com.raizlabs.android.dbflow.annotation.Database

/**
 * Setting up DBFLow database.
 * Note: Version change does not delete the database. It uses migrations.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION, generatedClassSeparator = "_")
object AppDatabase {
    const val NAME: String = "Questions"
    const val VERSION: Int = 1
}