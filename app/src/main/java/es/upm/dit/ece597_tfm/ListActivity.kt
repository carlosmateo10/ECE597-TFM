package es.upm.dit.ece597_tfm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class ListActivity  : AppCompatActivity(){

    private var adapter: ListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        //Define recycleview
        var recyclerView = findViewById(R.id.people_list) as RecyclerView
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        //Initialize your Firebase app
        var firestoreDB = FirebaseFirestore.getInstance()
        val query: Query =
            firestoreDB.collection("people")

        // RecyclerOptions
        val options: FirestoreRecyclerOptions<Person?> = FirestoreRecyclerOptions.Builder<Person>()
            .setLifecycleOwner(this)
            .setQuery(query, Person::class.java)
            .build()
        adapter = ListAdapter(options)
        recyclerView.setAdapter(adapter)

    }
}
