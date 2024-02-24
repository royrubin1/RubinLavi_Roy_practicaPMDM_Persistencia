package dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.room
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val module: String,
    var isDone: Boolean = false
)