package es.upm.dit.ece597_tfm

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jem.rubberpicker.RubberRangePicker
import java.lang.StringBuilder
import java.util.*


class ListActivity  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        // Get current date
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        //Initialize Firebase app
        var firestoreDB = FirebaseFirestore.getInstance()
        Log.d("QUERY", (month+1).toString()+"-"+day+"-"+year)
        var query: Query = firestoreDB.collection("data").document("people").collection((month+1).toString()+"-"+day+"-"+year)

        loadRecyclerView(query)

        val searchButton = findViewById<Button>(R.id.search_button)
        val cameraNumber = findViewById<EditText>(R.id.camera_number)
        val timePicker = findViewById<RubberRangePicker>(R.id.time_picker)
        val minTime= findViewById<TextView>(R.id.min_time)
        val maxTime= findViewById<TextView>(R.id.max_time)
        val date= findViewById<TextView>(R.id.date)

        date.text = (month+1).toString()+"-"+day+"-"+year

        timePicker.setMin(0)
        timePicker.setMax(24)
        timePicker.setCurrentEndValue(24)

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
                    maxTime.text = rangePicker.getCurrentEndValue().toString()+"AM"
                } else {
                    maxTime.text = "-"+rangePicker.getCurrentEndValue().toString()+"PM"
                }
            }
        })

        searchButton.setOnClickListener {
            var query: Query = firestoreDB.collection("data").document("people").collection(date.text.toString())
            Log.d("ListActivity", cameraNumber.text.toString())
            if(cameraNumber.text.isNotEmpty()) query = query.whereEqualTo("camera", cameraNumber.text.toString())
            query = query.whereGreaterThanOrEqualTo("hour", timePicker.getCurrentStartValue())
            query = query.whereLessThanOrEqualTo("hour", timePicker.getCurrentEndValue()).orderBy("hour", Query.Direction.DESCENDING).orderBy("minute", Query.Direction.DESCENDING)

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
