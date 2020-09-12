package evans18.skymeet.data.database

import androidx.room.*
import evans18.skymeet.data.model.entities.Flight

@Dao
interface FlightDao {

    @Query("SELECT * FROM flights")
    fun getAll(): List<Flight>

    @Query("SELECT * FROM flights WHERE id == (:flightId)")
    fun loadAllByIds(flightId: String): List<Flight>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg flight: Flight)

    @Delete
    fun delete(flight: Flight)

    @Query("DELETE FROM flights")
    fun nukeTable()
}