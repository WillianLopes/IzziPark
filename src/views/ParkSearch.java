package views;

import java.util.ArrayList;
import java.util.List;

import models.Park;
import models.ParkAdapter;
import models.ParkValidator;
import util.Mask;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.com.izzipark.R;
import controllers.ParkDataSource;

/**
 * ClienteCreate class, Activity that allows user to add a new Cliente register into SQLite database
 * @author Willian Lopes
 *
 */
public class ParkSearch extends Activity implements OnClickListener {
	
	EditText etPlaca, etDescricao;
	Button btnSearch;
	ParkValidator validator = null;
	TextWatcher maskCpf, maskTelefone;
	ParkAdapter adapter = null;
	List<Park> park = null;
	ParkDataSource parkDS = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_park_search);

		//validator = new ClienteValidator(this);
		
		etPlaca = (EditText) findViewById(R.id.search_etPlaca);
		etPlaca.setTag(getResources().getString(R.string.placa));

		btnSearch = (Button) findViewById(R.id.add_btnSearch);
		btnSearch.setOnClickListener(this);
	}

	
	/**
	 * Implementing OnClickListener interface
	 */
	public void onClick(View v) {
		if(v.equals(btnSearch)) {

			Park park = new Park();
			park.setPlaca(etPlaca.getText().toString());
			
			ArrayList<Park> parks = getparks(park);
			
			Intent i = new Intent(this, ParkActivity.class);
			i.putExtra("isSearch", true);
			ParkActivity.parksFilter = parks;
			startActivity(i);
			finish();
		}
	}
	
	public ArrayList<Park> getparks(Park pk) {
		ParkDataSource parkDS = new ParkDataSource(this);
		parkDS.open(true); //Abre conex�o com o DB
		ArrayList<Park> pks = new ArrayList<Park>();
		pks = (ArrayList<Park>)parkDS.getParkTituloOrDescricao(pk.getPlaca());
		
		parkDS.close(); //Fecha conex�o como DB
		return pks;
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent(this, ParkActivity.class);
		startActivity(i);
		finish();
	}

}
