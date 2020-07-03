package es.upm.dit.ece597_tfm

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val signOutButton = findViewById<Button>(R.id.sign_out_button)
        val toListButton = findViewById<Button>(R.id.list_button)
        val toSettingsButton = findViewById<Button>(R.id.settings_button)
        val toReportsButton = findViewById<Button>(R.id.reports_button)

        signOutButton.setOnClickListener { signOut()}
        toListButton.setOnClickListener { startActivity(Intent(this, ListActivity::class.java))}
        toReportsButton.setOnClickListener { startActivity(Intent(this, ReportsActivity::class.java))}
        toSettingsButton.setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java))}
    }

    private fun signOut() {
        startActivity(Intent(this, LoginActivity::class.java))
        FirebaseAuth.getInstance().signOut()
        var mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()
        var mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
        mGoogleSignInClient.signOut()
    }
}
