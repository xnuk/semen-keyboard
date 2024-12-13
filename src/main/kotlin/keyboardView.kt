package kr.xnu.keyboard.semen

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.preference.PreferenceManager
import kotlin.math.roundToInt

val keyValueTag = R.id.key_value

fun keyboardView(
	context: Context,
	keyboard: Keyboard,
	layer: Int = 0,
	onClick: (msg: KeyVal) -> Unit,
): View {
	val colorText = context.getColor(R.color.white)
	val colorBg = context.getColor(R.color.black)
	val setting = PreferenceManager.getDefaultSharedPreferences(context)

	val dp = context.resources.displayMetrics.density
	val keyHeight = ConfigKey.KEY_HEIGHT_DP.fetchFloat(setting)
	val fontSize = ConfigKey.FONT_SIZE_SP.fetchFloat(setting)

	val grid = LinearLayout(context).apply {
		layoutParams = LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT
		)
		orientation = LinearLayout.VERTICAL

		setBackgroundColor(colorBg)
	}

	val onClickListener = View.OnClickListener {
		val msg = it.getTag(keyValueTag) as? KeyVal
		if (msg != null) {
			onClick(msg)
		}
	}

	for (keyRow in keyboard.layers[layer]) {
		val row = LinearLayout(context).apply {
			layoutParams = LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				(keyHeight * dp).roundToInt()
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
					textSize = fontSize
				},
				LinearLayout.LayoutParams(
					0, ViewGroup.LayoutParams.MATCH_PARENT, key.size
				),
			)
		}

		grid.addView(row)
	}

	return grid
}
