package kr.xnu.keyboard.semen


data class Key(val label: Char, val value: String, val layoutOptions: String)
data class Keyboard(val engine: String, val layers: List<List<List<Key>>>)

private fun keySimple(value: Char): Key =
	Key(label = value, value = value.toString(), layoutOptions = "")

private fun keySimpleRow(value: CharSequence): List<Key> {
	val ret = mutableListOf<Key>()
	for (char in value.chars()) {
		ret.add(keySimple(char.toChar()))
	}
	return ret
}

val defaultKeyboard = Keyboard(
	engine = "Direct", layers = listOf(
		listOf(
			keySimpleRow("qwfpbjluy;"),
			keySimpleRow("arstgmneio"),
			keySimpleRow("zxcdvkh,./"),
		),
		listOf(
			keySimpleRow("QWFPBJLUY:"),
			keySimpleRow("ARSTGMNEIO"),
			keySimpleRow("ZXCDVKH<>?"),
		)
	)
)
