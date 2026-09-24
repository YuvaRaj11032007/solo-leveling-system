package com.sololeveling.system.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sololeveling.system.data.db.dao.InventoryDao
import com.sololeveling.system.data.db.dao.QuestDao
import com.sololeveling.system.data.db.dao.SkillDao
import com.sololeveling.system.data.db.dao.UserDao
import com.sololeveling.system.data.db.entities.DailyQuestEntity
import com.sololeveling.system.data.db.entities.GateQuestEntity
import com.sololeveling.system.data.db.entities.InventoryItemEntity
import com.sololeveling.system.data.db.entities.SkillEntity
import com.sololeveling.system.data.db.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        DailyQuestEntity::class,
        GateQuestEntity::class,
        InventoryItemEntity::class,
        SkillEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun questDao(): QuestDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun skillDao(): SkillDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "solo_leveling_system.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
