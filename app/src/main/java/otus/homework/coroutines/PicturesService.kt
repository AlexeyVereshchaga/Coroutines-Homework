package otus.homework.coroutines

import retrofit2.http.GET

interface PicturesService {

    //  Добавить к запросу фактов запрос рандомных картинок с https://aws.random.cat/meow
    @GET("v1/images/search")
    suspend fun getPicture() : List<Picture>


}