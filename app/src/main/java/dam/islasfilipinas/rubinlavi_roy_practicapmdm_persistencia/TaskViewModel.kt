package dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.room.DatabaseBuilder
import dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.room.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val db = DatabaseBuilder.getInstance(application)
    private val taskDao = db.taskDao()

    fun getAllTasks(): LiveData<List<Task>> = taskDao.getAllTasks()

    fun insertTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDao.insertTask(task)
        }
    }

    fun updateTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDao.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDao.deleteTask(task)
        }
    }

    fun getTaskById(taskId: Int): LiveData<Task> = taskDao.getTaskById(taskId)
}
