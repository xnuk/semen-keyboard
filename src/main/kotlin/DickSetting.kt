package kr.xnu.keyboard.semen

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS
import android.text.InputType
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val defaultKeyboardEncoded = Json.encodeToString(defaultKeyboard)

object ConfigKey {
	val LAYOUT = PrefKey("layout-config", defaultKeyboardEncoded)
	val KEY_HEIGHT_DP = PrefKey("key-height", "60")
	val FONT_SIZE_SP = PrefKey("font-size", "30")
}

class DickSettingFragment : PreferenceFragmentCompat() {
	override fun onCreatePreferences(
		savedInstanceState: Bundle?,
		rootKey: String?,
	) {
		renderPref {
			preference("Open keyboard setting") {
				setOnPreferenceClickListener {
					startActivity(Intent(ACTION_INPUT_METHOD_SETTINGS))
					true
				}
			}

			preference("Open keyboard switcher") {
				setOnPreferenceClickListener {
					val imm =
						context.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
					imm?.showInputMethodPicker()
					true
				}
			}

			category("For Testing") {
				preference("Open the keyboard") {
					setOnPreferenceClickListener {
						startActivity(
							Intent(
								context,
								KeyboardTester::class.java
							)
						)
						true
					}
				}
			}

			category("Import/Export") {
				input("Edit layout config", ConfigKey.LAYOUT) {
					summary =
						"Copy or paste layout config with JSON-formatted text"
					setOnBindEditTextListener { view ->
						if (view.text.toString().trim() == "") {
							view.setText(defaultKeyboardEncoded)
						}
						view.selectAll()
					}
				}
			}

			category("Some Setting") {
				input("Change key height (dp)", ConfigKey.KEY_HEIGHT_DP) {
					setOnBindEditTextListener { view ->
						view.inputType = InputType.TYPE_CLASS_NUMBER
						view.selectAll()
					}
				}

				input("Change font size (sp)", ConfigKey.FONT_SIZE_SP) {
					setOnBindEditTextListener { view ->
						view.inputType = InputType.TYPE_CLASS_NUMBER
						view.selectAll()
					}
				}
			}
		}
	}
}

class DickSetting : FragmentActivity() {
	// AppCompatActivity: 이쁘게
	// FragmentActivity: 되긴 되는데 덜 이쁘게

	private val prefListener: OnSharedPreferenceChangeListener =
		OnSharedPreferenceChangeListener { pref, key ->
			onPrefChange(pref, key)
		}

	@SuppressLint("ResourceType")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val frame = FrameLayout(this).apply { id = R.id.frame }
		setContentView(frame)
		supportFragmentManager
			.beginTransaction()
			.replace(frame.id, DickSettingFragment())
			.commit()

		val pref = PreferenceManager.getDefaultSharedPreferences(this)
		pref.registerOnSharedPreferenceChangeListener(prefListener)
	}

	override fun onDestroy() {
		PreferenceManager.getDefaultSharedPreferences(this)
			.unregisterOnSharedPreferenceChangeListener(prefListener)
		super.onDestroy()
	}

	private fun onPrefChange(pref: SharedPreferences, key: String?) {
		if (key == ConfigKey.LAYOUT.key) {
			val config = ConfigKey.LAYOUT.fetch(pref).trim()
			if (config.isEmpty()) {
				return
			}

			try {
				Json.decodeFromString<Keyboard>(config)
				return
			} catch (e: Throwable) {
//			pref.edit().remove(key).apply() // prefer to not delete?
				Toast.makeText(
					this,
					"The config doesn't fit. Make sure you pasted the whole config.",
					Toast.LENGTH_SHORT
				).show()
			}

			// pref.edit().putString(key, defaultKeyboardEncoded).apply()
		}
	}
}
