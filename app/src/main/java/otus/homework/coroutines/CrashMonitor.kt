package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(exception: Throwable) {
        Log.e("","Handle $exception in CoroutineExceptionHandler")
    }
}