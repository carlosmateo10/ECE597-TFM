package es.upm.dit.ece597_tfm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PersonActivity : AppCompatActivity() {

    //var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        //Define recycleview
        var recyclerView = findViewById(R.id.person_list) as RecyclerView
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        //Get person ID
        val personID = intent.getStringExtra("id")

        //Initialize Firebase app
        val firestoreDB = FirebaseFirestore.getInstance()
        val query: Query = firestoreDB.collection("people").whereEqualTo("id", personID)

        // RecyclerOptions
        val options: FirestoreRecyclerOptions<Person?> = FirestoreRecyclerOptions.Builder<Person>()
            .setLifecycleOwner(this)
            .setQuery(query, Person::class.java)
            .build()
        val adapter = ListAdapter(options)

        recyclerView.setAdapter(adapter)

    }
}