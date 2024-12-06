import com.lanlinju.bencode.Bencode
import com.lanlinju.bencode.BencodeName
import kotlin.test.Test
import kotlin.test.assertEquals


data class A(
	@BencodeName("b") val b: Int
)

class UnitTest {
	@Test(timeout = 5000)
	fun testEncode() {
		assertEquals(Bencode.encodeToString(A(0)), "di0ee")
	}
}
