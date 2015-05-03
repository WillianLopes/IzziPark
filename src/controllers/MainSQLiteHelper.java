package controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * ClienteSQliteHelper class, extends SQLiteOpenHelper. Used to create and manage upgrades of the SQLite database.
 * @author Willian Lopes
 *
 */
public class MainSQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLENAMEUSUARIO = "USER";
	public static final String TABLENAMEPARK = "park";
	
	public static final String[] COLUMNSUSUARIO = {"id", "usuario", "senha", "cpf", "email", "dataalteracao"};
	public static final String[] COLUMNSPARK = {"id", "placa", "hora_entrada", "hora_saida", "tipo"};
	
	private static final String DB = "park";
	private static final int DBVERSION = 1;

	// Create table Estacionamento SQL statement
	private static final String CREATE_TABLE_PARK = "CREATE TABLE "+ TABLENAMEPARK + "(" +
			COLUMNSPARK[0] + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " +
			COLUMNSPARK[1] + " TEXT NOT NULL , " +
			COLUMNSPARK[2] + " DATETIME NOT NULL , " +
			COLUMNSPARK[3] + " DATETIME NULL , " +
			COLUMNSPARK[4] + " TEXT NULL  " +
			");";
	
	// Create table Usuário SQL statement
	private static final String CREATE_TABLE_USUARIO = "CREATE TABLE "+ TABLENAMEUSUARIO + "(" +
			COLUMNSUSUARIO[0] + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " +
			COLUMNSUSUARIO[1] + " TEXT NOT NULL , " +
			COLUMNSUSUARIO[2] + " TEXT NOT NULL , " +
			COLUMNSUSUARIO[3] + " TEXT NULL , " +
			COLUMNSUSUARIO[4] + " INTEGER DEFAULT 0 NOT NULL , " +
			COLUMNSUSUARIO[5] + " DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL" +
			");";
	
	// Drop table SQL statement
	private static final String DROP_TABLE_USUARIO = "DROP TABLE IF EXISTS " + TABLENAMEUSUARIO;
	private static final String DROP_TABLE_PARK = "DROP TABLE IF EXISTS " + TABLENAMEPARK;
	
	/**
	 * Constructor. Its usually used by DataSource middle-class
	 * @param context, usually an Activity. 
	 */
	public MainSQLiteHelper(Context context) {
		super(context, DB, null, DBVERSION);
	}

	/**
	 * Method that will be executed when the app is installed or the DB is upgraded.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.e("SQLITE HELPER","DB " + DB +" CRIADO");
		db.execSQL(CREATE_TABLE_USUARIO);
		db.execSQL(CREATE_TABLE_PARK);
		Log.e("SQLITE HELPER","TABLE " + TABLENAMEUSUARIO +" CRIADO");
		Log.e("SQLITE HELPER","TABLE " + TABLENAMEPARK +" CRIADO");
	}

	/**
	 * Method called when DB version is lower than the one specified on DBVERSION variable.
	 * Drops tables and calls onCreate method.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE_PARK);
		db.execSQL(DROP_TABLE_USUARIO);
		onCreate(db);
		Log.e("SQLITE HELPER","DB UPDATED!");
		// TODO Need to migrate data from table!
	}

}
