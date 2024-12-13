package kr.xnu.keyboard.semen

import android.content.Context
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen

class PrefParent(
	val context: Context,
	val addPref: (pref: Preference) -> Unit,
)

inline fun PrefParent.preference(
	title: String,
	builder: Preference.() -> Unit,
): Preference {
	val pref = Preference(context)
	pref.title = title
	builder(pref)
	addPref(pref)
	return pref
}

inline fun PrefParent.editText(
	title: String,
	builder: EditTextPreference.() -> Unit,
): EditTextPreference {
	val pref = EditTextPreference(context)
	pref.title = title
	pref.dialogTitle = title
	builder(pref)
	addPref(pref)
	return pref
}

inline fun PrefParent.category(
	title: String,
	builder: PrefParent.(PreferenceCategory) -> Unit,
) {
	val category = PreferenceCategory(context)
	addPref(category)
	category.title = title
	val np = PrefParent(context) { category.addPreference(it) }
	builder(np, category)
}

inline fun PreferenceFragmentCompat.renderPref(builder: PrefParent.() -> Unit): PreferenceScreen {
	val context = requireContext()
	val screen = preferenceManager.createPreferenceScreen(context)
	val parent = PrefParent(context) { screen.addPreference(it) }

	preferenceScreen = screen
	builder(parent)
	return screen
}
