package util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Toast;

public class MensagemUtil {
	/**
	 * método add msg no padrão Toast, de tempo curto
	 * @param activity
	 * @param msg
	 */
	public static void mostrarMsg(Activity activity, String msg) {
		// primeiro cria a msg depois a mostra
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * método para exibir mensagens que necessitam de confirmação de visualização
	 * @param activity
	 * @param title
	 * @param msg
	 * @param icone
	 */
	public static void mostrarMsgOk(Activity activity, String title,
			String msg, int icone) {
		AlertDialog.Builder builderDialog = new AlertDialog.Builder(activity);
		builderDialog.setTitle(title);
		builderDialog.setMessage(msg);
		builderDialog.setNeutralButton("Ok", null);
		builderDialog.setIcon(icone);
		builderDialog.show();
	}
	
	/**
	 * Método para criação de uma mensagem de diálogo com opções de sim ou nao
	 * @param activity
	 * @param title
	 * @param msg
	 * @param icone
	 * @param listener
	 */
	public static void mostrarMsgConfirm(Activity activity, String title,
			String msg, int icone, OnClickListener listener){
		AlertDialog.Builder builderDialog = new AlertDialog.Builder(activity);
		builderDialog.setTitle(title);
		builderDialog.setMessage(msg);
		builderDialog.setPositiveButton("Sim", listener);
		builderDialog.setNegativeButton("Não", null);
		builderDialog.setIcon(icone);
		builderDialog.show();
	}
}
