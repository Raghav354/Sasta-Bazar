package com.example.sastabazar.model

class UserModel {
    var firstName: String = ""
    var lastName: String = ""
    var password: String = ""
    var email: String = ""
    var phoneNumber: String = ""
    var address: String = ""

    constructor()

    constructor(firstName: String, lastName: String, email: String, password: String) {
        this.firstName = firstName
        this.lastName = lastName
        this.password = password
        this.email = email
    }

    constructor(
        firstName: String,
        lastName: String,
        password: String,
        email: String,
        phoneNumber: String
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.password = password
        this.email = email
        this.phoneNumber = phoneNumber
    }
}