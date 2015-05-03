package controllers;

import java.util.ArrayList;
import java.util.List;

import br.com.izzipark.R;

import util.MensagemUtil;
import views.LoginActivity;

import models.Usuario;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class LoginDataSource {

	private SQLiteDatabase db;
	private MainSQLiteHelper dbHelper;
	private String[] columns;
	
	/**
	 * Constructor
	 * @param context, usually an Activity
	 */
	public LoginDataSource(Context context) {
		dbHelper = new MainSQLiteHelper(context);
		columns = dbHelper.COLUMNSUSUARIO;
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
	
	public int updatepark(Usuario usuario) {
		int updated = -1;
		
		if(parkExists(usuario)) {
			ContentValues values = new ContentValues();
			values.put(columns[1], usuario.getUsuario());
			values.put(columns[2], usuario.getSenha());
			values.put(columns[3], usuario.getCpf());
			values.put(columns[4], usuario.getEmail());
			String whereClause = columns[0] + " = '" + usuario.getId() + "'";
			System.out.println(whereClause);
			updated = db.update(dbHelper.TABLENAMEUSUARIO, values, whereClause, null);			
		}
		return updated;
	}
	
	public long addUsuario(Usuario usuario) {
		long inserted = -1; // token 
		if (!parkExists(usuario)) {
			ContentValues values = new ContentValues();
			values.put(columns[1], usuario.getUsuario());
			values.put(columns[2], usuario.getSenha());
			values.put(columns[3], usuario.getCpf());
			values.put(columns[4], usuario.getEmail());
			inserted = db.insert(dbHelper.TABLENAMEUSUARIO, null, values);
		}
		return inserted;
	}
	
	public Usuario getpark(int id){
		Usuario usr = new Usuario();
		String selection = columns[0] + " = "+id+"";
		Cursor cur = db.query(dbHelper.TABLENAMEUSUARIO, columns, selection, null, null, null, null);
		cur.moveToFirst(); // need to start the cursor first...!
		while(!cur.isAfterLast()) {
			usr.setId(cur.getInt(0));
			usr.setUsuario(cur.getString(1));
			usr.setSenha(cur.getString(2));
			usr.setCpf(cur.getString(3));
			usr.setEmail(cur.getString(4));
			cur.moveToNext();
		}
		cur.close(); // !important
		return usr;
	}
	
	public boolean parkExists(Usuario usuario) {
		String selection = columns[0] + " = " + usuario.getId() + "";
		Cursor cur = db.query(dbHelper.TABLENAMEUSUARIO, null, selection, null, null, null, null);
		boolean exists = cur.moveToFirst();
		cur.close(); // !important
		return exists;
	}
	
	public int deletepark(Usuario usuario) {
		int deleted = db.delete(dbHelper.TABLENAMEUSUARIO, "id = " + usuario.getId(), null);
		return deleted;
	}
	
	public List<Usuario> getUsuarios() {
		List<Usuario> user = new ArrayList<Usuario>();
		Cursor cur = db.query(dbHelper.TABLENAMEUSUARIO, columns, null, null, null, null, null);
		cur.moveToFirst(); // need to start the cursor first...!
		while(!cur.isAfterLast()) { // while not end of data stored in table...
			Usuario usr = new Usuario();
			usr.setId(cur.getInt(0));
			usr.setUsuario(cur.getString(1));
			usr.setSenha(cur.getString(2));
			usr.setCpf(cur.getString(3));
			usr.setEmail(cur.getString(4));
			user.add(usr);
			cur.moveToNext(); // next loop
		}
		cur.close(); // !important
		return user;
	}
	
	public int loginExists(String user, String senha) {
		// TODO Auto-generated method stub
		String selection = "usuario = '" + user + "' AND senha = '" + senha + "'";
		Cursor cur = db.rawQuery("SELECT 1 FROM USER WHERE USUARIO = '" + user + "' AND SENHA = '" + senha + "'", null);
		int exists = cur.getColumnCount();
		cur.close(); // !important
		return exists;
	}
	
	public boolean validarLogin(String usuario, String senha) {
		try{
			db = dbHelper.getReadableDatabase();
			Cursor cursor = db.query("USER", null, "USUARIO = ? AND SENHA = ?",
					new String[] { usuario, senha }, null, null, null);
	
			if (cursor.moveToFirst()) {
				return true;
			}
		}catch(SQLiteException e){
			
		}
		
		return false;
	}
	
}
