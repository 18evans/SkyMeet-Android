package evans18.skymeet.data.database

import android.database.SQLException
import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import evans18.skymeet.data.model.entities.Flight
import evans18.skymeet.util.TAG


@Database(entities = [Flight::class], version = 3, exportSchema = false)
@TypeConverters(FlightPositionListConverter::class)
abstract class SkyMeetDatabase : RoomDatabase() {

    abstract fun flightDao(): FlightDao

    internal companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                try {
                    database.beginTransaction()

                    database.execSQL("ALTER TABLE 'aircrafts' RENAME TO 'aircraftsBACKUP'")
                    database.execSQL(
                        "CREATE TABLE 'flights' (" +
                                "id" + " INTEGER" + " PRIMARY KEY" + " NOT NULL" + "," +
                                "aircraft_aircraftId" + " INTEGER" + " NOT NULL" + "," +
                                "aircraft_callSign" + " TEXT" + "," +
                                "aircraft_tailSign" + " TEXT" + "," +
                                "operator_name" + " TEXT" + " NOT NULL" + "," +
                                "flightPositions" + " TEXT" + "NOT NULL DEFAULT \"[]\"" +
                                ");"
                    )
                    database.execSQL(
                        "INSERT INTO flights(aircraft_aircraftId, aircraft_callSign, aircraft_tailSign, operator_name)\n" +
                                "SELECT id, serialNumber, registration, manager_name\n" +
                                "FROM aircraftsBACKUP;"
                    )
                    database.execSQL("DROP TABLE aircraftsBACKUP")
                    database.setTransactionSuccessful()
                } catch (ex: SQLException) { //if any of the queries has bad syntax/response
                    Log.e(TAG, "Exception during migration", ex)
                } finally {
                    database.endTransaction()
                }
            }
        }
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                try {
                    database.beginTransaction()

                    database.execSQL("ALTER TABLE 'flights' RENAME TO 'flightsBACKUP'")
                    database.execSQL(
                        "CREATE TABLE 'flights' (" +
                                "id" + " INTEGER" + " PRIMARY KEY" + "," +
                                "aircraft_aircraftId" + " INTEGER" + " NOT NULL" + "," +
                                "aircraft_callSign" + " TEXT" + "," +
                                "aircraft_tailSign" + " TEXT" + "," +
                                "operator_name" + " TEXT" + " NOT NULL" + "," +
                                "flightPositions" + " TEXT" + "NOT NULL DEFAULT \"{}\"" +
                                ");"
                    )
                    database.execSQL(
                        "INSERT INTO flights(id, aircraft_aircraftId, aircraft_callSign, aircraft_tailSign, operator_name, flightPositions)\n" +
                                "SELECT id, aircraft_aircraftId, aircraft_callSign, aircraft_tailSign, operator_name, flightPositions\n" +
                                "FROM flightsBACKUP;"
                    )
                    database.execSQL("DROP TABLE flightsBACKUP")
                    database.setTransactionSuccessful()
                } catch (ex: SQLException) { //if any of the queries has bad syntax/response
                    Log.e(TAG, "Exception during migration", ex)
                } finally {
                    database.endTransaction()
                }
            }
        }
        //make aircraft_tailSign NOT NULL
        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                try {
                    database.beginTransaction()

                    database.execSQL("ALTER TABLE 'flights' RENAME TO 'flightsBACKUP'")
                    database.execSQL(
                        "CREATE TABLE 'flights' (" +
                                "id" + " INTEGER" + " PRIMARY KEY" + "," +
                                "aircraft_aircraftId" + " INTEGER" + " NOT NULL" + "," +
                                "aircraft_callSign" + " TEXT" + "," +
                                "aircraft_tailSign" + " TEXT" + " NOT NULL" + "," +
                                "operator_name" + " TEXT" + " NOT NULL" + "," +
                                "flightPositions" + " TEXT" + "NOT NULL DEFAULT \"{}\"" +
                                ");"
                    )
                    database.execSQL(
                        "INSERT INTO flights(id, aircraft_aircraftId, aircraft_callSign, aircraft_tailSign, operator_name, flightPositions)\n" +
                                "SELECT id, aircraft_aircraftId, aircraft_callSign, aircraft_tailSign, operator_name, flightPositions\n" +
                                "FROM flightsBACKUP;"
                    )
                    database.execSQL("DROP TABLE flightsBACKUP")
                    database.setTransactionSuccessful()
                } catch (ex: SQLException) { //if any of the queries has bad syntax/response
                    Log.e(TAG, "Exception during migration", ex)
                } finally {
                    database.endTransaction()
                }
            }
        }
    }

}
