package kr.xnu.keyboard.semen

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.ComponentActivity

class KeyboardTester : ComponentActivity(), IcCondom {
	private var toastBefore: Toast? = null
	private var state: EngineDirectState? = null

	private var keyboard: View? = null
	private val keyboardLayoutParams = RelativeLayout.LayoutParams(
		RelativeLayout.LayoutParams.MATCH_PARENT,
		RelativeLayout.LayoutParams.WRAP_CONTENT
	).apply {
		addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val parent = RelativeLayout(this)
		val state = EngineDirectState(defaultKeyboard.keyboard, this)
		this.state = state

		val k = state.render(this)
		k.id = R.id.keyboard
		keyboard = k

		parent.addView(
			EditText(this).apply { hint = "Dick" },
			RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT
			).apply {
				addRule(RelativeLayout.ABOVE, R.id.keyboard)
			}
		)

		parent.addView(k, keyboardLayoutParams)

		setContentView(
			parent,
			RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT
			)
		)
	}

	override fun commitText(str: String) {
		toastBefore?.cancel()
		toastBefore =
			Toast.makeText(this, "commit: $str", Toast.LENGTH_SHORT)
				.apply { show() }
	}

	override fun preeditText(str: String) {
		toastBefore?.cancel()
		toastBefore =
			Toast.makeText(this, "preedit: $str", Toast.LENGTH_SHORT)
				.apply { show() }
	}

	override fun deleteSelectedText(): Boolean {
		toastBefore?.cancel()
		toastBefore =
			Toast.makeText(this, "deleteSelectedText", Toast.LENGTH_SHORT)
				.apply { show() }
		return true
	}

	override fun deleteOneBefore() {
		toastBefore?.cancel()
		toastBefore =
			Toast.makeText(this, "deleteOneBefore", Toast.LENGTH_SHORT)
				.apply { show() }
	}

	override fun onEnter(): Boolean {
		toastBefore?.cancel()
		toastBefore =
			Toast.makeText(this, "Enter", Toast.LENGTH_SHORT).apply { show() }
		return true
	}

	override fun onRenderListener() {
		val k = keyboard
		val newK = state?.render(this)
		val parent = k?.parent as? ViewGroup
		if (k != null && parent != null && newK != null) {
			val index = parent.indexOfChild(k)
			parent.removeView(k)
			newK.id = R.id.keyboard
			parent.addView(newK, index, keyboardLayoutParams)
		}
	}
}
