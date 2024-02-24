package dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.api

import retrofit2.http.GET
import retrofit2.Call

interface StudentApiService {
    @GET("student")
    fun getStudentModules(): Call<Student>
}
