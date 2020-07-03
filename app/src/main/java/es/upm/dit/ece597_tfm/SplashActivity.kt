package es.upm.dit.ece597_tfm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val background = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(100)

                    // Check for existing Google Sign In account, if the user is already signed in
                    // the GoogleSignInAccount will be non-null.
                    val account = FirebaseAuth.getInstance().currentUser
                    lateinit var intent: Intent
                    if(account != null) {
                        Log.d("SPLASH SCREEN", "User already logged in")
                        intent = Intent(baseContext, MainActivity::class.java)
                    } else {
                        Log.d("SPLASH SCREEN", "User not logged in")
                        intent = Intent(baseContext, LoginActivity::class.java)
                    }
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}