package nl.entreco.domain.beta

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 17/03/2018.
 */
class DonationTest {

    private lateinit var subject: Donation

    @Test
    fun getTitle() {
        givenSubject(title = "MasterCaller")
        assertEquals("MasterCaller", subject.title)
    }

    @Test
    fun getDescription() {
        givenSubject(description = "Some description")
        assertEquals("Some description", subject.description)
    }

    @Test
    fun getSku() {
        givenSubject(sku = "1234567890")
        assertEquals("1234567890", subject.sku)
    }

    @Test
    fun getPrice() {
        givenSubject(price = "12.00")
        assertEquals("12.00", subject.price)
    }

    @Test
    fun getVotes() {
        givenSubject(votes = 500)
        assertEquals(500, subject.votes)
    }

    @Test
    fun getPriceCurrencyCode() {
        givenSubject(code = "$")
        assertEquals("$", subject.priceCurrencyCode)
    }

    @Test
    fun getPriceMicros() {
        givenSubject(micros = 100000)
        assertEquals(100000, subject.priceMicros)
    }

    private fun givenSubject(
        title: String = "donation",
        description: String = "desc",
        sku: String = "sku",
        price: String = "5",
        votes: Int = 0,
        code: String = "EUR",
        micros: Long = 100000,
    ) {
        subject = Donation(title, description, sku, price, votes, code, micros)
    }

}