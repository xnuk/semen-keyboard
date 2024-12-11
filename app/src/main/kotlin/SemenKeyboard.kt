package kr.xnu.keyboard.semen

import android.content.Context
import android.content.SharedPreferences
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.preference.PreferenceManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val defaultKeyboardEncoded = Json.encodeToString(defaultKeyboard)
const val LAYOUT_KEY = "layout-config"
val keyValueTag = R.id.key_value

fun keyboardView(
	context: Context,
	keyboard: Keyboard,
	onClick: (msg: String) -> Unit,
): View {

	val colorText = context.getColor(R.color.black)
	val colorBg = context.getColor(R.color.white)

	val grid = LinearLayout(context).apply {
		layoutParams = LinearLayout.LayoutParams(
			LayoutParams.MATCH_PARENT,
			LayoutParams.MATCH_PARENT
		)
		orientation = LinearLayout.VERTICAL

		setBackgroundColor(colorBg)
	}

	val onClickListener = View.OnClickListener {
		val msg = it.getTag(keyValueTag) as? String
		if (msg != null) {
			onClick(msg)
		}
	}

	for (keyRow in keyboard.layers[0]) {
		val row = LinearLayout(context).apply {
			layoutParams = LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT
			)
			orientation = LinearLayout.HORIZONTAL
		}

		for (key in keyRow) {
			row.addView(
				// TODO: reset button theme and apply custom
				Button(context).apply {
					text = key.label
					setTag(keyValueTag, key.value)
					setOnClickListener(onClickListener)
					setPadding(0, 0, 0, 0)
					setBackgroundColor(colorBg)
					setTextColor(colorText)
				},
				LinearLayout.LayoutParams(
					0, LayoutParams.MATCH_PARENT, key.size
				),
			)
		}

		grid.addView(row)
	}




	return grid
}

class SemenKeyboard : InputMethodService() {
	private var imm: InputMethodManager? = null
	private lateinit var pref: SharedPreferences
	private var keyboard: Keyboard = defaultKeyboard

	private fun getLayoutConfig(): Keyboard {
		val text = pref.getString(LAYOUT_KEY, "")
		if (text.isNullOrBlank()) {
			return defaultKeyboard
		}

		try {
			return Json.decodeFromString(text.trim())
		} catch (_: Throwable) {
			Toast.makeText(
				this,
				"Invalid layout config. Using default.",
				Toast.LENGTH_SHORT
			).show()
			return defaultKeyboard
		}
	}

	override fun onCreate() {
		super.onCreate()
		imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
		pref = PreferenceManager.getDefaultSharedPreferences(this)
	}

	// TODO: put keyboard in activity - to debug
	// TODO: maybe we should not use grid?
	override fun onCreateInputView(): View {
		keyboard = getLayoutConfig()
		return keyboardView(this, keyboard) {
			currentInputConnection.commitText(it, 1)
		}
	}
}
