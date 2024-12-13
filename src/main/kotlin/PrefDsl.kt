package kr.xnu.keyboard.semen

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen

data class PrefParent(
	val context: Context,
	val addPref: (pref: Preference) -> Unit,
)

data class PrefKey(val key: String, val default: String)

fun PrefKey.fetch(pref: SharedPreferences): String =
	pref.getString(key, default) ?: default

fun PrefKey.fetchInt(pref: SharedPreferences): Int =
	pref.getString(key, default)?.toIntOrNull() ?: default.toInt()

fun PrefKey.fetchFloat(pref: SharedPreferences): Float =
	pref.getString(key, default)?.toFloatOrNull() ?: default.toFloat()

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

inline fun PrefParent.input(
	title: String,
	key: PrefKey,
	builder: EditTextPreference.() -> Unit,
): EditTextPreference {
	val pref = EditTextPreference(context)
	pref.title = title
	pref.key = key.key
	pref.setDefaultValue(key.default) // Object(Any)라곤 하지만? 실제론? Any가 아니다
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
