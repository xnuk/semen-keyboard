package kr.xnu.keyboard.semen

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.preference.PreferenceManager
import kotlin.math.roundToInt

val keyValueTag = R.id.key_value

fun keyboardView(
	context: Context,
	keyboard: List<List<EngineDirectKey>>,
	onClick: (msg: EngineDirectKey) -> Unit,
): View {
	val colorText = context.getColor(R.color.white)
	val colorBg = context.getColor(R.color.black)
	val setting = PreferenceManager.getDefaultSharedPreferences(context)

	val dp = context.resources.displayMetrics.density
	val keyHeight = ConfigKey.KEY_HEIGHT_DP.fetchFloat(setting)
	ConfigKey.FONT_SIZE_SP.fetchFloat(setting)

	val grid = LinearLayout(context).apply {
		layoutParams = LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT
		)
		orientation = LinearLayout.VERTICAL

		setBackgroundColor(colorBg)
	}

	val onClickListener = View.OnClickListener {
		val msg = it.getTag(keyValueTag) as? EngineDirectKey
		if (msg != null) {
			onClick(msg)
		}
	}

	for (keyRow in keyboard) {
		val row = LinearLayout(context).apply {
			layoutParams = LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				(keyHeight * dp).roundToInt()
			)
			orientation = LinearLayout.HORIZONTAL
		}

		for (key in keyRow) {
			val k = key.view(context, colorText, colorBg).also {
				it.setOnClickListener(onClickListener)
			}
			row.addView(
				k, LinearLayout.LayoutParams(
					0,
					ViewGroup.LayoutParams.MATCH_PARENT,
					key.style().size
				)
			)
		}

		grid.addView(row)
	}

	return grid
}
