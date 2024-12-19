import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kr.xnu.keyboard.semen.EngineDirectKey
import kr.xnu.keyboard.semen.defaultKeyboard
import kotlin.test.Test
import kotlin.test.assertEquals

class UnitTest {
	@Test(timeout = 5000)
	fun testEncode() {
		println(Json.encodeToString(defaultKeyboard))
		assertEquals(
			"str",
			EngineDirectKey.Str.serializer().descriptor.serialName
		)
	}
}
