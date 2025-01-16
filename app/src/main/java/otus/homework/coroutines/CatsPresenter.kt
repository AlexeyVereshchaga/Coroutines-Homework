package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatsPresenter(
    private val catsService: CatsService,
    private val picturesService: PicturesService
) {

    private var _catsView: ICatsView? = null
    private val scope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private var job: Job? = null

    fun onInitComplete() {
        job = scope.launch {
                val factDeferred = async {
                    getFact().onFailure { onRequestFailure(it) }
                }
                val pictureDeferred = async {
                    getPicture().onFailure { onRequestFailure(it) }
                }

                val fact = factDeferred.await()
                val picture = pictureDeferred.await()
                if (fact.isSuccess && picture.isSuccess) {
//                    _catsView?.populate(
//                        ICatsView.Model(
//                            fact.getOrThrow(),
//                            picture.getOrThrow()[0].url
//                        )
//                    )
                }
        }
    }

    private fun CoroutineScope.onRequestFailure(t: Throwable) {
        CrashMonitor.trackWarning(t)
//        _catsView?.showToast(t.message ?: "Ошибка")
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

    fun detachView() {
        job?.cancel()
        _catsView = null
    }
}