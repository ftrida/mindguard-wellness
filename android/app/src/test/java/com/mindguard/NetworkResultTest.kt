package com.mindguard

import com.mindguard.core.network.NetworkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkResultTest {

    @Test
    fun `test network result success encapsulation`() {
        val result = NetworkResult.Success("Data Payload")
        assertTrue(result is NetworkResult.Success)
        assertEquals("Data Payload", result.data)
    }

    @Test
    fun `test network result error encapsulation`() {
        val result = NetworkResult.Error("Unauthorized request", 401)
        assertTrue(result is NetworkResult.Error)
        assertEquals("Unauthorized request", result.message)
        assertEquals(401, result.code)
    }
}
