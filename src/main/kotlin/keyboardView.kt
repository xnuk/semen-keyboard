package kr.xnu.keyboard.semen

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.preference.PreferenceManager
import kotlin.math.roundToInt

val keyValueTag = R.id.key_value

fun keyboardView(
	context: Context,
	keyboard: Keyboard,
	layer: Int = 0,
	onClick: (msg: EngineDirectKey) -> Unit,
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
		val msg = it.getTag(keyValueTag) as? EngineDirectKey
		if (msg != null) {
			onClick(msg)
		}
	}

	for (keyRow in keyboard.keyboard.layers[layer]) {
		val row = LinearLayout(context).apply {
			layoutParams = LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				(keyHeight * dp).roundToInt()
			)
			orientation = LinearLayout.HORIZONTAL
		}

		for (key in keyRow) {
			val style = key.style()

			// TODO: reset button theme and apply custom
			val view = if (style.icon != null && style.description != null) {
				ImageButton(context).apply {
					setImageDrawable(context.getDrawable(style.icon))
					contentDescription = style.description
				}
			} else {
				Button(context).apply {
					text = key.label().first
					setTextColor(colorText)
					textSize = fontSize
				}
			}

			view.apply {
				setTag(keyValueTag, key)
				setOnClickListener(onClickListener)
				setPadding(0, 0, 0, 0)
				setBackgroundColor(colorBg)
			}

			row.addView(
				view,
				LinearLayout.LayoutParams(
					0,
					ViewGroup.LayoutParams.MATCH_PARENT,
					key.style().size
				),
			)
		}

		grid.addView(row)
	}

	return grid
}
