package kr.xnu.keyboard.semen

import kotlinx.serialization.Serializable

@Serializable
enum class Command {
	Backspace,
	Enter
}

@Serializable
sealed class KeyVal() {
	@Serializable
	class Str(val str: String) : KeyVal()

	@Serializable
	class TO(val layer: Int) : KeyVal()

	@Serializable
	class LT(val short: KeyVal, val long: KeyVal) : KeyVal()

	@Serializable
	class OneShot(val layer: Int) : KeyVal()

	@Serializable
	class Cmd(val cmd: Command) : KeyVal()

	@Serializable
	class Seq(val sequence: List<KeyVal>) : KeyVal()
}

@Serializable
data class Key(
	val label: String,
	val value: KeyVal = KeyVal.Str(label),
	val subLabel: String? = null,
	val size: Float = 1f,
	val options: String? = null,
)

@Serializable
data class EngineConfig(
	val type: String,
	val param: Map<String, String>,
)

@Serializable
data class Keyboard(val engine: EngineConfig, val layers: List<List<List<Key>>>)

private fun keySimpleRow(values: String): List<Key> {
	val ret = mutableListOf<Key>()
	for (value in values.chars()) {
		ret.add(Key(value.toChar().toString()))
	}
	return ret
}

val defaultKeyboard =
	Keyboard(engine = EngineConfig("direct", mapOf()), layers = listOf(
		listOf(
			keySimpleRow("1234567890"),
			mutableListOf<Key>().apply {
				add(Key("q", size = 1.5f))
				addAll(keySimpleRow("wfpbjlu"))
				add(Key("y", size = 1.5f))
			},
			keySimpleRow("arstgmneio"),
			mutableListOf<Key>().apply {
				add(
					Key(
						label = "^",
						value = KeyVal.OneShot(1),
						size = 1.5f
					)
				)
				addAll(keySimpleRow("zxcdvkh"))
				add(
					Key(
						label = "<=",
						value = KeyVal.Cmd(Command.Backspace),
						size = 1.5f
					)
				)
			},
			mutableListOf<Key>().apply {
				add(Key(label = "기호", value = KeyVal.TO(4), size = 1.5f))
				add(Key(label = "한영", value = KeyVal.TO(8), size = 1.5f))
				add(
					Key(
						label = "space",
						value = KeyVal.Str(" "),
						size = 1.5f
					)
				)
				add(Key("."))
				add(
					Key(
						label = "enter",
						value = KeyVal.Cmd(Command.Enter),
						size = 1.5f
					)
				)
			}
		),

		listOf(
			keySimpleRow("1234567890"),
			mutableListOf<Key>().apply {
				add(Key("Q", size = 1.5f))
				addAll(keySimpleRow("WFPBJLU"))
				add(Key("Y", size = 1.5f))
			},
			keySimpleRow("ARSTGMNEIO"),
			mutableListOf<Key>().apply {
				add(Key(label = "^^", value = KeyVal.TO(2), size = 1.5f))
				addAll(keySimpleRow("ZXCDVKH"))
				add(
					Key(
						label = "<=",
						value = KeyVal.Cmd(Command.Backspace),
						size = 1.5f
					)
				)
			},
			mutableListOf<Key>().apply {
				add(Key(label = "기호", value = KeyVal.TO(4), size = 1.5f))
				add(Key(label = "한영", value = KeyVal.TO(8), size = 1.5f))
				add(
					Key(
						label = "space",
						value = KeyVal.Str(" "),
						size = 1.5f
					)
				)
				add(Key("."))
				add(
					Key(
						label = "enter",
						value = KeyVal.Cmd(Command.Enter),
						size = 1.5f
					)
				)
			}
		)
	))
