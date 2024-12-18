package kr.xnu.keyboard.semen

import android.content.SharedPreferences
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.preference.PreferenceManager
import kotlinx.serialization.json.Json

class SemenKeyboard : InputMethodService(), IcCondom {
	private var imm: InputMethodManager? = null
	private lateinit var pref: SharedPreferences
	private var keyboard = defaultKeyboard
	private var changed = false
	private var state: EngineDirectState? = null
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

	override fun onCreateInputView(): View {
		keyboard = getLayoutConfig()
		val state = EngineDirectState(keyboard.keyboard, this)
		this.state = state
		return state.render(this)
	}

	override fun onShowInputRequested(
		flags: Int,
		configChange: Boolean,
	): Boolean {
		if (configChange || changed) {
			setInputView(onCreateInputView())
			changed = false
		}
		return super.onShowInputRequested(flags, configChange)
	}

	override fun commitText(str: String) {
		currentInputConnection.commitText(str, 1)
	}

	override fun preeditText(str: String) {
		currentInputConnection.setComposingText(str, 1)
	}

	override fun deleteSelectedText(): Boolean {
		val ic = currentInputConnection

		val selected = ic.getSelectedText(0)
		if (selected == null || selected.isEmpty()) {
			return false
		}

		ic.commitText("", 1)
		return true
	}

	override fun deleteOneBefore() {
		currentInputConnection.deleteSurroundingText(1, 0)
	}

	override fun onEnter(): Boolean =
		sendDefaultEditorAction(true)

	override fun onRenderListener() {
		state?.also { setInputView(it.render(this)) }
	}
}
