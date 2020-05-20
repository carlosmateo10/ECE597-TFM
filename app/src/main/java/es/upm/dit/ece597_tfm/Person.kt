package es.upm.dit.ece597_tfm

import com.google.firebase.Timestamp
import java.util.*

class Person {

    var id : String? = null
    var camera : String? = null
    var month : Int? = null
    var day : Int? = null
    var hour : Int? = null
    var minute : Int? = null

    constructor() {}

    constructor(id: String?, camera: String?, month: Int?, day: Int?, hour: Int?, minute: Int?) {
        this.id= id
        this.camera = camera
        this.month = month
        this.day = day
        this.hour = hour
        this.minute = minute
    }
}