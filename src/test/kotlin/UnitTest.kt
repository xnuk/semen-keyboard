import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kr.xnu.keyboard.semen.defaultKeyboard
import kotlin.test.Test

class UnitTest {
	@Test(timeout = 5000)
	fun testEncode() {
		println(Json.encodeToString(defaultKeyboard))
	}
}
