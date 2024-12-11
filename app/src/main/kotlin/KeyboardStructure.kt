package kr.xnu.keyboard.semen

import kotlinx.serialization.Serializable

@Serializable
data class Key(
	val value: String,
	val label: String = value,
	val subLabel: String? = null,
	val options: String? = null,
)

@Serializable
data class Engine(
	val type: String,
	val param: Map<String, String>,
)

@Serializable
data class Keyboard(val engine: Engine, val layers: List<List<List<Key>>>)

private fun keySimpleRow(value: CharSequence): List<Key> {
	val ret = mutableListOf<Key>()
	for (char in value.chars()) {
		ret.add(Key(char.toChar().toString()))
	}
	return ret
}

val engineDirect = Engine("direct", mapOf())

val defaultKeyboard = Keyboard(
	engine = engineDirect,
	layers = listOf(
		listOf(
			keySimpleRow("1234567890"),
			keySimpleRow("qwfpbjluy"),
			keySimpleRow("arstgmneio"),
			listOf(
				Key("shift", "^"),
				Key("z"),
				Key("x"),
				Key("c"),
				Key("d"),
				Key("v"),
				Key("k"),
				Key("h"),
				Key("bksp", "<=")
			),
			listOf(
				Key("기호"),
				Key("한영"),
				Key("space"),
				Key("."),
				Key("enter")
			)
		),
		listOf(
			keySimpleRow("1234567890"),
			keySimpleRow("QWFPBJLUY"),
			keySimpleRow("ARSTGMNEIO"),
			listOf(
				Key("shift", "^"),
				Key("Z"),
				Key("X"),
				Key("C"),
				Key("D"),
				Key("V"),
				Key("K"),
				Key("H"),
				Key("bksp", "<=")
			),
			listOf(
				Key("기호"),
				Key("한영"),
				Key("space"),
				Key("."),
				Key("enter")
			)
		),
	),
)
