package kr.xnu.keyboard.semen

import android.content.SharedPreferences
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.preference.PreferenceManager
import kotlinx.serialization.json.Json

const val LAYOUT_KEY = "layout-config"

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

	fun handleEvent(item: KeyVal) {
		val ic = currentInputConnection
		when (item) {
			is KeyVal.Str -> ic.commitText(item.str, 1)
			is KeyVal.Cmd -> when (item.cmd) {
				Command.Backspace -> {
					val selected = ic.getSelectedText(0)
					if (selected == null || selected.isEmpty()) {
						ic.deleteSurroundingText(1, 0)
					} else {
						ic.commitText("", 1)
					}
				}

				Command.Enter -> {/*
					currentInputConnection.sendKeyEvent(
						KeyEvent(
							KeyEvent.ACTION_DOWN,
							KeyEvent.KEYCODE_ENTER
						)
					)
					currentInputConnection.sendKeyEvent(
						KeyEvent(
							KeyEvent.ACTION_UP,
							KeyEvent.KEYCODE_ENTER
						)
					)*/
					if (!sendDefaultEditorAction(true)) {
						ic.commitText("\n", 1)
					}
				}
			}

			is KeyVal.TO -> {
				setInputView(
					keyboardView(this, keyboard, item.layer) {
						handleEvent(it)
					}
				)
			}

			else -> {}
		}
	}

	// TODO: put keyboard in activity - to debug
	// TODO: maybe we should not use grid?
	override fun onCreateInputView(): View {
		keyboard = getLayoutConfig()
		return keyboardView(this, keyboard) { handleEvent(it) }
	}
}
