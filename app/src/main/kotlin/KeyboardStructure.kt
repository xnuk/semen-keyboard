package kr.xnu.keyboard.semen

import kotlinx.serialization.Serializable

@Serializable
data class Key(
	val value: String,
	val label: String = value,
	val subLabel: String? = null,
	val size: Float = 1f,
	val options: String? = null,
)

@Serializable
data class Engine(
	val type: String,
	val param: Map<String, String>,
)

@Serializable
data class Keyboard(val engine: Engine, val layers: List<List<List<Key>>>)

private fun keySimpleRow(values: String): List<Key> {
	val ret = mutableListOf<Key>()
	for (value in values.chars()) {
		ret.add(
			Key(
				value.toChar().toString()
			)
		)
	}
	return ret
}

val defaultKeyboard =
	Keyboard(engine = Engine("direct", mapOf()), layers = listOf(
		listOf(
			keySimpleRow("1234567890"),
			keySimpleRow("qwfpbjluy"),
			keySimpleRow("arstgmneio"),
			mutableListOf<Key>().apply {
				add(Key(value = "shift", label = "^", size = 1.5f))
				addAll(keySimpleRow("zxcdvkh"))
				add(Key(value = "bksp", label = "<=", size = 1.5f))
			},
			mutableListOf<Key>().apply {
				add(Key(value = "기호", size = 1.5f))
				add(Key(value = "한영", size = 1.5f))
				add(Key(value = "space", size = 1.5f))
				add(Key(value = "."))
				add(Key(value = "enter", size = 1.5f))
			}
		),

		listOf(
			keySimpleRow("1234567890"),
			keySimpleRow("QWFPBJLUY"),
			keySimpleRow("ARSTGMNEIO"),
			mutableListOf<Key>().apply {
				add(Key(value = "shift", label = "^", size = 1.5f))
				addAll(keySimpleRow("ZXCDVKH"))
				add(Key(value = "bksp", label = "<=", size = 1.5f))
			},
			mutableListOf<Key>().apply {
				add(Key(value = "기호", size = 1.5f))
				add(Key(value = "한영", size = 1.5f))
				add(Key(value = "space", size = 3f))
				add(Key(value = "."))
				add(Key(value = "enter", size = 1.5f))
			}
		)
	))
