package views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import models.Park;
import models.ParkValidator;

import org.apache.commons.lang3.text.WordUtils;

import util.Mask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import br.com.izzipark.R;
import controllers.ParkDataSource;


public class ParkUpdate extends Activity implements OnClickListener{

	TextView tvTitle;
	EditText etPlaca;
	TimePicker etEntrada, etSaida;
	Button btnSave, btnSpeech;
	TextWatcher maskPlaca;
	int parkId;
	AutoCompleteTextView type_list;
	Park park = null;
	ParkDataSource parkDS = null;
	ParkValidator validator = null;
	private final int REQ_CODE_SPEECH_INPUT = 100;
	
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
		setContentView(R.layout.activity_park_update);
		
		// capture park id from parkDetails to load its data
		Bundle bundle = getIntent().getExtras();
		parkId = bundle.getInt("park_id"); 
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, TIPOS);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.type_list);
        textView.setAdapter(adapter);
		
		parkDS = new ParkDataSource(this);
		parkDS.open(true); // open DB connection in writable mode, because of Delete option
		// pega objeto park pelo ID
		park = parkDS.getpark(parkId);
		
		preencherDadosPark(park);
		validator = new ParkValidator(this);
	}
	/**
	 * Fills Cliente data from database into the view in order to get modified.
	 * @param park
	 */
	private void preencherDadosPark(Park park) {
		tvTitle = (TextView) findViewById(R.id.update_title);
		tvTitle.setText(getResources().getString(R.string.update)+ " " + park.getPlaca());
		
		String currentDate = day + "-" + (month + 1) + "-" + year + " " + hour + ":" + minute + ":" + second;
		String[] hour = park.getHora_entrada().toString().split(":");
		String[] minutos = hour[0].split(" ");
		
		etPlaca = (EditText) findViewById(R.id.update_etPlaca);
		etPlaca.setTag(getResources().getString(R.string.placa));
		etPlaca.setText(park.getPlaca());
		
		etEntrada = (TimePicker) findViewById(R.id.update_etEntrada);
		etEntrada.setCurrentHour(Integer.parseInt(minutos[1]));
		etEntrada.setCurrentMinute(Integer.parseInt(hour[1]));
		etEntrada.setTag(getResources().getString(R.string.entrada));
		etEntrada.setTag(park.getHora_entrada());
		etEntrada.setEnabled(false);
		
		etSaida = (TimePicker) findViewById(R.id.update_etSaida);
		etSaida.setTag(getResources().getString(R.string.saida));
		etSaida.setTag(currentDate);
		etSaida.setEnabled(false);
        
		type_list = (AutoCompleteTextView) findViewById(R.id.type_list);
		type_list.setTag(getResources().getString(R.string.tipo));
		type_list.setTag(park.getTipo());
		
		maskPlaca = Mask.insert("###-####", etPlaca);
		etPlaca.addTextChangedListener(maskPlaca);

		btnSave = (Button) findViewById(R.id.update_btnSave);
		btnSave.setOnClickListener(this);
	}
	
	/**
	 * Makes sure every value of every EditText is valid and calls updateCliente
	 */
	public void onClick(View v) {
		if(v.equals(btnSave)) {
			// eval EditText values in this order
			if(validator.isValidText(etPlaca) && validator.isValidText(type_list)) {
				park.setPlaca(etPlaca.getText().toString().toUpperCase());
				park.setHora_entrada(etEntrada.getTag().toString());
				park.setHora_saida(etSaida.getTag().toString());		
				park.setTipo(type_list.getTag().toString());	
				atualizarPark(park);
			} 
		}
	}
 
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
	        case REQ_CODE_SPEECH_INPUT: {
	            if (resultCode == RESULT_OK && null != data) {
	                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	                
	                Bundle bundle = getIntent().getExtras();
	        		parkId = bundle.getInt("park_id"); 
	        		
	        		parkDS = new ParkDataSource(this);
	        		parkDS.open(true); //abre conexão com o DB
	        		// pega objeto Cliente pelo ID
	        		park = parkDS.getpark(parkId);
	            }
	            break;
	        }
        }
    }
    
    @Override
	public void onBackPressed() {
		Intent i = new Intent(this, ParkActivity.class);
		startActivity(i);
		finish();
	}
	
	/**
	 * Updates an Cliente register into the database 
	 * and prompts AlertDialog that notifies the update.
	 * @param cliente, the Cliente with updated attributes
	 */
	public void atualizarPark(Park cliente) {
		int updated = parkDS.updatepark(cliente);
		String alertMessage = "";
		if (updated != -1) {
			alertMessage = getResources().getString(R.string.upated_successfully);
		} else {
			alertMessage = getResources().getString(R.string.updated_fail);
		}
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setTitle(R.string.update_park)
		.setMessage(alertMessage)
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent(ParkUpdate.this, ParkActivity.class);
				startActivity(i);
				finish();
			}
		})
		.show();
	}
	
	/**
	 * need to override onDestroy in order to close DB connection
	 */
	@Override
	protected void onDestroy() {
		parkDS.close(); // close DB connection, before calling super.onDestroy()
		super.onDestroy();
	}
}
