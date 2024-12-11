package kr.xnu.keyboard.semen

import android.os.Bundle
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.ComponentActivity

class KeyboardTester : ComponentActivity() {
	private var toastBefore: Toast? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val k = keyboardView(this, defaultKeyboard) {
			toastBefore?.cancel()
			val t = Toast.makeText(this, it, Toast.LENGTH_SHORT)
			t.show()
			toastBefore = t
		}

		setContentView(
			k,
			GridLayout.LayoutParams(
				ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT
				)
			)
		)
	}
}
