package controllers;

import models.ValidacaoLogin;
import br.com.izzipark.R;
import android.content.Context;

public class LoginBO {
	
	private Context context;
	
	private LoginDataSource loginDataSource;
	
	public LoginBO(Context context){
		this.context = context;
		loginDataSource = new LoginDataSource(context);
	}
	
	public ValidacaoLogin validarLogin(String login, String senha){
		ValidacaoLogin retorno = new ValidacaoLogin();
		
		if (login.equals("") || login == null) {		
			retorno.setValido(false);
			retorno.setMensagem(context.getString(R.string.msg_login_invalido));			
		}else if (senha.equals("") || senha == null) {		
			retorno.setValido(false);
			retorno.setMensagem(context.getString(R.string.msg_senha_obrg));	
		}else if (loginDataSource.validarLogin(login, senha)){
			retorno.setValido(true);
			retorno.setMensagem(context.getString(R.string.msg_login_sucesso));
		}else{
			retorno.setValido(false);
			retorno.setMensagem(context.getString(R.string.msg_login_invalido));
		}
		return retorno;		
	}
}
