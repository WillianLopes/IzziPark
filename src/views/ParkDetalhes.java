package views;

import models.Park;
import util.Mask;
import util.MaskTextView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import br.com.izzipark.R;
import controllers.ParkDataSource;

/**
 * ClienteDetails class, Activity that displays the whole information that an Cliente object contains.
 * @author Willian Lopes
 *
 */
public class ParkDetalhes extends Activity {
	
	Park park;
	ParkDataSource parkDS = null;
	int parkId;
	private TextView tvId, tvPlaca, tvEntrada, tvSaida, tv_tipo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.park_detail);
		
		Bundle bundle = getIntent().getExtras();	
		parkId = bundle.getInt("park_id");

		tvId = (TextView) findViewById(R.id.detail_id);
		tvPlaca = (TextView) findViewById(R.id.detail_placa);
		tvEntrada = (TextView) findViewById(R.id.detail_entrada);
		tvSaida = (TextView) findViewById(R.id.detail_saida);
		tv_tipo = (TextView) findViewById(R.id.detail_tipo);
			
		parkDS = new ParkDataSource(this);
		parkDS.open(false);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		park = parkDS.getpark(parkId);
		preencherDetalhesView(park);
	}
	
	/**
	 * Shows menu view specified in it's XML file view
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_park_details, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle menu item selection
		if(item.getItemId() == R.id.menu_update_park) {
			Intent intent = new Intent(this, ParkUpdate.class);
			intent.putExtra("park_id", Integer.parseInt(tvId.getText().toString()));			
			this.startActivity(intent);
			finish();
		}
		return false;
	}
	
	/**
	 * @param cli, Park object
	 * @param bundle, Bundle to get Park values from the selected Park
	 */
	private void preencherDetalhesView(Park park) {
		tvId.setText(Integer.toString(park.getId()));
		
		tvPlaca.setText(park.getPlaca());
		
		if(park.getHora_entrada() != null && !park.getHora_entrada().equals(""))
			tvEntrada.setText(park.getHora_entrada());
		else
			tvEntrada.setText("---");
		
		if(park.getHora_saida() != null && !park.getHora_saida().equals(""))
			tvSaida.setText(park.getHora_saida());
		else
			tvSaida.setText("---");
		
		if(park.getTipo() != null && !park.getTipo().equals("")) {
			tv_tipo.setText(park.getTipo());
		}
		else {
			tv_tipo.setText("Sem tipo");
		}

	}
	
	/**
	 * need to override onDestroy in order to close DB connection
	 */
	@Override
	protected void onDestroy() {
		parkDS.close(); // close DB connection, before calling super.onDestroy()
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent(this, ParkActivity.class);
		startActivity(i);
		finish();
	}
}
