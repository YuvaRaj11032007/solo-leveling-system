package com.sololeveling.system.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sololeveling.system.data.db.entities.DailyQuestEntity
import com.sololeveling.system.data.db.entities.GateQuestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestDao {
    @Query("SELECT * FROM daily_quests WHERE id = 1 LIMIT 1")
    fun getDailyQuestFlow(): Flow<DailyQuestEntity?>

    @Query("SELECT * FROM daily_quests WHERE id = 1 LIMIT 1")
    suspend fun getDailyQuest(): DailyQuestEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDailyQuest(quest: DailyQuestEntity)

    @Update
    suspend fun updateDailyQuest(quest: DailyQuestEntity)

    @Query("UPDATE daily_quests SET pushupsCurrent = MIN(pushupsTarget, pushupsCurrent + :count) WHERE id = 1")
    suspend fun incrementPushups(count: Int = 1)

    @Query("UPDATE daily_quests SET situpsCurrent = MIN(situpsTarget, situpsCurrent + :count) WHERE id = 1")
    suspend fun incrementSitups(count: Int = 1)

    @Query("UPDATE daily_quests SET squatsCurrent = MIN(squatsTarget, squatsCurrent + :count) WHERE id = 1")
    suspend fun incrementSquats(count: Int = 1)

    @Query("UPDATE daily_quests SET stepsCurrent = MIN(stepsTarget, :steps) WHERE id = 1")
    suspend fun setSteps(steps: Int)

    @Query("UPDATE daily_quests SET stepsCurrent = MIN(stepsTarget, stepsCurrent + :steps) WHERE id = 1")
    suspend fun addSteps(steps: Int)

    // Gate Quests
    @Query("SELECT * FROM gate_quests ORDER BY id ASC")
    fun getAllGatesFlow(): Flow<List<GateQuestEntity>>

    @Query("SELECT * FROM gate_quests WHERE id = :id LIMIT 1")
    suspend fun getGateById(id: Int): GateQuestEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGates(gates: List<GateQuestEntity>)

    @Update
    suspend fun updateGate(gate: GateQuestEntity)
}
