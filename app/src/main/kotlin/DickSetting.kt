package kr.xnu.keyboard.semen

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class DickSettingFragment : PreferenceFragmentCompat() {
	override fun onCreatePreferences(
		savedInstanceState: Bundle?,
		rootKey: String?
	) {
		setPreferencesFromResource(R.xml.preferences, rootKey)

		findPreference<Preference>("open-keyboard-setting")
			?.setOnPreferenceClickListener {
				startActivity(Intent(ACTION_INPUT_METHOD_SETTINGS))
				true
			}

		findPreference<Preference>("open-keyboard-switcher")
			?.setOnPreferenceClickListener {
				val imm =
					context?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
				imm?.showInputMethodPicker()
				true
			}
	}
}

class DickSetting : FragmentActivity() {
	// AppCompatActivity: 이쁘게
	// FragmentActivity: 되긴 되는데 덜 이쁘게
	@SuppressLint("ResourceType")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val frame = FrameLayout(this).apply {
			id = R.id.frame
		}
		setContentView(frame)
		supportFragmentManager.beginTransaction().replace(
			frame.id,
			DickSettingFragment()
		).commit()
	}
}
