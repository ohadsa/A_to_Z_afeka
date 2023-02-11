package com.ohadsa.a_to_z

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.ohadsa.a_to_z.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : FragmentActivity() {


    @Inject
    lateinit var auth: FirebaseAuth

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }



    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null)
            startLoginActivity()
        else {
            println("cur userId = ${auth.currentUser?.uid}")
            println("cur user email = ${auth.currentUser?.email}")
        }

    }


    private var shouldHide = false

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_UP -> {
                if (shouldHide) {
                    currentFocus?.let { focus ->
                        UIUtil.hideKeyboard(this)
                        focus.clearFocus()
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> shouldHide = false
            MotionEvent.ACTION_DOWN -> shouldHide = true
        }
        return super.dispatchTouchEvent(ev)
    }

}
