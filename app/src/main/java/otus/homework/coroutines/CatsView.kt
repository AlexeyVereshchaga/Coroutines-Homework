package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

//    var presenter: CatsPresenter? = null

//    override fun onFinishInflate() {
//        super.onFinishInflate()
//        findViewById<Button>(R.id.button).setOnClickListener {
//            presenter?.onInitComplete()
//        }
//    }

    override fun populate(result: Result) {
        when (result) {
            is Result.Error -> Toast.makeText(context, result.text, Toast.LENGTH_SHORT).show()
            is Result.Success<*> -> {
                (result.data as? ICatsView.Model)?.let {
                    findViewById<TextView>(R.id.fact_textView).text = it.fact.fact
                    Picasso.get()
                        .load(it.imageUrl)
                        .into(findViewById<ImageView>(R.id.cat_imageView))
                }

            }
        }
    }
}

interface ICatsView {
    fun populate(result: Result)

    data class Model(
        val fact: Fact,
        val imageUrl: String
    )
}