package es.upm.dit.ece597_tfm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val blueprintButton = findViewById<Button>(R.id.blueprint_button)
        val manageButton = findViewById<Button>(R.id.manage_button)

        blueprintButton.setOnClickListener { startActivity(Intent(this, AddBlueprintActivity::class.java))}
        manageButton.setOnClickListener { startActivity(Intent(this, ManageBlueprintActivity::class.java))}
    }
}