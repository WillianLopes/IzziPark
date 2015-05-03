package views;

import java.util.ArrayList;
import java.util.List;

import models.Park;
import models.ParkAdapter;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import br.com.izzipark.R;
import controllers.ParkDataSource;

public class ParkActivity extends ListActivity {

	ParkAdapter adapter = null;
	List<Park> parks = null;
	ParkDataSource parkDS = null;
	public static ArrayList<Park> parksFilter;
	boolean isSearch;
	/**
	 * onCreate method
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		parkDS = new ParkDataSource(this);
		parkDS.open(true); // open DB connection in writable mode, because of Delete option
		
		Intent i = getIntent();
        Bundle b = i.getExtras();

        if(b != null)
        {
            isSearch = (boolean) b.getBoolean("isSearch");
            if(isSearch == true) {
            	if(parksFilter != null) {
            		parks = parksFilter;
            		if(parksFilter.isEmpty()) {
            			Toast.makeText(this, "Nenhuma placa foi encontrada.", Toast.LENGTH_SHORT).show();
            		}
            	}
            }
        }
		
		carregarParks(parks);
	}
	
	/**
	 * Retrieve data on onResume Activity Cicle Life state in order to refresh data
	 */
	protected void carregarParks(List<Park> parks) {
		super.onResume();

		if(parks == null){
			parks = parkDS.getparks(); // retrieve data from DB
		}
		
		adapter = new ParkAdapter(this, parks, parkDS);
		adapter.notifyDataSetChanged();
		this.setListAdapter(adapter);
		this.registerForContextMenu(this.getListView());
	}
		
	/**
	 * Creating and inflating context menu
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    
	    this.getMenuInflater().inflate(R.menu.activity_park_context_menu, menu);
	    
	    if (v.getId() == this.getListView().getId()) {
	        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
	        Park park = (Park) getListView().getItemAtPosition(info.position);
	        menu.setHeaderTitle(park.getPlaca());
	    }
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final Park park = (Park) getListView().getItemAtPosition(info.position);
		
		switch (item.getItemId()) {
			// update placa
			case R.id.context_updatepark:
				Intent intent = new Intent(this, ParkUpdate.class);
				intent.putExtra("park_id", park.getId());			
				this.startActivity(intent);
				finish();
			return true;
			
			// delete placa
			case R.id.context_deletepark:
				// build a confirmation dialog
			    new AlertDialog.Builder(this)
	        	.setIcon(android.R.drawable.ic_dialog_alert)
	        	.setTitle(R.string.remove_park)
	        	.setMessage(this.getResources().getString(R.string.remove_confirm_message) + " " + park.getPlaca()+" ?")
	        	.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
	        		public void onClick(DialogInterface dialog, int which) {
	        			int deleted = parkDS.deletepark(park);
	        			if(deleted != 0) {
	        				carregarParks(null);
	        				adapter.refresh();
	        			}
	        		}
	        	}).setNegativeButton(R.string.cancel, null).show();
			return true;
			
			default:
				return super.onContextItemSelected(item);
		}
	}
	
	/**
	 * Shows menu view specified in it's XML file view
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_park_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    //Manipulando item de menu
		if(item.getItemId() == R.id.menu_new_park) {
			Intent intent = new Intent(this, ParkCreate.class);
			this.startActivity(intent);
			finish();
		}
		else if(item.getItemId() == R.id.menu_search_park) {
			Intent intent = new Intent(this, ParkSearch.class);
			this.startActivity(intent);
			finish();
		}
		else if(item.getItemId() == R.id.refreshList) {
			carregarParks(null);
		}
		return false;
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
