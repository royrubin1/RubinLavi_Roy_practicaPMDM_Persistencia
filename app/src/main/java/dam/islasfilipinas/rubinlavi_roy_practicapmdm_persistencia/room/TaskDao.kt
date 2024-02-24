package dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.room
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAllTasks(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task WHERE id = :taskId")
    fun getTaskById(taskId: Int): LiveData<Task>
}