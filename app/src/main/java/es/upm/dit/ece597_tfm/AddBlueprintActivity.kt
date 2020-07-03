package es.upm.dit.ece597_tfm

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_blueprint.*

class AddBlueprintActivity : AppCompatActivity() {

    var uri:Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blueprint)
        val selectBlueprintButton = findViewById<Button>(R.id.select_image_button)
        val saveBlueprintButton = findViewById<Button>(R.id.save_blueprint_button)

        selectBlueprintButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        saveBlueprintButton.setOnClickListener {
            if (uri != null) {
                val userID = FirebaseAuth.getInstance().currentUser?.uid

                val path = "/"+userID+"/"+blueprint_name.text
                val ref = FirebaseStorage.getInstance().getReference(path)

                ref.putFile(uri!!).addOnSuccessListener {
                    val firestoreDB = FirebaseFirestore.getInstance()
                    val blueprint = hashMapOf("name" to blueprint_name.text.toString())

                    firestoreDB.collection("users").document(userID!!)
                        .collection("blueprints").document(blueprint_name.text.toString())
                        .set(blueprint)
                        .addOnSuccessListener {
                            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
                        }

                    firestoreDB.collection("users").document(userID!!)
                        .set( hashMapOf("userID" to userID))
                }
            } else {
                Toast.makeText(this, "Please, first select the image of the blueprint", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

            val bitmapDrawable = BitmapDrawable(bitmap)
            blueprint_imageview.setBackgroundDrawable(bitmapDrawable)
        }
    }
}