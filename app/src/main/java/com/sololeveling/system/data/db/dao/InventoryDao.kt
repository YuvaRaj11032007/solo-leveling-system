package com.sololeveling.system.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sololeveling.system.data.db.entities.InventoryItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {
    @Query("SELECT * FROM inventory_items ORDER BY category ASC, rarity DESC")
    fun getAllItemsFlow(): Flow<List<InventoryItemEntity>>

    @Query("SELECT * FROM inventory_items WHERE id = :id LIMIT 1")
    suspend fun getItemById(id: String): InventoryItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<InventoryItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: InventoryItemEntity)

    @Update
    suspend fun updateItem(item: InventoryItemEntity)

    @Query("DELETE FROM inventory_items WHERE id = :id")
    suspend fun deleteItem(id: String)
}
