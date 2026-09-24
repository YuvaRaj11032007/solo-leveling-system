package com.sololeveling.system.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sololeveling.system.data.db.entities.SkillEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SkillDao {
    @Query("SELECT * FROM hunter_skills ORDER BY id ASC")
    fun getAllSkillsFlow(): Flow<List<SkillEntity>>

    @Query("SELECT * FROM hunter_skills WHERE id = :id LIMIT 1")
    suspend fun getSkillById(id: String): SkillEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkills(skills: List<SkillEntity>)

    @Update
    suspend fun updateSkill(skill: SkillEntity)
}
