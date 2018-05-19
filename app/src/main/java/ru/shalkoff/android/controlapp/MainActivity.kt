package ru.shalkoff.android.controlapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Observer
import org.json.JSONObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val URL = "http://serverman4ik.myarena.ru/"
    private val RELAY_POST = "relayControl.php"

    private val RELAY_KEY = "relay"
    private val TOKEN_KEY = "token"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListeners()
        main_relay_status_tv.setText(R.string.main_status_unknown_text)
    }

    private fun initListeners() {
        main_relay_on_btn.setOnClickListener {
            changeStateRelay(true)
        }
        main_relay_off_btn.setOnClickListener {
            changeStateRelay(false)
        }
    }

    private fun changeStateRelay(stateOn: Boolean) {
        main_progress_layout.visibility = View.VISIBLE
        Rx2AndroidNetworking.post(URL + RELAY_POST)
                .addBodyParameter(RELAY_KEY, stateOn.toString())
                .addBodyParameter(TOKEN_KEY, "(^_^)")
                .build()
                .jsonObjectObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    main_relay_status_tv.setText(
                            if (stateOn)
                                R.string.main_status_on_text
                            else
                                R.string.main_status_off_text)
                    main_progress_layout.visibility = View.GONE
                }, {
                    main_relay_status_tv.setText(R.string.main_status_error_text)
                    main_progress_layout.visibility = View.GONE
                })
    }
}
