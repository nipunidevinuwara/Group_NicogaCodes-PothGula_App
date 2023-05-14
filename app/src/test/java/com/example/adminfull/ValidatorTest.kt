package com.example.adminfull

import com.google.common.truth.ExpectFailure.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.Assert.*


@RunWith(JUnit4::class)
class ValidatorTest{
    @Test
    fun whenInputIsValid(){
        val fullName="John Does"
        val email="xxx.@gmail.com"
        val phone="0776117977"

        val result=Validator.validateInput(fullName, email, phone)
        assertThat(result).isEqualTo(true)
    }


    @Test
    fun whenInputIsInvalid(){
        val fullName=""
        val email=""
        val phone=""

        val result=Validator.validateInput(fullName, email, phone)
        assertThat(result).isEqualTo(false)
    }
}