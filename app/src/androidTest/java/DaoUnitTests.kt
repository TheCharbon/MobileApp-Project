import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.mobileapp_project.data.Entry
import com.example.mobileapp_project.data.EntryDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.example.mobileapp_project.data.FinanceDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.IOException
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobileapp_project.data.CategoryExpense
import org.junit.runner.RunWith

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
    fun testInsert() = runBlocking {

        entryDao.insert(item1)


        val items = entryDao.getAll().first()


        assertEquals(1, items.size)
        assertEquals(item1.copy(id = items[0].id), items[0])
    }

    @Test
    fun testGetItem() = runBlocking {
        val novemberTimestamp = 1732745615000L
        val novemberItem = item1.copy(timestamp = novemberTimestamp)

        entryDao.insert(novemberItem)

        val items = entryDao.getItem("2024-11").first()


        assertEquals(1, items.size)
        assertEquals(novemberItem.copy(id = items[0].id), items[0])
    }


    @Test
    fun testGetExpensesGroupedByCategory() = runBlocking {
        val item1 = Entry(0, true, "Food", "test", 34.20, 1732745615) // Expense = 1
        val item2 = Entry(0, true, "Food", "test2", 15.80, 1732745616) // Expense = 1
        val item3 = Entry(0, false, "Entertainment", "test3", 50.75, 1732745617) // Expense = 0
        val item4 = Entry(0, true, "Utilities", "test4", 100.00, 1732745618) // Expense = 1

        entryDao.insert(item1)
        entryDao.insert(item2)
        entryDao.insert(item3)
        entryDao.insert(item4)

        val expenses = entryDao.getExpensesGroupedByCategory().first()

        val expected = listOf(
            CategoryExpense("Food", 50.00),
            CategoryExpense("Utilities", 100.00)
        )

        assertEquals("Expected ${expected.size} categories, found ${expenses.size}", expected.size, expenses.size)

        for (i in expected.indices) {
            assertEquals("Category names don't match", expected[i].category, expenses[i].category)

            val expectedTotal = expected[i].totalExpenses.toDouble()
            val actualTotal = expenses[i].totalExpenses.toDouble()
            
            assertEquals("Category totals don't match", expectedTotal, actualTotal, 0.01)
        }
    }
}