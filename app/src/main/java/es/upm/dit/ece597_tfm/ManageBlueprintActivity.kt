package es.upm.dit.ece597_tfm

import android.app.ActionBar
import android.content.ClipData
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_blueprint.*
import kotlinx.android.synthetic.main.activity_manage_blueprint.*
import kotlinx.android.synthetic.main.activity_manage_blueprint.camera_image
import kotlinx.android.synthetic.main.camera_layout.*


class ManageBlueprintActivity: AppCompatActivity() {

    var spinner: Spinner? = null
    var firestoreDB: FirebaseFirestore? = null
    lateinit var blueprintLayout: RelativeLayout
    var blueprintName: String? = null
    var camerasHashMap: HashMap<String, HashMap<String, String>> = HashMap<String, HashMap<String, String>>()
    var cameras: HashMap<String, String> = HashMap<String, String>()
    var userID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_blueprint)

        blueprintLayout = findViewById<RelativeLayout>(R.id.blueprint_layout)
        spinner = findViewById<Spinner>(R.id.blueprints_spinner)
        firestoreDB = FirebaseFirestore.getInstance()
        userID = FirebaseAuth.getInstance().currentUser?.uid

        addSpinnerContent()

        camera_image.setOnLongClickListener {
            val data: ClipData = ClipData.newPlainText("id", camera_select_id.text)
            val shadowBuilder = DragShadowBuilder(it)
            it.startDrag(data, shadowBuilder, it, 0)
        }

        val dragInBlueprintListener = View.OnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    true
                }

                DragEvent.ACTION_DRAG_LOCATION -> {
                    position_x.text = "x: "+event.x.toString()
                    position_y.text = "y: "+event.y.toString()
                    Log.d("ManageBlueprintActivity", "Over the: "+v.id+" View")

                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    true
                }
                DragEvent.ACTION_DROP -> {
                    // Gets the item containing the dragged data
                    val item: ClipData.Item = event.clipData.getItemAt(0)

                    // Gets the text data from the item.
                    val dragData = item.text

                    if (dragData.toString().equals("")) {
                        Toast.makeText(this, "Camera ID must not be empty", Toast.LENGTH_SHORT).show()
                    } else {
                        // Displays a message containing the dragged data.
                        Toast.makeText(this, "Dragged camera is " + dragData+ " at x:"+event.x+" y:"+event.y, Toast.LENGTH_SHORT).show()

                        var newView: View? = layoutInflater.inflate(R.layout.camera_layout, null, false)

                        blueprintLayout.addView(newView)
                        println("HEIGHT: "+blueprintLayout.height + " WIDTH: " +blueprintLayout.width)
                        newView!!.x = event.x
                        newView.y = event.y
                        newView.findViewById<TextView>(R.id.camera_id).text = dragData

                        newView.setOnLongClickListener {
                            val data: ClipData = ClipData.newPlainText("id", it.findViewById<TextView>(R.id.camera_id).text)
                            val shadowBuilder = DragShadowBuilder(it)
                            it.visibility = GONE
                            it.startDrag(data, shadowBuilder, it, 0)
                        }

                        var coordinates: StringBuilder = StringBuilder()
                        coordinates.append(event.x)
                        coordinates.append("-")
                        coordinates.append(event.y)
                        cameras.set(dragData.toString(), coordinates.toString())
                        camerasHashMap.set("cameras", cameras)

                        val userID = FirebaseAuth.getInstance().currentUser?.uid

                        Log.d("ManageBlueprintActivity", "blueprintName: "+ blueprintName.toString())
                        firestoreDB?.collection("users")?.document(userID!!)
                            ?.collection("blueprints")?.document(blueprintName.toString())
                            ?.update(camerasHashMap as Map<String, Any>)
                            ?.addOnSuccessListener {
                                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
                            }
                    }

                    true
                }
                else -> {
                    // An unknown action type was received.
                    Log.e("ManageBlueprintActivity", "Unknown action type received by OnDragListener.")

                    false
                }
            }
        }

        val dragInTrashListener = View.OnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    true
                }

                DragEvent.ACTION_DRAG_LOCATION -> {
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    true
                }
                DragEvent.ACTION_DROP -> {
                    // Gets the item containing the dragged data
                    val item: ClipData.Item = event.clipData.getItemAt(0)

                    // Gets the text data from the item.
                    val dragData = item.text

                    // Displays a message containing the dragged data.
                    Toast.makeText(this, "Deleted camera is " + dragData, Toast.LENGTH_SHORT).show()
                    cameras.remove(dragData.toString())

                    val userID = FirebaseAuth.getInstance().currentUser?.uid

                    Log.d("ManageBlueprintActivity", "blueprintName: "+ blueprintName.toString())
                    firestoreDB?.collection("users")?.document(userID!!)
                        ?.collection("blueprints")?.document(blueprintName.toString())
                        ?.update(camerasHashMap as Map<String, Any>)
                        ?.addOnSuccessListener {
                            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
                        }

                    true
                }
                else -> {
                    // An unknown action type was received.
                    Log.e("ManageBlueprintActivity", "Unknown action type received by OnDragListener.")


                    false
                }
            }
        }

        trash_image.setOnDragListener(dragInTrashListener)
        blueprintLayout.setOnDragListener(dragInBlueprintListener)

    }

    fun addSpinnerContent() {
        val arrayList: ArrayList<String?> = ArrayList()

        firestoreDB?.collection("users")?.document(userID!!)?.collection("blueprints")
            ?.get()
            ?.addOnCompleteListener {
                for (document in it.result?.documents!!) {
                    Log.d("Blueprints", "${document.id} => ${document.data}")
                    arrayList.add(document.get("name") as String?)

                }
                val arrayAdapter: ArrayAdapter<String?> = ArrayAdapter<String?>(this, android.R.layout.simple_spinner_item, arrayList)
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner?.setAdapter(arrayAdapter)
                spinner?.setSelection(0)

                spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        Log.d("SPINNER", spinner?.getItemAtPosition(position).toString())
                        reloadImage(position)
                        blueprintName = spinner?.getItemAtPosition(position).toString()
                    }

                }
            }
    }

    fun reloadImage (position: Int) {
        val storage = FirebaseStorage.getInstance()

        val reference: StringBuilder = StringBuilder()
        reference.append("gs://ece-597---tfm.appspot.com/")
        reference.append(FirebaseAuth.getInstance().currentUser?.uid)
        reference.append("/")
        reference.append(spinner?.getItemAtPosition(position).toString())
        // Create a reference to a file from a Google Cloud Storage URI
        val gsReference = storage.getReferenceFromUrl(reference.toString())

        Glide.with(this)
            .load(gsReference)
            .into(blueprint_image)

        camerasHashMap = HashMap()
        cameras = HashMap()
        blueprintLayout.removeAllViewsInLayout()
        reloadCameras(spinner?.getItemAtPosition(position).toString())

    }

    fun reloadCameras (blueprint: String) {


        firestoreDB?.collection("users")?.document(userID!!)?.collection("blueprints")
            ?.document(blueprint)
            ?.get()
            ?.addOnCompleteListener {
                var documentSnapshot = it.result
                var documentCameras = documentSnapshot?.get("cameras")
                if (documentCameras != null) {
                    (documentCameras as HashMap<String, String>).forEach { (camera, coordinates) ->
                        println("$camera at $coordinates")

                        var auxCoordinates = coordinates.split("-")

                        var newView: View? = layoutInflater.inflate(R.layout.camera_layout, null, false)

                        blueprintLayout.addView(newView)
                        newView!!.x = auxCoordinates[0].toFloat()
                        newView.y = auxCoordinates[1].toFloat()
                        newView.findViewById<TextView>(R.id.camera_id).text = camera

                        newView.setOnLongClickListener {
                            val data: ClipData = ClipData.newPlainText("id", it.findViewById<TextView>(R.id.camera_id).text)
                            val shadowBuilder = DragShadowBuilder(it)
                            it.visibility = GONE
                            it.startDrag(data, shadowBuilder, it, 0)
                        }

                        cameras.set(camera, coordinates)

                    }
                }
                camerasHashMap.set("cameras", cameras)
            }
    }
}
