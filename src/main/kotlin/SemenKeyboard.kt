package kr.xnu.keyboard.semen

import android.content.SharedPreferences
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.preference.PreferenceManager
import kotlinx.serialization.json.Json

class SemenKeyboard : InputMethodService() {
	private var imm: InputMethodManager? = null
	private lateinit var pref: SharedPreferences
	private var keyboard = defaultKeyboard
	private var changed = false
	private val changeListener =
		SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
			changed = true
		}

	override fun onCreate() {
		super.onCreate()
		imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
		pref = PreferenceManager.getDefaultSharedPreferences(this)
		pref.registerOnSharedPreferenceChangeListener(changeListener)
	}

	override fun onDestroy() {
		pref.unregisterOnSharedPreferenceChangeListener(changeListener)
		super.onDestroy()
	}

	private fun getLayoutConfig(): Keyboard {
		val text = ConfigKey.LAYOUT.fetch(pref)
		if (text.isBlank()) {
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

				Command.Enter -> {
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

	override fun onCreateInputView(): View {
		keyboard = getLayoutConfig()
		return keyboardView(this, keyboard) { handleEvent(it) }
	}

	override fun onShowInputRequested(
		flags: Int,
		configChange: Boolean,
	): Boolean {
		if (configChange || changed) {
			setInputView(keyboardView(this, keyboard) { handleEvent(it) })
			changed = false
		}
		return super.onShowInputRequested(flags, configChange)
	}
}
