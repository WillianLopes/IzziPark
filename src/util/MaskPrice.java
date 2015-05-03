package util;

 import java.text.NumberFormat;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

 
public abstract class MaskPrice {
    public static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "").replaceAll("[:]", "");
    }
 
    public static TextWatcher insert(final EditText ediTxt) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";
            private String current = "";
 
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)) {
                	ediTxt.removeTextChangedListener(this);

                   String cleanString = s.toString().replaceAll("[R$,.]", "");

                   double parsed = Double.parseDouble(cleanString);
                   String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                   current = formatted;
                   ediTxt.setText(formatted);
                   ediTxt.setSelection(formatted.length());

                   ediTxt.addTextChangedListener(this);
                }
            }
 
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
 
            public void afterTextChanged(Editable s) {
            }
        };
    }
}