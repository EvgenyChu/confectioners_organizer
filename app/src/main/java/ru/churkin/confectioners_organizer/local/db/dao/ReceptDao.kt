package ru.churkin.confectioners_organizer.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.churkin.confectioners_organizer.local.db.entity.Recept
import ru.churkin.confectioners_organizer.local.db.entity.ReceptFull

@Dao
interface ReceptDao {

    @Query("SELECT * FROM recepts")
    suspend fun loadAll(): List<Recept>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recept: Recept): Long

    @Query("DELETE FROM recepts WHERE id = :id")
    suspend fun delete(id: Long)

    @Query(
        """
        SELECT * FROM recepts
        WHERE id = :id
    """
    )
    suspend fun loadReceptFull(id: Long):ReceptFull
}