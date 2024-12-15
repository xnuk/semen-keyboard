package kr.xnu.keyboard.semen

import kotlinx.serialization.Serializable

@Serializable
enum class Command {
	Backspace,
	Enter
}

@Serializable
enum class EngineDirectModifier {
	Backspace, Enter, Space, Symbol, Switch, Shift, ShiftLock, Unshift
}

fun EngineDirectModifier.icon(): Pair<Int, String> =
	when (this) {
		EngineDirectModifier.Backspace ->
			Pair(R.drawable.backspace, "Backspace")

		EngineDirectModifier.Enter -> Pair(R.drawable.keyboard_return, "Enter")
		EngineDirectModifier.Space -> Pair(R.drawable.space_bar, "Space")

		// TODO: is this correct?
		EngineDirectModifier.Shift -> Pair(R.drawable.shift, "Shift")
		EngineDirectModifier.ShiftLock ->
			Pair(R.drawable.shift_filled, "Shift lock")

		EngineDirectModifier.Unshift -> Pair(R.drawable.shift_lock, "Unshift")

		// TODO
		EngineDirectModifier.Switch -> Pair(R.drawable.xnuk_rainbow, "Switch")
		EngineDirectModifier.Symbol -> Pair(R.drawable.xnuk_rainbow, "Symbol")

	}

enum class ShiftModifier {
	None, OneShot, Keep
}

@Serializable
data class Decorative(
	val size: Float = 1f,
)

@Serializable
sealed class EngineDirectKey() {
	@Serializable
	class Str(
		val short: String,
		val long: String? = null,
		val style: Decorative = Decorative(),
	) : EngineDirectKey()

	@Serializable
	class Modifier(
		val modifier: EngineDirectModifier,
		val style: Decorative = Decorative(),
	) :
		EngineDirectKey()
}

fun EngineDirectKey.label(): Pair<String?, String?> = when (this) {
	is EngineDirectKey.Str -> Pair(short, long)
	is EngineDirectKey.Modifier -> Pair(null, null)
}

fun EngineDirectKey.style(): Decorative = when (this) {
	is EngineDirectKey.Str -> style
	is EngineDirectKey.Modifier -> style
}

@Serializable
data class EngineDirectKeyboard(
	val layers: List<List<List<EngineDirectKey>>>,
)

interface IcCondom {
	fun commitText(str: String)
	fun preeditText(str: String)
	fun deleteSelectedText(): Boolean
	fun deleteOneBefore()
	fun onEnter(): Boolean
}

interface RunState<T> {
	fun onClick(key: T, long: Boolean, ic: IcCondom)
}

class EngineDirectState(
	private val keyboard: EngineDirectKeyboard,
	private val onChangeLayer: (layer: Int) -> Int,
) : RunState<EngineDirectKey> {
	var layer = 0

	override fun onClick(
		key: EngineDirectKey,
		long: Boolean,
		ic: IcCondom,
	) {
		when (key) {
			is EngineDirectKey.Str -> {
				val value = if (long && key.long != null) {
					key.long
				} else {
					key.short
				}
				ic.commitText(value)
				if (layer == 1) {
					layer = 0
					onChangeLayer(layer)
				}
			}

			is EngineDirectKey.Modifier -> {
				when (key.modifier) {
					EngineDirectModifier.Backspace -> {
						if (!ic.deleteSelectedText()) {
							ic.deleteOneBefore()
						}
					}

					EngineDirectModifier.Enter -> {
						if (!ic.onEnter()) {
							ic.commitText("\n")
						}
					}

					EngineDirectModifier.Space -> {
						ic.commitText(" ")
					}

					EngineDirectModifier.Shift -> {
						layer = 1
						onChangeLayer(layer)
					}

					EngineDirectModifier.ShiftLock -> {
						layer = 2
						onChangeLayer(layer)
					}

					EngineDirectModifier.Unshift -> {
						layer = 0
						onChangeLayer(layer)
					}

					EngineDirectModifier.Switch -> {
						layer = 3
						onChangeLayer(layer)
					}

					else -> {}
				}
			}
		}
	}
}

@Serializable
data class EngineConfig(
	val type: String,
	val param: Map<String, String>,
)

@Serializable
data class Keyboard(
	val engine: EngineConfig,
	val keyboard: EngineDirectKeyboard,
)

private fun keySimpleRow(values: String): List<EngineDirectKey> {
	val ret = mutableListOf<EngineDirectKey>()
	for (value in values.chars()) {
		ret.add(EngineDirectKey.Str(value.toChar().toString()))
	}
	return ret
}
//⇧⌫␣↵⬆

private val backspace = EngineDirectKey.Modifier(
	EngineDirectModifier.Backspace,
	style = Decorative(size = 1.5f)
)

private val backspaceShort = EngineDirectKey.Modifier(
	EngineDirectModifier.Backspace,
	style = Decorative(size = 1f)
)

private val shift = EngineDirectKey.Modifier(
	EngineDirectModifier.Shift,
	style = Decorative(size = 1.5f)
)

private val shiftOneshot = EngineDirectKey.Modifier(
	EngineDirectModifier.ShiftLock,
	style = Decorative(size = 1.5f)
)

private val shiftLocked = EngineDirectKey.Modifier(
	EngineDirectModifier.Unshift,
	style = Decorative(size = 1.5f)
)

private val symbol = EngineDirectKey.Modifier(
	EngineDirectModifier.Symbol,
	style = Decorative(size = 1.5f)
)

private val switch = EngineDirectKey.Modifier(
	EngineDirectModifier.Switch,
	style = Decorative(size = 1.5f)
)
private val space = EngineDirectKey.Modifier(
	EngineDirectModifier.Space,
	style = Decorative(size = 2f)
)
private val enter = EngineDirectKey.Modifier(
	EngineDirectModifier.Enter,
	Decorative(size = 1.5f)
)

val defaultKeyboard =
	Keyboard(engine = EngineConfig("direct", mapOf()),
		keyboard = EngineDirectKeyboard(listOf(
			listOf(
				keySimpleRow("1234567890"),
				keySimpleRow("qwfpbjluy;"),
				keySimpleRow("arstgmneio"),
				mutableListOf<EngineDirectKey>().apply {
					addAll(keySimpleRow("zxcdvkh"))
					add(shift)
					add(backspace)
				},
				mutableListOf<EngineDirectKey>().apply {
					add(symbol)
					add(switch)
					add(space)
					add(EngineDirectKey.Str(".", "?"))
					add(enter)
				}
			),
			listOf(
				keySimpleRow("1234567890"),
				keySimpleRow("QWFPBJLUY:"),
				keySimpleRow("ARSTGMNEIO"),
				mutableListOf<EngineDirectKey>().apply {
					addAll(keySimpleRow("ZXCDVKH"))
					add(shiftOneshot)
					add(backspace)
				},
				mutableListOf<EngineDirectKey>().apply {
					add(symbol)
					add(switch)
					add(space)
					add(EngineDirectKey.Str(".", "?"))
					add(enter)
				}
			),
			listOf(
				keySimpleRow("1234567890"),
				keySimpleRow("QWFPBJLUY:"),
				keySimpleRow("ARSTGMNEIO"),
				mutableListOf<EngineDirectKey>().apply {

					addAll(keySimpleRow("ZXCDVKH"))
					add(shiftLocked)
					add(backspace)
				},
				mutableListOf<EngineDirectKey>().apply {
					add(symbol)
					add(switch)
					add(space)
					add(EngineDirectKey.Str(".", "?"))
					add(enter)
				}
			),

			listOf(
				keySimpleRow("1234567890"),
				keySimpleRow("ㅂㅈㄷㄱㅅㅗㅐㅔ"),
				keySimpleRow("ㅁㄴㅇㄹㅎㅓㅏㅣ"),
				mutableListOf<EngineDirectKey>().apply {
					add(
						EngineDirectKey.Str(
							"ㅋ",
							style = Decorative(size = 2f)
						)
					)
					addAll(keySimpleRow("ㅌㅊㅍㅜㅡ"))
					add(backspace)
				},
				mutableListOf<EngineDirectKey>().apply {
					add(symbol)
					add(switch)
					add(space)
					add(EngineDirectKey.Str(".", "?"))
					add(enter)
				}
			),

			)))
