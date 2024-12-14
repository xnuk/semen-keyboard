package kr.xnu.keyboard.semen

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable

@Serializable
enum class Command {
	Backspace,
	Enter
}

@Serializable
enum class EngineDirectModifier {
	Backspace, Enter, Space, Symbol, Switch, Shift
}

enum class ShiftModifier {
	None, OneShot, Keep
}

@Serializable
data class Decorative(
	val size: Float = 1f,
	@DrawableRes val icon: Int? = null,
	val description: String? = null,
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
						layer = (layer + 1) % 3
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
	style = Decorative(
		size = 1.5f,
		icon = R.drawable.backspace,
		description = "Backspace"
	)
)

private val shift = EngineDirectKey.Modifier(
	EngineDirectModifier.Shift,
	style = Decorative(
		size = 1.5f,
		icon = R.drawable.shift,
		description = "Shift"
	)
)

private val shiftOneshot = EngineDirectKey.Modifier(
	EngineDirectModifier.Shift,
	style = Decorative(
		size = 1.5f,
		icon = R.drawable.shift_filled,
		description = "Shifted"
	)
)

private val shiftLocked = EngineDirectKey.Modifier(
	EngineDirectModifier.Shift,
	style = Decorative(
		size = 1.5f,
		icon = R.drawable.shift_lock,
		description = "Shift locked"
	)
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
	style = Decorative(
		size = 2f,
		icon = R.drawable.space_bar,
		description = "Space"
	)
)
private val enter = EngineDirectKey.Modifier(
	EngineDirectModifier.Enter,
	Decorative(
		size = 1.5f,
		icon = R.drawable.keyboard_return,
		description = "Enter"
	)
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

			)))
