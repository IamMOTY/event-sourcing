package test

import event.Event
import http.HttpClient
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import service.Service
import storage.impl.InMemoryEventStorage
import java.util.concurrent.Executors

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseTest {

    private val executor = Executors.newSingleThreadExecutor()
    protected val storage = InMemoryEventStorage()
    protected val httpClient = HttpClient("http://localhost:$PORT")

    abstract fun initService(): Service

    fun InMemoryEventStorage.clear() {
        events.clear()
    }

    @BeforeAll
    fun beforeAll() {
        executor.execute {
            val service = initService()
            service.start(PORT)
        }
        Thread.sleep(1000) // wait a bit until service start
    }

    @BeforeEach
    fun beforeEach() {
        storage.clear()
    }

    @AfterAll
    fun afterAll() {
        executor.shutdown()
    }

    protected fun testWithEvents(vararg events: Event, testBody: () -> Unit) {
        events.forEach { storage.addEvent(it) }
        testBody()
    }

    companion object {
        const val PORT = 8000
    }
}