package com.droidmind.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room
import net.sqlcipher.database.SupportFactory

@Entity(tableName = "ad_logs")
data class AdLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val domain: String,
    val appName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val blocked: Boolean = true
)

@Dao
interface AdLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: AdLog)

    @Query("SELECT * FROM ad_logs ORDER BY timestamp DESC")
    suspend fun getAllLogs(): List<AdLog>

    @Query("SELECT COUNT(*) FROM ad_logs WHERE blocked = 1")
    suspend fun getTotalAdsBlocked(): Int
}

@Database(entities = [AdLog::class], version = 1, exportSchema = false)
abstract class DroidMindDatabase : RoomDatabase() {
    abstract fun adLogDao(): AdLogDao

    companion object {
        @Volatile
        private var INSTANCE: DroidMindDatabase? = null

        fun getDatabase(context: Context, securityManager: SecurityManager): DroidMindDatabase {
            return INSTANCE ?: synchronized(this) {
                val passphrase = securityManager.getDatabasePassphrase().toByteArray()
                val factory = SupportFactory(passphrase)

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DroidMindDatabase::class.java,
                    "droidmind_db"
                ).openHelperFactory(factory)
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
