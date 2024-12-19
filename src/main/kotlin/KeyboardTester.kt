package kr.xnu.keyboard.semen

import android.os.Bundle
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.ComponentActivity

class KeyboardTester : ComponentActivity() {
	private var toastBefore: Toast? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val parent = RelativeLayout(this)

		val k = keyboardView(this, defaultKeyboard.keyboard.layers[0]) {
			toastBefore?.cancel()
			val t = Toast.makeText(this, "$it", Toast.LENGTH_SHORT)
			t.show()
			toastBefore = t

		}
		k.id = R.id.keyboard

		parent.addView(
			EditText(this).apply { hint = "Dick" },
			RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT
			).apply {
				addRule(RelativeLayout.ABOVE, R.id.keyboard)
			}
		)

		parent.addView(
			k,
			RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT
			).apply {
				addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
			}
		)

		setContentView(
			parent,
			RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT
			)

		)
	}
}
