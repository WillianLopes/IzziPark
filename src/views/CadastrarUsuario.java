package views;

import models.Endereco;
import models.Usuario;
import util.Mask;
import util.MensagemUtil;
import util.ValidaCPF;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import br.com.izzipark.R;
import controllers.LoginDataSource;
import controllers.WebClient;

/**
 * ClienteCreate class, Activity that allows user to add a new Cliente register into SQLite database
 * @author Willian Lopes
 *
 */
public class CadastrarUsuario extends Activity implements OnClickListener {
	
	private Button btnSalvarPerfil;
	private Button btnVoltarMenu;
	private EditText edtUsuario_senha, edtUsuario_login, edtUsuario_email, edtUsuario_cpf, edtUsuario_cep;
	TextWatcher maskCpf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_perfil);
		
		edtUsuario_login = (EditText) findViewById(R.id.edtUsuario_login);
		edtUsuario_senha = (EditText) findViewById(R.id.edtUsuario_senha);
		edtUsuario_email = (EditText) findViewById(R.id.edtUsuario_email);
		edtUsuario_cpf = (EditText) findViewById(R.id.edtUsuario_cpf);
		edtUsuario_cep = (EditText) findViewById(R.id.edtUsuario_cep);
		
		maskCpf = Mask.insert("###.###.###-##", edtUsuario_cpf);
		edtUsuario_cpf.addTextChangedListener(maskCpf);
		
		edtUsuario_cep.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					if(edtUsuario_cep.getText().toString().length() > 0){
						new BuscaCepTask().execute();
						//Toast.makeText(v.getContext(), "Perdeu Foco", Toast.LENGTH_LONG).show();
					}else{
						MensagemUtil.mostrarMsg(CadastrarUsuario.this, "O campo CEP deve ser preenchido!");
					}
				}
			}
		});
		
		btnSalvarPerfil = (Button)findViewById(R.id.btnSalvar_perfil);
		btnVoltarMenu = (Button)findViewById(R.id.btnSair_perfil);
		
		btnSalvarPerfil.setOnClickListener(this);
		btnVoltarMenu.setOnClickListener(this);
	}
	
	@SuppressWarnings("unused")
	private void OnFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.edtUsuario_cep:
				
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnSalvar_perfil:
			edtUsuario_login.getText();
			edtUsuario_senha.getText();
			edtUsuario_email.getText();
			edtUsuario_cpf.getText();
			String cpf = edtUsuario_cpf.getText().toString().replace(".", "").replace("-", "");
			if(ValidaCPF.isCPF(cpf) == true){
				if(!edtUsuario_login.getText().toString().equals("") && !edtUsuario_senha.getText().toString().equals("") ) {
					Usuario user = new Usuario();
					user.setUsuario(edtUsuario_login.getText().toString());
					user.setSenha(edtUsuario_senha.getText().toString());
					user.setCpf(edtUsuario_cpf.getText().toString());
					user.setEmail(edtUsuario_email.getText().toString());
					addUsuario(user);	
				}
				else {
					Toast.makeText(this, "Campo usuário e senha são obrigatórios.", Toast.LENGTH_SHORT).show();
				}
			}else{
				MensagemUtil.mostrarMsg(this, "CPF Inválido");
			}
			
			break;
		case R.id.btnSair_perfil:
			finish();
			break;
		}
	}
	
	public void addUsuario(Usuario user) { 
		LoginDataSource loginDS = new LoginDataSource(this);
		loginDS.open(true); //Abre conex�o com o DB
		if(loginDS.addUsuario(user) != -1){
			new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(R.string.added_user)
				.setMessage(user.getUsuario() + " " + getResources().getString(R.string.added_park_msg))
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(CadastrarUsuario.this, LoginActivity.class);
						startActivity(i);
						finish();
					}
				})
				.show();
		}
		loginDS.close(); // open DB connection
	}
	
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	private String montarURL(String cep){
		return "http://cep.republicavirtual.com.br/web_cep.php?cep=".concat(cep).concat("&formato=json");
	}
	
	private class BuscaCepTask extends AsyncTask<Void, Void, Void>{

		private String retorno;
		//private ProgressDialog progressDialog = new ProgressDialog(CadastrarUsuario.this);
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//progressDialog.setMessage("Carregando...");
			//progressDialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			WebClient client = new WebClient(montarURL(edtUsuario_cep.getText().toString()));
			retorno = client.obterEndereco();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			//barraProgresso.setVisibility(4);
			Endereco endereco = new Endereco();
			
			if(endereco.parseJSON(retorno) != null){
				Endereco endParse = endereco.parseJSON(retorno);
				if(endParse.getResultado() == 0){
					MensagemUtil.mostrarMsg(CadastrarUsuario.this, "Não foi encontrado registro referente ao CEP informado!");
					//txtResultado.setText();
					//txtResultado.setGravity(1);
				}else{
					MensagemUtil.mostrarMsgOk(CadastrarUsuario.this, "Detalhes CEP",
					"Endereço encontrado:\n\nLogradouro: "+endParse.getTipoLogradouro()+" "+endParse.getLogradouro()+
					"\nBairro: "+endParse.getBairro()+
					"\nCidade: "+endParse.getCidade()+
					"\nUF: "+endParse.getUf(), R.drawable.ic_about);
					//txtResultado.setGravity(0);
				}
				
			}else{
				MensagemUtil.mostrarMsg(CadastrarUsuario.this, 
						"Um erro ocorreu!=(\nNão foi possível obter informações sobre o CEP!");
				//txtResultado.setGravity(1);
			}
			
		}
	}
}
