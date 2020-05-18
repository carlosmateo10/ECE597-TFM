package es.upm.dit.ece597_tfm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class ListActivity  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        //Initialize Firebase app
        var firestoreDB = FirebaseFirestore.getInstance()
        var query: Query = firestoreDB.collection("people")

        loadRecyclerView(query)

        val searchButton = findViewById<Button>(R.id.search_button)
        val cameraNumber = findViewById<EditText>(R.id.camera_number)

        searchButton.setOnClickListener {
            var query: Query = firestoreDB.collection("people")
            Log.d("ListActivity", cameraNumber.text.toString())
            if(cameraNumber.text.isNotEmpty()) query = query.whereEqualTo("camera", cameraNumber.text.toString())
            loadRecyclerView(query)
        }



    }

    fun loadRecyclerView(query: Query) {
        //Define recycleview
        var recyclerView = findViewById(R.id.people_list) as RecyclerView
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        // RecyclerOptions
        val options: FirestoreRecyclerOptions<Person?> = FirestoreRecyclerOptions.Builder<Person>()
            .setLifecycleOwner(this)
            .setQuery(query, Person::class.java)
            .build()
        val adapter = ListAdapter(options)

        // Item Click Listener
        adapter.onItemClick = {person ->
            val intent = Intent(this, PersonActivity::class.java).apply {
                putExtra("id", person.id)
            }
            startActivity(intent)
        }
        recyclerView.setAdapter(adapter)
    }
}
