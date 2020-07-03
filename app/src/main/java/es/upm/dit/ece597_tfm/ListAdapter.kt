package es.upm.dit.ece597_tfm

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class ListAdapter (options: FirestoreRecyclerOptions<Person?>) : FirestoreRecyclerAdapter<Person?, ListAdapter.ListViewHolder?>(options) {

    var minHour: Int = 0
    var maxHour: Int = 24
    var onItemClick: ((Person) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int, model: Person) {
        holder.personId.text = "ID: "+model.id
        holder.cameraId.text = model.camera
        var hour = ""
        var minute = ""
        if (model.hour!! < 10) {
            hour = "0${model.hour}"
        } else {
            hour = "${model.hour}"
        }
        if (model.minute!! < 10) {
            minute = "0${model.minute}"
        } else {
            minute = "${model.minute}"
        }
        holder.timestamp.text = hour+":"+minute

        if (model.hour!! < minHour || model.hour!! > maxHour) {
            System.out.println("LO QUITO BECAUSE: ${model.hour} < ${minHour} or ${model.hour} > ${maxHour}")
            var layout0:ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 0)
            holder.layout.layoutParams = layout0
        }
    }

    inner class ListViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var personId: TextView
        internal var cameraId: TextView
        internal var timestamp: TextView
        internal var layout: ConstraintLayout
        init {
            personId = view.findViewById(R.id.person_id)
            cameraId = view.findViewById(R.id.camera_id)
            timestamp = view.findViewById(R.id.timestamp)
            layout = view.findViewById(R.id.detection_item_layout)

            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(adapterPosition))
            }
        }
    }

    fun setMaxHour_(maxHour: Int) {
        this.maxHour = maxHour
    }

    fun setMinHour_(minHour: Int) {
        this.minHour = minHour
    }
}
