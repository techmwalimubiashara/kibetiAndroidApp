package com.mb.kibeti.sms_filter

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// --- ROOM DATABASE SETUP --- //
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val transactionCode: String,
    val amount: Double,
    val recipient: String,
    val transactionType: String,
    val timestamp: Long,
    val smsId: Long? = null  // Add this field to track SMS message ID
)


@Database(
    entities = [TransactionEntity::class],
    version = 2,  // Incremented from 1 to 2 due to schema change
    exportSchema = true  // Important for production to track schema changes
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Migration from version 1 to 2 (adding smsId column)
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE transactions ADD COLUMN smsId INTEGER DEFAULT NULL"
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mpesa_db"
                )
                    .addMigrations(MIGRATION_1_2)  // Add all migrations here
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}