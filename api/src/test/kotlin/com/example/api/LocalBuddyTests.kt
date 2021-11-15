package com.example.api

import com.example.api.models.request.SigninRequest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class LocalBuddyTests {
    private val client = LocalBuddyClient

    @Test
    fun `POST user`(){
        val userCreds = SigninRequest(
            username = "divanshurox",
            password = "luckysmart"
        )
        runBlocking {
            val user = client.publicApi.signinUser(userCreds)
            assertEquals(userCreds.username,user.body()?.username)
        }
    }
}