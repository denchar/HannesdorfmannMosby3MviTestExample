package com.example.mviexapplication.View

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.mviexapplication.View.Data.MainView
import com.example.mviexapplication.View.Utils.DataSource
import com.example.mviexapplication.databinding.ActivityMainBinding
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.RxView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.Observable

import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.*


class MainActivity() : MviActivity<MainView, MainPresenter>(), MainView {

    private  var imageList: List<String>? = null
    private  var binding: ActivityMainBinding? = null

    override val imageIntent: Observable<Int>
        get() = RxView.clicks(binding?.myButton!!).map { _ ->
            getRandomNumberInRange(0, imageList?.size!! - 1)
        }

    private fun getRandomNumberInRange(i: Int, i1: Int): Int? {
        if (i > i1) {
            throw IllegalArgumentException("max mast be greater then min")
        }
        val r = Random()
        return r.nextInt(i1 - i + 1) + i
    }


    override fun createPresenter(): MainPresenter {
        return imageList?.let { DataSource(it) }?.let { MainPresenter(it) }!!
    }

    override fun render(viewState: MainViewState) {

        if (viewState.isLoading) {
            binding?.imageView?.visibility = View.GONE
            binding?.progressCircular?.visibility = View.VISIBLE
            binding?.myButton?.isEnabled = false
        } else if (viewState.error != null) {
            binding?.imageView?.visibility = View.GONE
            binding?.progressCircular?.visibility = View.GONE
            binding?.myButton?.isEnabled = true
            Toast.makeText(this, viewState.error!!.message.toString(), Toast.LENGTH_SHORT).show()
        }else if (viewState.isImageViewShow) {

            binding?.myButton?.isEnabled = true

            Picasso.get().load(viewState.imageLink).fetch(
                object :Callback{
                    override fun onSuccess() {
                        binding?.imageView?.alpha = 0f
                        Picasso.get().load(viewState.imageLink).into(binding?.imageView)
                        binding?.imageView?.animate()?.setDuration(300)?.alpha(1f)?.start()

                        binding?.imageView?.visibility = View.VISIBLE
                        binding?.progressCircular?.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        binding?.progressCircular?.visibility = View.GONE
                    }
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)

        imageList = createImageList()
    }

    private fun createImageList(): List<String> {
        return listOf(
            "https://www.nasa.gov/sites/default/files/styles/full_width_feature/public/thumbnails/image/s20-005_core_stage_installation_2718.jpg",
            "https://www.nasa.gov/sites/default/files/styles/image_card_4x3_ratio/public/thumbnails/image/iss061e142964_0.jpg",
            "https://www.nasa.gov/sites/default/files/styles/image_card_4x3_ratio/public/thumbnails/image/pia21421.jpg",
            "https://www.nasa.gov/sites/default/files/styles/image_card_4x3_ratio/public/thumbnails/image/potw2002a.jpg",
            "https://www.nasa.gov/sites/default/files/styles/image_card_4x3_ratio/public/thumbnails/image/pia22692.jpg",
            "https://www.nasa.gov/sites/default/files/styles/image_card_4x3_ratio/public/thumbnails/image/bd20307_fnl_lynettecook_0.jpg"
        )
    }

}
