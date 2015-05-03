package util;

 import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

 
public abstract class Mask {
    public static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "").replaceAll("[:]", "");
    }
 
    public static TextWatcher insert(final String mask, final EditText ediTxt) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";
 
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                String str = Mask.unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
            }
 
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }
 
            public void afterTextChanged(Editable s) {
            }
        };
        
    }
    
    public static String setMaskCpf(String s) {
    	
    	for(int i = 0; i < s.length(); i++) {
    		if(i == 3) {
    			s = s.substring(0,3) + "." + s.substring(3, s.length());
    		}
    		if(i == 6) {
    			s = s.substring(3,6) + "." + s.substring(6, s.length());
    		}
    		if(i == 9) {
    			s = s.substring(6,9) + "-" + s.substring(9, s.length());
    		}
    	}
    	
		return s;
    }
}