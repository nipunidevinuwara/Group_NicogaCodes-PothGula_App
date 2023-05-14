package com.example.adminfull

object Validator {
    fun validateInput(fullName:String,email:String,phone:String): Boolean {
        return!(fullName.isEmpty()||email.isEmpty()||phone.isEmpty())

    }
}