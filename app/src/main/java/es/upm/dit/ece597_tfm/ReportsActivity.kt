package es.upm.dit.ece597_tfm

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import co.csadev.kellocharts.listener.ColumnChartOnValueSelectListener
import co.csadev.kellocharts.model.*
import co.csadev.kellocharts.util.ChartUtils
import co.csadev.kellocharts.view.ColumnChartView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_reports.*
import java.util.*
import kotlin.collections.ArrayList


class ReportsActivity: AppCompatActivity() {

    lateinit var chart:ColumnChartView
    lateinit var chart2:ColumnChartView
    lateinit var spinner: Spinner
    lateinit var switch: Switch
    lateinit var date: TextView
    lateinit var spinnerBlueprints: Spinner
    var firestoreDB = FirebaseFirestore.getInstance()
    var userID: String? = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        spinnerBlueprints = findViewById<Spinner>(R.id.spinner_blueprints_reports)
        addSpinnerContent()

        spinner =  findViewById<Spinner>(R.id.report_spinner)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setReport(position+1, 1, "")
                if(position == 2) {
                    switch.visibility = VISIBLE
                } else {
                    switch.visibility = GONE
                }
            }

        }

        chart = findViewById<ColumnChartView>(R.id.chart)
        chart.onValueTouchListener = this.ValueTouchListener()
        chart2 = findViewById<ColumnChartView>(R.id.chart2)

        switch = findViewById<Switch>(R.id.switch_report)
        switch.isChecked = true
        switch.text = "Camera"
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                switch.text = "Camera"
            } else {
                switch.text = "Time"
            }
        }

        val reportBtn = findViewById<Button>(R.id.reload_button)
        reportBtn.setOnClickListener {
            setReport(spinner.selectedItemPosition+1, 1, "")
        }

        // Get current date
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        date = findViewById<TextView>(R.id.date_picker)

        date.text = (month + 1).toString() + "-" + day + "-" + year

        date.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    date.text = (month + 1).toString() + "-" + dayOfMonth.toString() + "-" + year
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }



    }

    private fun report1(document: Map<String, Any>, chartN: Int) {
        val numColumns = document.size
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        val columns = ArrayList<Column>()
        var values: MutableList<SubcolumnValue>
        val xAxisValues: ArrayList<AxisValue> = ArrayList()
        var i = 0
        for ((camera, detections) in document) {
            var xAxisValue = AxisValue(i)
            xAxisValue.label = camera.toCharArray()
            xAxisValues.add(xAxisValue)
            i++
            values = ArrayList()
            values.add( SubcolumnValue(
                (detections as Long).toFloat(),
                ChartUtils.pickColor()))

            val column = Column(values)
            column.hasLabels = false
            column.hasLabelsOnlyForSelected = true
            columns.add(column)
        }


        val data = ColumnChartData(columns, isHorizontal = false )

        val axisX = Axis()
        axisX.values = xAxisValues
        val axisY = Axis(hasLines = true)
        axisX.name = "Camera ID"
        axisY.name = "Detections"
        data.axisXBottom = axisX
        data.axisYLeft = axisY

        chart?.columnChartData = data
        chart2?.visibility = GONE
    }

    private fun report2(document: Map<String, Any>, chartN: Int) {
        val numColumns = 24
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        val columns = ArrayList<Column>()
        var values: MutableList<SubcolumnValue>
        val xAxisValues: ArrayList<AxisValue> = ArrayList()
        for (i in 0 until numColumns) {
            xAxisValues.add(AxisValue(i))
            values = ArrayList()
            values.add( SubcolumnValue(
                ((document[i.toString()]) as Long).toFloat(),
                ChartUtils.pickColor()))

            val column = Column(values)
            column.hasLabels = false
            column.hasLabelsOnlyForSelected = true
            columns.add(column)
        }


        val data = ColumnChartData(columns, isHorizontal = false )

        val axisX = Axis()
        axisX.values = xAxisValues
        val axisY = Axis(hasLines = true)
        axisX.name = "DayTime (hours)"
        axisY.name = "Detections"
        data.axisXBottom = axisX
        data.axisYLeft = axisY

        chart?.columnChartData = data
        chart2?.visibility = GONE
    }

    private fun report3(document: Map<String, Any>, chartN: Int) {

        val numColumns = document.size
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        val columns = ArrayList<Column>()
        var values: MutableList<SubcolumnValue>
        val xAxisValues: ArrayList<AxisValue> = ArrayList()
        var i = 0
        for ((id, detections) in document) {
            var xAxisValue = AxisValue(i)
            xAxisValue.label = id.toCharArray()
            xAxisValues.add(xAxisValue)
            i++
            values = ArrayList()
            values.add( SubcolumnValue(
                (detections as Long).toFloat(),
                ChartUtils.pickColor()))

            val column = Column(values)
            column.hasLabels = false
            column.hasLabelsOnlyForSelected = true
            columns.add(column)
        }


        val data = ColumnChartData(columns, isHorizontal = false )

        val axisX = Axis()
        axisX.values = xAxisValues
        val axisY = Axis(hasLines = true)
        axisX.name = "Person ID"
        axisY.name = "Detections"
        data.axisXBottom = axisX
        data.axisYLeft = axisY

        chart?.columnChartData = data
        chart2?.visibility = GONE
    }

    private fun report4(document: Map<String, Any>, chartN: Int, id: String) {

        var document2: Map<String, Any> = document.get(id) as Map<String, Any>

        val numColumns = 24
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        val columns = ArrayList<Column>()
        var values: MutableList<SubcolumnValue>
        val xAxisValues: ArrayList<AxisValue> = ArrayList()
        for (i in 0 until numColumns) {
            xAxisValues.add(AxisValue(i))
            values = ArrayList()
            values.add( SubcolumnValue(
                ((document2[i.toString()]) as Long).toFloat(),
                ChartUtils.pickColor()))

            val column = Column(values)
            column.hasLabels = false
            column.hasLabelsOnlyForSelected = true
            columns.add(column)
        }


        val data = ColumnChartData(columns, isHorizontal = false )

        val axisX = Axis()
        axisX.values = xAxisValues
        val axisY = Axis(hasLines = true)
        axisX.name = "DayTime (hours)"
        axisY.name = "Detections"
        data.axisXBottom = axisX
        data.axisYLeft = axisY

        chart2.visibility = VISIBLE
        chart2?.columnChartData = data

    }

    private fun report5(document: Map<String, Any>, chartN: Int, id: String) {

        var document2: Map<String, Any> = document.get(id) as Map<String, Any>

        val numColumns = document2.size
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        val columns = ArrayList<Column>()
        var values: MutableList<SubcolumnValue>
        val xAxisValues: ArrayList<AxisValue> = ArrayList()
        var i = 0
        for ((camera, detections) in document2) {
            var xAxisValue = AxisValue(i)
            xAxisValue.label = camera.toCharArray()
            xAxisValues.add(xAxisValue)
            i++
            values = ArrayList()
            values.add( SubcolumnValue(
                (detections as Long).toFloat(),
                ChartUtils.pickColor()))

            val column = Column(values)
            column.hasLabels = false
            column.hasLabelsOnlyForSelected = true
            columns.add(column)
        }


        val data = ColumnChartData(columns, isHorizontal = false )

        val axisX = Axis()
        axisX.values = xAxisValues
        val axisY = Axis(hasLines = true)
        axisX.name = "Camera ID"
        axisY.name = "Detections"
        data.axisXBottom = axisX
        data.axisYLeft = axisY

        chart2.visibility = VISIBLE
        chart2?.columnChartData = data

    }

    private fun report6(document: Map<String, Any>, chartN: Int, id: String) {

        var document2: Map<String, Any> = document.get(id) as Map<String, Any>

        val numColumns = 24
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        val columns = ArrayList<Column>()
        var values: MutableList<SubcolumnValue>
        val xAxisValues: ArrayList<AxisValue> = ArrayList()
        for (i in 0 until numColumns) {
            xAxisValues.add(AxisValue(i))
            values = ArrayList()
            values.add( SubcolumnValue(
                ((document2[i.toString()]) as Long).toFloat(),
                ChartUtils.pickColor()))

            val column = Column(values)
            column.hasLabels = false
            column.hasLabelsOnlyForSelected = true
            columns.add(column)
        }


        val data = ColumnChartData(columns, isHorizontal = false )

        val axisX = Axis()
        axisX.values = xAxisValues
        val axisY = Axis(hasLines = true)
        axisX.name = "DayTime (hours)"
        axisY.name = "Detections"
        data.axisXBottom = axisX
        data.axisYLeft = axisY

        chart2.visibility = VISIBLE
        chart2?.columnChartData = data

    }

    private inner class ValueTouchListener : ColumnChartOnValueSelectListener {

        override fun onValueSelected(columnIndex: Int, subcolumnIndex: Int, value: SubcolumnValue) {

            when (spinner.selectedItemPosition+1) {
                1 -> {
                    setReport(4, 2, String(chart.columnChartData.axisXBottom!!.values[columnIndex].label!!))
                    Toast.makeText(baseContext, "Selected Camera: " + String(chart.columnChartData.axisXBottom!!.values[columnIndex].label!!), Toast.LENGTH_SHORT).show()
                }
                3 -> {
                    if(switch.isChecked) {
                        setReport(5, 2, String(chart.columnChartData.axisXBottom!!.values[columnIndex].label!!))
                        Toast.makeText(baseContext, "Selected Person: " + String(chart.columnChartData.axisXBottom!!.values[columnIndex].label!!), Toast.LENGTH_SHORT).show()
                    } else {
                        setReport(6, 2, String(chart.columnChartData.axisXBottom!!.values[columnIndex].label!!))
                        Toast.makeText(baseContext, "Selected Person: " + String(chart.columnChartData.axisXBottom!!.values[columnIndex].label!!), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        override fun onValueDeselected() {}

    }

    fun setReport(report: Int, chartN: Int, id: String) {
        var reportDocument = "report"+report
        if(spinnerBlueprints.selectedItem == null) spinnerBlueprints.setSelection(0)
        if(spinnerBlueprints.selectedItemPosition == 0) {
            firestoreDB.collection("data").document("reports").collection(date.text.toString())
                .document(reportDocument)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        Log.d("ReportsActivity", "DocumentSnapshot data: ${document.data}")
                        when (report) {
                            1 -> report1(document.data as Map<String, Any>, chartN)
                            2 -> report2(document.data as Map<String, Any>, chartN)
                            3 -> report3(document.data as Map<String, Any>, chartN)
                            4 -> report4(document.data as Map<String, Any>, chartN, id)
                            5 -> report5(document.data as Map<String, Any>, chartN, id)
                            6 -> report6(document.data as Map<String, Any>, chartN, id)
                        }
                    } else {
                        Log.d("ReportsActivity", "No such document")
                    }
                }
        } else {
            firestoreDB.collection("data").document("reports").collection("area").document(spinnerBlueprints.selectedItem.toString()).collection(date.text.toString())
                .document(reportDocument)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        Log.d("ReportsActivity", "DocumentSnapshot data: ${document.data}")
                        when (report) {
                            1 -> report1(document.data as Map<String, Any>, chartN)
                            2 -> report2(document.data as Map<String, Any>, chartN)
                            3 -> report3(document.data as Map<String, Any>, chartN)
                            4 -> report4(document.data as Map<String, Any>, chartN, id)
                            5 -> report5(document.data as Map<String, Any>, chartN, id)
                            6 -> report6(document.data as Map<String, Any>, chartN, id)
                        }
                    } else {
                        Log.d("ReportsActivity", "No such document")
                    }
                }
        }
    }

    fun addSpinnerContent() {
        val arrayList: java.util.ArrayList<String?> = java.util.ArrayList()
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