package kr.xnu.keyboard.semen

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

		EngineDirectModifier.Shift -> Pair(R.drawable.shift, "Shift is off")
		EngineDirectModifier.ShiftLock ->
			Pair(R.drawable.shift_filled, "Shift is on")

		EngineDirectModifier.Unshift -> Pair(
			R.drawable.shift_lock,
			"Shift is locked"
		)

		EngineDirectModifier.Switch -> Pair(
			R.drawable.language,
			"Switch language"
		)

		EngineDirectModifier.Symbol -> Pair(
			R.drawable.emoji,
			"Symbol and Emoji"
		)

	}

@Serializable
data class Decorative(
	val size: Float = 1f,
)

@Serializable
sealed class EngineDirectKey() {
	@Serializable
	@SerialName("str")
	class Str(
		val short: String,
		val long: String? = null,
		val style: Decorative = Decorative(),
	) : EngineDirectKey()

	@Serializable
	@SerialName("modifier")
	class Modifier(
		val modifier: EngineDirectModifier,
		val style: Decorative = Decorative(),
	) :
		EngineDirectKey()
}

fun EngineDirectKey.view(
	context: Context,
	@ColorInt colorText: Int,
	@ColorInt colorBg: Int,
): View {

	// TODO: reset button theme and apply custom
	val view = when (this) {
		is EngineDirectKey.Modifier -> {
			val (icon, desc) = modifier.icon()
			KeyView(context, icon, desc)
		}

		is EngineDirectKey.Str -> {
			KeyView(context, short, long)
		}
	}


	return view.also {
		it.setTag(keyValueTag, this)
		it.layoutParams = LinearLayout.LayoutParams(
			0,
			ViewGroup.LayoutParams.MATCH_PARENT,
			style().size
		)
	}
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
	fun onRenderListener()
}

interface RunState<Key> {
	fun onClick(key: Key, long: Boolean)
	fun render(context: Context): View
	val ic: IcCondom
}

class EngineDirectState(
	private val keyboard: EngineDirectKeyboard,
	override val ic: IcCondom,

	) : RunState<EngineDirectKey> {
	var layer: Int = 0

	override fun render(context: Context): View =
		keyboardView(context, keyboard.layers[layer]) { onClick(it, false) }

	override fun onClick(
		key: EngineDirectKey,
		long: Boolean,
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
					ic.onRenderListener()
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
						ic.onRenderListener()
					}

					EngineDirectModifier.ShiftLock -> {
						layer = 2
						ic.onRenderListener()
					}

					EngineDirectModifier.Unshift -> {
						layer = 0
						ic.onRenderListener()
					}

					EngineDirectModifier.Switch -> {
						layer = if (layer > 0) {
							0
						} else {
							3
						}
						ic.onRenderListener()
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
