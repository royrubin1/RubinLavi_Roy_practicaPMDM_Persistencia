package dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.room
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}