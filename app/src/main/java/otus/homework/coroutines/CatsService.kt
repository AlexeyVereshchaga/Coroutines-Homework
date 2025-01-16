package otus.homework.coroutines

import retrofit2.http.GET

interface CatsService {

    @GET("fact1")
    suspend fun getCatFact(): Fact
}