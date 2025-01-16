package otus.homework.coroutines


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatsViewModel(
    private val catsService: CatsService,
    private val picturesService: PicturesService
) : ViewModel() {

    private var _catsView: ICatsView? = null
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(exception)
    }

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            launch {
                val factDeferred = async {
                    getFact().onFailure { onRequestFailure(it) }
                }
                val pictureDeferred = async {
                    getPicture().onFailure { onRequestFailure(it) }
                }

                val fact = factDeferred.await()
                val picture = pictureDeferred.await()
                if (fact.isSuccess && picture.isSuccess) {
                    _catsView?.populate(
                        Result.Success(
                            ICatsView.Model(
                                fact.getOrThrow(),
                                picture.getOrThrow()[0].url
                            )
                        )
                    )
                }
            }
        }
    }

    private fun CoroutineScope.onRequestFailure(t: Throwable) {
        CrashMonitor.trackWarning(t)
        _catsView?.populate(Result.Error(t.message ?: "Ошибка"))
        cancel()
    }

    private suspend fun getFact() = runCatching {
        withContext(Dispatchers.IO) {
            catsService.getCatFact()
        }
    }

    private suspend fun getPicture() = runCatching {
        withContext(Dispatchers.IO) {
            picturesService.getPicture()
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    companion object {
        // Define a custom key for your dependency
        val CATS_SERVICE_KEY = object : CreationExtras.Key<CatsService> {}
        val PICTURES_SERVICE_KEY = object : CreationExtras.Key<PicturesService> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Get the dependency in your factory
                val catsService = this[CATS_SERVICE_KEY] as CatsService
                val picturesService = this[PICTURES_SERVICE_KEY] as PicturesService
                CatsViewModel(
                    catsService = catsService,
                    picturesService = picturesService
                )
            }
        }
    }
}