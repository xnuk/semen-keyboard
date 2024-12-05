package kr.xnu.keyboard.semen

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.GridLayout
import com.lanlinju.bencode.Bencode


data class Key(val label: Char, val value: String, val layoutOptions: String)
data class Keyboard(val engine: String, val layers: List<List<List<Key>>>)

fun keySimple(value: Char): Key =
	Key(label = value, value = value.toString(), layoutOptions = "")

fun keySimpleRow(value: CharSequence): List<Key> {
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

val defaultKeyboardEncoded = Bencode.encodeToString(defaultKeyboard)

class SemenKeyboard : InputMethodService() {
	private var imm: InputMethodManager? = null
	override fun onCreate() {
		super.onCreate()
		imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
	}


	override fun onCreateInputView(): View {
		val keyLabel = R.id.key_label
		val grid = GridLayout(this).apply {
			layoutParams = GridLayout.LayoutParams().apply {
				width = MATCH_PARENT
				height = WRAP_CONTENT

			}
			columnCount = 2
			rowCount = 4
			useDefaultMargins = false

			setBackgroundColor(getColor(R.color.white))
		}

		val onClick = View.OnClickListener {
			val msg = it.getTag(keyLabel) as? String
			if (msg != null) {
				currentInputConnection.commitText(msg, 1)
			}
		}

		val weight1 = GridLayout.LayoutParams(
			GridLayout.spec(GridLayout.UNDEFINED, 1f),
			GridLayout.spec(GridLayout.UNDEFINED, 1f)
		)

		grid.addView(
			Button(this).apply {
				text = defaultKeyboardEncoded
				setTag(keyLabel, "Dick")
				setOnClickListener(onClick)
			},
			GridLayout.LayoutParams(weight1)
		)

		grid.addView(
			Button(this).apply {
				text = "Perfect"
				setTag(keyLabel, "Perfect")
				setOnClickListener(onClick)
			},
			GridLayout.LayoutParams(weight1)
		)

		return grid
	}


}
