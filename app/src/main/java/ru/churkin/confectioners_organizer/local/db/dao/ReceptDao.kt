package ru.churkin.confectioners_organizer.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.churkin.confectioners_organizer.local.db.entity.Recept
import ru.churkin.confectioners_organizer.local.db.entity.ReceptFull

@Dao
interface ReceptDao {
    @Query("SELECT * FROM recepts")
    suspend fun loadAll(): List<Recept>

    @Insert
    suspend fun insert(recept: Recept): Long

    @Query("DELETE FROM recepts WHERE id = :receptId")
    suspend fun delete(receptId: Int)

    @Query(
        """
        SELECT * FROM recepts
        WHERE id = :receptId
    """
    )
    suspend fun loadReceptFull(receptId: Int):ReceptFull
}