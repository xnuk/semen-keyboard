package kr.xnu.keyboard.semen

import kotlinx.serialization.Serializable

@Serializable
data class Key(
	val value: String,
	val label: String = value,
	val options: String? = null,
)

@Serializable
data class Keyboard(val engine: String, val layers: List<List<List<Key>>>)

private fun keySimple(value: String): Key = Key(value = value)

private fun keySimpleRow(value: CharSequence): List<Key> {
	val ret = mutableListOf<Key>()
	for (char in value.chars()) {
		ret.add(keySimple(char.toChar().toString()))
	}
	return ret
}

val defaultKeyboard = Keyboard(
	engine = "Direct",
	layers = listOf(
		listOf(
			keySimpleRow("qwfpbjluy;"),
			keySimpleRow("arstgmneio"),
			keySimpleRow("zxcdvkh,./"),
		),
		listOf(
			keySimpleRow("QWFPBJLUY:"),
			keySimpleRow("ARSTGMNEIO"),
			keySimpleRow("ZXCDVKH<>?"),
		),
	),
)
