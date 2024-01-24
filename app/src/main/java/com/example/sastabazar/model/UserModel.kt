package com.example.sastabazar.model

class UserModel {
    var firstName:String = ""
    var lastName:String = ""
    var password:String = ""
    var email:String = ""

    constructor()

    constructor(firstName: String, lastName: String,  email: String ,password: String) {
        this.firstName = firstName
        this.lastName = lastName
        this.password = password
        this.email = email
    }
}