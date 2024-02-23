package dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia

data class Task(val title: String, val description: String, val module: String, var isDone: Boolean = false) {
}

