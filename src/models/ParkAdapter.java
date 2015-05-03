package models;

import java.util.List;

import util.MaskTextView;
import views.ParkActivity;
import views.ParkDetalhes;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.izzipark.R;
import controllers.ParkDataSource;

public class ParkAdapter extends BaseAdapter  {

	Context _context;
	List<Park> _park;
	ParkDataSource _parkDS;
	
	public ParkAdapter(Context context, List<Park> pk, ParkDataSource parkDS) {
		this._parkDS = parkDS;
		this._context = context;
		this._park = pk;
	}
	
	/**
	 * @return how many list items we have got in our list adapter.
	 */
	public int getCount() {
		return _park.size();
	}

	/**
	 * @param location, index
	 * @return  object of the list at the specified location
	 */
	public Object getItem(int location) {
		return _park.get(location);
	}

	/**
	 * @param location, index
	 * @return an identifier for the object specified in the location of the list
	 */
	public long getItemId(int location) {
		return _park.get(location).getId();
	}

	public View getView(final int location, View convertView, ViewGroup arg2) {
		View customView = null;
		if(convertView == null) {
			// must generate a new view from the item XML layout
			LayoutInflater inflater = (LayoutInflater) 
					_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // got inflater service
			customView = inflater.inflate(R.layout.park_listitem, null);
		} else {
			customView = convertView;
		}
		
		// Set TextView's values
		TextView tvPlaca = (TextView) customView.findViewById(R.id.tv_placa);
		TextView tvEntrada = (TextView) customView.findViewById(R.id.tv_entrada);
		TextView tvSaida = (TextView) customView.findViewById(R.id.tv_saida);
		
		if(_park.get(location).getPlaca() != null && !_park.get(location).getPlaca().equals(""))
			tvPlaca.setText(_park.get(location).getPlaca());
		else
			tvPlaca.setText("Sem Placa");
		
		if(_park.get(location).getHora_entrada() != null && !_park.get(location).getHora_entrada().equals("")) {
			tvEntrada.setText("> " +_park.get(location).getHora_entrada());	
		}
		else {
			tvEntrada.setText("Sem horário de entrada");
		}
		
		if(_park.get(location).getHora_saida() != null && !_park.get(location).getHora_saida().equals("")) {
			tvSaida.setText("< " + _park.get(location).getHora_saida());
		}
		else {
			tvSaida.setText("Sem horário de saída");
		}

			
		customView.setOnClickListener(new OnClickListener() {	
			public void onClick(View v) {
				Park selected = _park.get(location);
				Intent intent = new Intent(_context, ParkDetalhes.class);
				intent.putExtra("park_id", selected.getId());
				_context.startActivity(intent);
				((Activity) _context).finish();
			}
		});
		
		customView.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View arg0) {
				// 
				return false;
			}}); 
		return customView;
	}
	
	/**
	 * used to refresh/notify list adapter changes
	 */
	public void refresh(){
		this.notifyDataSetChanged();
	};

	
}
