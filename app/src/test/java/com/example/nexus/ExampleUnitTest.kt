package com.example.nexus

import com.example.nexus.data.repositories.ListRepository
import org.junit.Test

import org.junit.Assert.*
import org.mockito.kotlin.mock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test(){
        val repo = mock<ListRepository>()
        assertEquals(4, 2 + 2)
    }
}