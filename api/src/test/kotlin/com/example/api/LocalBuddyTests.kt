package com.example.api

import kotlinx.coroutines.runBlocking
import org.junit.Test

class LocalBuddyTests {
    private val client = LocalBuddyClient

    @Test
    fun `POST orders`(){
        runBlocking {
            val orders = client.authApi.getOrdersList("619f8462fd08f34d812116fe")
            println(orders.toString())
        }
    }
}