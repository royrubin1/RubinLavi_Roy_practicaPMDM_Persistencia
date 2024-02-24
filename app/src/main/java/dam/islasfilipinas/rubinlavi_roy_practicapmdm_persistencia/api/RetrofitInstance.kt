package dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: StudentApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.137:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StudentApiService::class.java)
    }
}

