package kr.xnu.keyboard.semen

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.GridLayout
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val defaultKeyboardEncoded = Json.encodeToString(defaultKeyboard)

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
				width = LayoutParams.MATCH_PARENT
				height = LayoutParams.WRAP_CONTENT
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
			GridLayout.spec(GridLayout.UNDEFINED, 1f),
		)

		grid.addView(
			Button(this).apply {
				text = defaultKeyboardEncoded
				setTag(keyLabel, "Dick")
				setOnClickListener(onClick)
			},
			GridLayout.LayoutParams(weight1),
		)

		grid.addView(
			Button(this).apply {
				text = "Perfect"
				setTag(keyLabel, "Perfect")
				setOnClickListener(onClick)
			},
			GridLayout.LayoutParams(weight1),
		)

		return grid
	}
}
