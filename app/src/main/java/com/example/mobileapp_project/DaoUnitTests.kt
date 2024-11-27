package com.example.mobileapp_project

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobileapp_project.data.Entry
import com.example.mobileapp_project.data.EntryDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.example.mobileapp_project.data.FinanceDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DaoUnitTests {
    private lateinit var entryDao: EntryDao
    private lateinit var financeDatabase: FinanceDatabase
    private val item1 = Entry(0, true, "Food", "test", 34.20, 1732745615)
    private val item2 = Entry(1, false, "Food", "test", 50.75, 1732745615)

    @Before
    fun createDb() {
        financeDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FinanceDatabase::class.java
        ).allowMainThreadQueries().build()
        entryDao = financeDatabase.entryDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        financeDatabase.close()
    }

    @Test
    fun testInsert() = runBlocking{
        entryDao.insert(item1)
        val items = entryDao.getAll().first()
        assertEquals(item1, items[0])
    }

    @Test
    fun testGetItem() = runBlocking{
        entryDao.insert(item1)
        val items = entryDao.getItem("november").first()
        assertEquals(item1, items[0])
    }

    @Test
    fun testGetAll() = runBlocking{
        entryDao.insert(item1)
        entryDao.insert(item2)
        val items = entryDao.getAll().first()
        val expected = listOf(item1, item2)
        assertEquals(expected, items)
    }
}