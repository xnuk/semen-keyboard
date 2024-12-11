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
			mutableListOf<Key>().apply {
				add(Key(value = "q", size = 1.5f))
				addAll(keySimpleRow("wfpbjlu"))
				add(Key(value = "y", size = 1.5f))
			},
			keySimpleRow("arstgmneio"),
			mutableListOf<Key>().apply {
				add(Key(value = "shift", label = "^", size = 1.5f))
				addAll(keySimpleRow("zxcdvkh"))
				add(Key(value = "bksp", label = "<=", size = 1.5f))
			},
			mutableListOf<Key>().apply {
				add(Key(value = "기호", size = 1.5f))
				add(Key(value = "한영", size = 1.5f))
				add(Key(value = " ", label = "space", size = 1.5f))
				add(Key(value = "."))
				add(Key(value = "\n", label = "enter", size = 1.5f))
			}
		),

		listOf(
			keySimpleRow("1234567890"),
			mutableListOf<Key>().apply {
				add(Key(value = "Q", size = 1.5f))
				addAll(keySimpleRow("WFPBJLU"))
				add(Key(value = "Y", size = 1.5f))
			},
			keySimpleRow("ARSTGMNEIO"),
			mutableListOf<Key>().apply {
				add(Key(value = "shift", label = "^", size = 1.5f))
				addAll(keySimpleRow("ZXCDVKH"))
				add(Key(value = "bksp", label = "<=", size = 1.5f))
			},
			mutableListOf<Key>().apply {
				add(Key(value = "기호", size = 1.5f))
				add(Key(value = "한영", size = 1.5f))
				add(Key(value = " ", label = "space", size = 3f))
				add(Key(value = "."))
				add(Key(value = "\n", label = "enter", size = 1.5f))
			}
		)
	))
