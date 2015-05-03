package views;

import models.ValidacaoLogin;
import util.MensagemUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.com.izzipark.R;
import controllers.LoginBO;
import controllers.LoginDataSource;

public class LoginActivity extends Activity implements OnClickListener {

	private LoginBO loginBO;
	private Button btnEntrar, btnCadastrar;
	private EditText edtUsuario_login, edtSenha_login;
	LoginDataSource loginDS = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_login);

		loginDS = new LoginDataSource(this);
		loginDS.open(true); // open DB connection in writable mode, because of Delete option
		
		edtUsuario_login = (EditText) findViewById(R.id.edtUsuario_login);
		edtSenha_login = (EditText) findViewById(R.id.edtSenha_login);

		btnEntrar = (Button) findViewById(R.id.btnEntrar);
		btnEntrar.setOnClickListener(this);
		
		btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
		btnCadastrar.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnEntrar:
				//LoginDataSource loginDS = new LoginDataSource(this);
				//int resultado = loginDS.loginExists(edtUsuario_login.getText().toString(), edtSenha_login.getText().toString());
				/*if(edtUsuario_login.getText().toString().equals("") && edtSenha_login.getText().toString().equals("")) {
					Toast.makeText(this, "Usuário ou senha incorretos.", Toast.LENGTH_SHORT).show();
				}
				else {
					Intent i = new Intent(this, MainActivity.class);
					startActivity(i);
					finish();
				}*/
				new LoadingAsync().execute();
			break;
			case R.id.btnCadastrar:
				Intent j = new Intent(this, CadastrarUsuario.class);
				startActivity(j);
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
		.setTitle("Sair")
		.setMessage("Tem certeza que deseja fechar a aplicação?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		    	Intent exitApp = new Intent(getApplicationContext(), LoginActivity.class);
	            exitApp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivityForResult(exitApp, 1);
	            finishActivity(1);
		    }})
		.setNegativeButton(android.R.string.no, null).show();
    }
	
	private class LoadingAsync extends AsyncTask<Void, Void, ValidacaoLogin> {

		private ProgressDialog progressDialog = new ProgressDialog(
				LoginActivity.this);

		@Override
		protected void onPreExecute() {
			progressDialog.setMessage("Carregando...");
			progressDialog.show();
		}

		@Override
		protected ValidacaoLogin doInBackground(Void... params) {

			String login = edtUsuario_login.getText().toString();
			String senha = edtSenha_login.getText().toString();

			loginBO = new LoginBO(LoginActivity.this);

			return loginBO.validarLogin(login, senha);
		}

		
		@Override
		protected void onPostExecute(ValidacaoLogin validacao) {
			progressDialog.dismiss();

			if (validacao.isValido()) {
				Intent i = new Intent(LoginActivity.this,
						MainActivity.class);
				i.putExtra("msg", validacao.getMensagem());
				startActivity(i);
				finish();
			} else {
				MensagemUtil.mostrarMsg(LoginActivity.this,
						validacao.getMensagem());
			}
		}
	}
}
