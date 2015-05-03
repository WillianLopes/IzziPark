package views;

import java.util.Calendar;

import models.Park;
import models.ParkValidator;

import org.apache.commons.lang3.text.WordUtils;

import util.Mask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import br.com.izzipark.R;
import controllers.ParkDataSource;

/**
 * ClienteCreate class, Activity that allows user to add a new Park register into SQLite database
 * @author Willian Lopes
 *
 */
public class ParkCreate extends Activity implements OnClickListener {
	
	EditText etPlaca, etSaida; //etEntrada,
	Button btnAdd, btnSpeech;
	CheckBox checkbox;
	TimePicker etEntrada;
	AutoCompleteTextView type_list;
	ParkValidator validator = null;
	TextWatcher maskPlaca;
	private final int REQ_CODE_SPEECH_INPUT = 100;
	public static final String MyPREFERENCES = "MyPrefs" ;
	public static final String Placa = "namePlaca"; 
	SharedPreferences shared;
	Calendar now = Calendar.getInstance();
	int year = now.get(Calendar.YEAR);
	int month = now.get(Calendar.MONTH); // Note: zero based!
	int day = now.get(Calendar.DAY_OF_MONTH);
	int hour = now.get(Calendar.HOUR_OF_DAY);
	int minute = now.get(Calendar.MINUTE);
	int second = now.get(Calendar.SECOND);
	int millis = now.get(Calendar.MILLISECOND);
	
	private static final String[] TIPOS = new String[] {
        "Carro", "Moto", "Bicicleta", "Triciclo", "Caminhonete", "Patinete"
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_park_create);
		
		validator = new ParkValidator(this);
		
		String currentDate = day + "-" + (month + 1) + "-" + year + " " + hour + ":" + minute + ":" + second;
		
		etPlaca = (EditText) findViewById(R.id.add_etPlaca);
		etPlaca.setTag(getResources().getString(R.string.placa));
		etEntrada = (TimePicker) findViewById(R.id.add_etEntrada);
		etEntrada.setTag(currentDate);
		etSaida = (EditText) findViewById(R.id.add_etSaida);
		etSaida.setTag(getResources().getString(R.string.saida));
		etSaida.setEnabled(false);
		checkbox = (CheckBox) findViewById(R.id.checkbox);	
		type_list = (AutoCompleteTextView) findViewById(R.id.type_list);
		type_list.setTag(getResources().getString(R.string.tipo));
		
		shared = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, TIPOS);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.type_list);
        textView.setAdapter(adapter);

		maskPlaca = Mask.insert("###-####", etPlaca);
		etPlaca.addTextChangedListener(maskPlaca);
		
		btnAdd = (Button) findViewById(R.id.add_btnAdd);
		btnAdd.setOnClickListener(this);
	}
	
	public void onCheckboxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    // Check which checkbox was clicked
	    switch(view.getId()) {
	        case R.id.checkbox:
	            if (checked) {
	            	//Do nothing
	            }
	            else {
	                // Remove the meat
	            }
	            break;
	    }
	}
	
	/**
	 * Implementing OnClickListener interface
	 */
	public void onClick(View v) {
		if(v.equals(btnAdd)) {
			// eval EditText values in this order
			if(validator.isValidText(etPlaca) && validator.isValidText(type_list)) {
				Park park = new Park();
				park.setPlaca(etPlaca.getText().toString().toUpperCase());
				park.setHora_entrada(etEntrada.getTag().toString());
				park.setHora_saida(etSaida.getText().toString());
				
				boolean isChecked = ((CheckBox) findViewById(R.id.checkbox)).isChecked();
				
				if(isChecked) {            	
	                Editor editor = shared.edit();
	                editor.putString(Placa, etPlaca.getText().toString());
	                editor.commit(); 
				}
				else {
					SharedPreferences preferences = getSharedPreferences("MyPrefs", 0);
					preferences.edit().remove("Placa").commit();
				}
                
                park.setTipo(type_list.getText().toString());
				addpark(park);	
			} 
		}
	}
	
	/**
	 * Method that instantiates a new ParkDataSource object 
	 * in order to insert the new Estacionamento.
	 * @param park
	 */
	public void addpark(Park park) { 
		ParkDataSource parkDS = new ParkDataSource(this);
		parkDS.open(true); //Abre conexão com o DB
		if(parkDS.addpark(park) != -1){
			new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(R.string.added_park)
				.setMessage(park.getPlaca() + " " + getResources().getString(R.string.added_park_msg))
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(ParkCreate.this, ParkActivity.class);
						startActivity(i);
						finish();
					}
				})
				.show();
		}
		parkDS.close(); // open DB connection
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent(this, ParkActivity.class);
		startActivity(i);
		finish();
	}
}
