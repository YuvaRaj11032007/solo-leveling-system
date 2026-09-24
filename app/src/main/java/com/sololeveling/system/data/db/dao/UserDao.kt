package com.sololeveling.system.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sololeveling.system.data.db.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserFlow(): Flow<UserEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE user_profile SET unallocatedPoints = unallocatedPoints - 1, strength = strength + 1 WHERE id = 1 AND unallocatedPoints > 0")
    suspend fun increaseStrength()

    @Query("UPDATE user_profile SET unallocatedPoints = unallocatedPoints - 1, agility = agility + 1 WHERE id = 1 AND unallocatedPoints > 0")
    suspend fun increaseAgility()

    @Query("UPDATE user_profile SET unallocatedPoints = unallocatedPoints - 1, vitality = vitality + 1, maxHp = maxHp + 20, currentHp = currentHp + 20 WHERE id = 1 AND unallocatedPoints > 0")
    suspend fun increaseVitality()

    @Query("UPDATE user_profile SET unallocatedPoints = unallocatedPoints - 1, intelligence = intelligence + 1, maxMp = maxMp + 10, currentMp = currentMp + 10 WHERE id = 1 AND unallocatedPoints > 0")
    suspend fun increaseIntelligence()

    @Query("UPDATE user_profile SET unallocatedPoints = unallocatedPoints - 1, perception = perception + 1 WHERE id = 1 AND unallocatedPoints > 0")
    suspend fun increasePerception()

    @Query("UPDATE user_profile SET currentHp = maxHp, currentMp = maxMp, fatigue = 0 WHERE id = 1")
    suspend fun fullRecovery()
}
