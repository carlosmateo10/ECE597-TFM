package es.upm.dit.ece597_tfm

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val signOutButton = findViewById<Button>(R.id.sign_out_button)

        signOutButton.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        startActivity(Intent(this, LoginActivity::class.java))
        FirebaseAuth.getInstance().signOut()
    }
}
