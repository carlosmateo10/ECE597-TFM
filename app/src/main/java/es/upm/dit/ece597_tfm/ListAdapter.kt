package es.upm.dit.ece597_tfm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class ListAdapter (options: FirestoreRecyclerOptions<Person?>) : FirestoreRecyclerAdapter<Person?, ListAdapter.ListViewHolder?>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.list_item, parent, false)

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int, model: Person) {
        holder!!.personId.text = model.id
        holder.cameraId.text = model.camera
        holder.timestamp.text = model.timestamp
    }

    inner class ListViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var personId: TextView
        internal var cameraId: TextView
        internal var timestamp: TextView

        init {
            personId = view.findViewById(R.id.person_id)
            cameraId = view.findViewById(R.id.camera_id)
            timestamp = view.findViewById(R.id.timestamp)

        }
    }
}
