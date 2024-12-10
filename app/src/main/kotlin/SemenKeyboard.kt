package kr.xnu.keyboard.semen

import android.content.Context
import android.content.SharedPreferences
import android.inputmethodservice.InputMethodService
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.GridLayout
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

	val firstLayer = keyboard.layers[0]
	val firstLayerMaxCol = firstLayer.maxOf { it.size }
	val colorText = context.getColor(R.color.white)
	val colorBg = context.getColor(R.color.black)

	val grid = GridLayout(context).apply {
		layoutParams = GridLayout.LayoutParams().apply {
			width = LayoutParams.MATCH_PARENT
			height = LayoutParams.MATCH_PARENT
		}
		rowCount = firstLayer.size
		columnCount = firstLayerMaxCol
		useDefaultMargins = false

		setBackgroundColor(colorBg)
	}

	Log.d("dick", "${firstLayer.size} $firstLayerMaxCol")

	val onClickListener = View.OnClickListener {
		val msg = it.getTag(keyValueTag) as? String
		if (msg != null) {
			onClick(msg)
		}
	}

	var r = 0
	for (row in keyboard.layers[0]) {
		var c = 0
		for (v in row) {
			grid.addView(
				Button(context).apply {
					text = v.label
					setTag(keyValueTag, v.value)
					setOnClickListener(onClickListener)
					if ((r + c) % 2 == 0) {
						setBackgroundColor(colorBg)
						setTextColor(colorText)
					} else {
						setBackgroundColor(colorText)
						setTextColor(colorBg)
					}

				},
				GridLayout.LayoutParams(
					GridLayout.spec(r, 1f),
					GridLayout.spec(c, 1f),
				).apply {
					width = 0
				},
			)
			c += 1
		}
		r += 1
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
