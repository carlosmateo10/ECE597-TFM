package es.upm.dit.ece597_tfm

import android.graphics.Camera

class Person {

    var id : String? = null
    var camera : String? = null
    var timestamp : String? = null

    constructor() {}

    constructor(id: String?, camera: String?, timestamp: String?) {
        this.id= id
        this.camera = camera
        this.timestamp = timestamp
    }
}