package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import models.Park;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ParkDataSource {

	private SQLiteDatabase db;
	private MainSQLiteHelper dbHelper;
	private String[] columns;
	
	/**
	 * Constructor
	 * @param context, usually an Activity
	 */
	public ParkDataSource(Context context) {
		dbHelper = new MainSQLiteHelper(context);
		columns = dbHelper.COLUMNSPARK;
	}
	
	/**
	 * Open SQLite database connection
	 * @param writable, whether is true or false it will open a DB connection on R+W or R modes
	 * @throws SQLException
	 */
	public void open(boolean writable) throws SQLException {
		if(writable) {
			db = dbHelper.getWritableDatabase();
		} else {
			db = dbHelper.getReadableDatabase();
		}
	}
	
	/**
	 * Closes dbHelper and SQLite database connection
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		dbHelper.close();
		db.close();
	}
	
	public int updatepark(Park park) {
		int updated = -1;
		
		if(parkExists(park)) {
			ContentValues values = new ContentValues();
			values.put(columns[1], park.getPlaca());
			values.put(columns[2], park.getHora_entrada());
			values.put(columns[3], park.getHora_saida());
			values.put(columns[4], park.getTipo());
			String whereClause = columns[0] + " = '" + park.getId() + "'";
			System.out.println(whereClause);
			updated = db.update(dbHelper.TABLENAMEPARK, values, whereClause, null);			
		}
		return updated;
	}
	
	public long addpark(Park park) {
		long inserted = -1; // token 
		if (!parkExists(park)) {
			ContentValues values = new ContentValues();
			values.put(columns[1], park.getPlaca());
			values.put(columns[2], park.getHora_entrada());
			values.put(columns[3], park.getHora_saida());
			values.put(columns[4], park.getTipo());
			inserted = db.insert(dbHelper.TABLENAMEPARK, null, values);
		}
		return inserted;
	}
	
	public Park getpark(int id){
		Park park = new Park();
		String selection = columns[0] + " = "+id+"";
		Cursor cur = db.query(dbHelper.TABLENAMEPARK, columns, selection, null, null, null, null);
		cur.moveToFirst(); // need to start the cursor first...!
		while(!cur.isAfterLast()) {
			park.setId(cur.getInt(0));
			park.setPlaca(cur.getString(1));
			park.setHora_entrada(cur.getString(2));
			park.setHora_saida(cur.getString(3));
			park.setTipo(cur.getString(4));
			cur.moveToNext();
		}
		cur.close(); // !important
		return park;
	}
	
	public boolean parkExists(Park park) {
		String selection = columns[0] + " = " + park.getId() + "";
		Cursor cur = db.query(dbHelper.TABLENAMEPARK, null, selection, null, null, null, null);
		boolean exists = cur.moveToFirst();
		cur.close(); // !important
		return exists;
	}
	
	public int deletepark(Park park) {
		int deleted = db.delete(dbHelper.TABLENAMEPARK, "id = " + park.getId(), null);
		return deleted;
	}
	
	public List<Park> getParkTituloOrDescricao(String placa) {
		List<Park> park = new ArrayList<Park>();
		String selection = "";
		
		selection += columns[1] + " like '%" + placa + "%'";
			
		Cursor cur = db.query(dbHelper.TABLENAMEPARK, columns, selection, null, null, null, null);
		cur.moveToFirst(); // need to start the cursor first...!
		while(!cur.isAfterLast()) { // while not end of data stored in table...
			Park pk = new Park();
			pk.setId(cur.getInt(0));
			pk.setPlaca(cur.getString(1));
			pk.setHora_entrada(cur.getString(2));
			pk.setHora_saida(cur.getString(3));
			pk.setTipo(cur.getString(4));
			park.add(pk); 
			cur.moveToNext(); // next loop
		}
		cur.close(); // !important
		return park;
		
	}
	
	public List<Park> getparks() {
		
		Calendar c = Calendar.getInstance(); 
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		String today = year + "-" + (month + 1) + "-" + day;

		List<Park> park = new ArrayList<Park>();
		
		Cursor cur = db.rawQuery("SELECT * FROM PARK", null);
		//Cursor cur = db.query(dbHelper.TABLENAMEpark, columns, null, null, null, null, columns[4] + " DESC", null);
		cur.moveToFirst(); // need to start the cursor first...!
		while(!cur.isAfterLast()) { // while not end of data stored in table...
			Park pk = new Park();
			pk.setId(cur.getInt(0));
			pk.setPlaca(cur.getString(1));
			pk.setHora_entrada(cur.getString(2));
			pk.setHora_saida(cur.getString(3));
			pk.setTipo(cur.getString(4));
			park.add(pk);
			cur.moveToNext(); // next loop
		}
		cur.close(); // !important
		return park;
	}
	
}
