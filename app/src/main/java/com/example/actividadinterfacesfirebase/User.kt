package com.example.actividadinterfacesfirebase

import android.net.Uri
import java.io.Serializable


class User : Serializable {
    var firstName: String? = null
    var lastName: String? = null
    var age: String? = null
    var phone: String? = null
    constructor() {}

    constructor(name: String?, lastName : String?, age: String, phone: String){
        this.firstName = name
        this.lastName=lastName
        this.age = age
        this.phone=phone
    }
}

/*class User : Serializable {
    var firstName: String? = null
    var lastName: String? = null
    var age: String? = null
    var phone: String? = null

    constructor(){}

    constructor(name: String?, lastName : String?) {
        this.firstName = name
        this.lastName=lastName
    }
    constructor(name: String?, lastName : String?, age: String, phone: String){
        this.firstName = name
        this.lastName=lastName
        this.age = age
        this.phone=phone
    }


}*/