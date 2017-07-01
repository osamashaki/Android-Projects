package com.example.waleed.unitenapp123456.helpermethods;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.EditText;

import com.example.waleed.unitenapp123456.R;

public class EditTextValidator extends Activity {

	private Context context;
	
	private EditText edt;
	private Drawable error_indicator;
	private String action ;
	public boolean empty = false ;
	
	public EditTextValidator (Context context, EditText edt, String action){
		this.context = context;
		this.edt = edt;
		this.action = action;
	}

	public void EditTextForCheck() {
		error_indicator = context.getResources().getDrawable(R.drawable.edittext_alert);

		int left = 0;
		int top = 0;

		int right = error_indicator.getIntrinsicHeight();
		int bottom = error_indicator.getIntrinsicWidth();

		error_indicator.setBounds(new Rect(left, top, right, bottom));

		if (TextUtils.isEmpty(edt.getText().toString().trim()) || edt.length() == 0){
			ShowError(edt);
		}else{
			empty = false;
		}
		
		if(action.equals("email")){
			if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edt.getText().toString()).matches()) {
				edt.setError("Invalid Email Format");
			}
		}
		
//		if(action.equals("email")){
//			// Called when user type in EditText
//			edt.addTextChangedListener(new InputValidator(edt));
//		}
		
		// Called when an action is performed on the EditText
//		edt.setOnEditorActionListener(new EmptyTextListener(edt));
	}
	
	public void ShowError(EditText edt){
		edt.setError(null);		
		edt.setError("Oops! empty.", error_indicator);
		empty = true;
	}

	public void HideError(EditText edt){
		edt.setError(null);		
		empty = false;
	}
	
//	private class InputValidator implements TextWatcher {
//		private EditText et;
//
//		private InputValidator(EditText editText) {
//			this.et = editText;
//		}
//
//		@Override
//		public void afterTextChanged(Editable s) {
//				if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
//					et.setError("Invalid Email Format");
//				}
//		}
//
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
//
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before, int count) { }

//	}

//	private class EmptyTextListener implements OnEditorActionListener {
//		private EditText et;
//
//		public EmptyTextListener(EditText editText) {
//			this.et = editText;
//		}
//
//		@Override
//		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				// Called when user press Next button on the soft keyboard
//				if (et.getText().toString().equals("") || et.getText().toString().equals(" ")){
//					et.setError("Oops! empty.", error_indicator);
//					empty = true;
//				}
//			return false;
//		}
//	}
	
}