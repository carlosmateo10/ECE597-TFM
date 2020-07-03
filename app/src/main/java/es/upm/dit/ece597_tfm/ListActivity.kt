package es.upm.dit.ece597_tfm

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jem.rubberpicker.RubberRangePicker
import java.lang.StringBuilder
import java.util.*


class ListActivity  : AppCompatActivity() {

    var firestoreDB = FirebaseFirestore.getInstance()
    var userID: String? = FirebaseAuth.getInstance().currentUser?.uid
    lateinit var spinnerBlueprints: Spinner
    lateinit var timePicker: RubberRangePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        timePicker = findViewById<RubberRangePicker>(R.id.time_picker)
        // Get current date
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        Log.d("QUERY", (month+1).toString()+"-"+day+"-"+year)
        var query: Query = firestoreDB.collection("data").document("people").collection((month+1).toString()+"-"+day+"-"+year)


        spinnerBlueprints = findViewById<Spinner>(R.id.spinner_blueprints)
        val searchButton = findViewById<Button>(R.id.search_button)
        val cameraNumber = findViewById<EditText>(R.id.camera_number)
        val minTime= findViewById<TextView>(R.id.min_time)
        val maxTime= findViewById<TextView>(R.id.max_time)
        val date= findViewById<TextView>(R.id.date)


        date.text = (month+1).toString()+"-"+day+"-"+year

        timePicker.setMin(0)
        timePicker.setMax(24)
        timePicker.setCurrentEndValue(24)

        loadRecyclerView(query)
        addSpinnerContent()


        timePicker.setOnRubberRangePickerChangeListener(object: RubberRangePicker.OnRubberRangePickerChangeListener{
            override fun onProgressChanged(rangePicker: RubberRangePicker, startValue: Int, endValue: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(rangePicker: RubberRangePicker, isStartThumb: Boolean) {}
            override fun onStopTrackingTouch(rangePicker: RubberRangePicker, isStartThumb: Boolean) {
                if (rangePicker.getCurrentStartValue() < 12) {
                    minTime.text = rangePicker.getCurrentStartValue().toString()+"AM"
                } else {
                    minTime.text = rangePicker.getCurrentStartValue().toString()+"PM"
                }
                if (rangePicker.getCurrentEndValue() < 12) {
                    maxTime.text = "-"+rangePicker.getCurrentEndValue().toString()+"AM"
                } else {
                    maxTime.text = "-"+rangePicker.getCurrentEndValue().toString()+"PM"
                }
            }
        })

        searchButton.setOnClickListener {
            var query: Query = firestoreDB.collection("data").document("people").collection(date.text.toString())
            if(spinnerBlueprints.selectedItemPosition != 0) query = firestoreDB.collection("data").document("people").collection(date.text.toString()).document("area").collection(spinnerBlueprints.selectedItem.toString())

            if(cameraNumber.text.isNotEmpty()) query = query.whereEqualTo("camera", cameraNumber.text.toString())
            query = query.orderBy(FieldPath.documentId())

            loadRecyclerView(query)
        }

        date.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ _, year, month, dayOfMonth ->
                date.text = (month+1).toString()+"-"+dayOfMonth.toString()+"-"+year
            }, year, month,day)
            datePickerDialog.show()
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
        var adapter = ListAdapter(options)

        adapter.minHour = timePicker.getCurrentStartValue()
        adapter.maxHour = timePicker.getCurrentEndValue()
        // Item Click Listener
        adapter.onItemClick = {person ->
            val intent = Intent(this, PersonActivity::class.java).apply {
                putExtra("id", person.id)
            }
            startActivity(intent)
        }
        recyclerView.setAdapter(adapter)
    }

    fun addSpinnerContent() {
        val arrayList: ArrayList<String?> = ArrayList()
        arrayList.add("ALL")
        firestoreDB?.collection("users")?.document(userID!!)?.collection("blueprints")
            ?.get()
            ?.addOnCompleteListener {
                for (document in it.result?.documents!!) {
                    Log.d("Blueprints", "${document.id} => ${document.data}")
                    arrayList.add(document.get("name") as String?)

                }
                val arrayAdapter: ArrayAdapter<String?> = ArrayAdapter<String?>(this, android.R.layout.simple_spinner_item, arrayList)
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerBlueprints?.setAdapter(arrayAdapter)
                spinnerBlueprints?.setSelection(0)
            }
    }

}
