package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.MutableCreationExtras

class MainActivity : AppCompatActivity() {

//    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    val viewModelStoreOwner: ViewModelStoreOwner = this
    lateinit var viewModel: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        viewModel= ViewModelProvider.create(
            viewModelStoreOwner,
            factory = CatsViewModel.Factory,
            extras = MutableCreationExtras().apply {
                set(CatsViewModel.CATS_SERVICE_KEY, diContainer.service)
                set(CatsViewModel.PICTURES_SERVICE_KEY, diContainer.servicePictures)
            },
        )[CatsViewModel::class]

//        catsPresenter = CatsPresenter(diContainer.service, diContainer.servicePictures)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
        viewModel.attachView(view)
        view.findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.getData()
        }
    }

//    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//        }
//        super.onStop()
//    }
}