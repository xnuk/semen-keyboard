package kr.xnu.keyboard.semen

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes

private fun text(
	context: Context,
	text: String,
	gravity: Int = Gravity.CENTER,
) = TextView(context).also {
	it.text = text
	it.gravity = gravity // 한 번 찍으면 못 바꿈...
	it.textDirection = TextView.TEXT_DIRECTION_LTR
	it.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
	it.setTextColor(context.getColor(R.color.white))
	it.setPadding(0, 0, 0, 0)
}

private fun weight(weight: Float) = LinearLayout.LayoutParams(
	LinearLayout.LayoutParams.MATCH_PARENT,
	0
).also { it.weight = weight }

class KeyView : LinearLayout {
	private fun common() {
		setBackgroundColor(context.getColor(R.color.black))
		setPadding(0, 0, 0, 0)
		isClickable = true
		orientation = VERTICAL
	}

	private fun three(a: View, b: View, c: View) {
		addView(a, weight(3f))
		addView(b, weight(6f))
		addView(c, weight(1f))
	}

	constructor(
		context: Context,
		primary: String,
		sub: String?,
	) : super(context) {
		common()

		val subView = text(context, sub ?: "", gravity = Gravity.END)
		val mainView = text(context, primary, gravity = Gravity.CENTER)

		three(subView, mainView, View(context))
	}

	constructor(
		context: Context,
		@DrawableRes icon: Int,
		description: String,
	) : super(context) {
		common()

		val main =
			ImageView(context).apply {
				setImageDrawable(context.getDrawable(icon))
				gravity = Gravity.CENTER
				contentDescription = description
				// colorFilter = context.getColor(R.color.white)
				setPadding(0, 0, 0, 0)
			}

		three(View(context), main, View(context))
	}
}
