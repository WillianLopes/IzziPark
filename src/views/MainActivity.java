package views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import br.com.izzipark.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements OnClickListener {

	private RelativeLayout btnCadastrarpark, btnSobre;
	private ImageButton btnSpeech, btnNext;
	private Button btnSair;

    private final int REQ_CODE_SPEECH_INPUT = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_menu);

		btnCadastrarpark = (RelativeLayout) findViewById(R.id.btnCadastrarpark);
		btnSobre = (RelativeLayout) findViewById(R.id.btnSobre);
		btnSair = (Button) findViewById(R.id.btnSair);

		exportDB();
		btnCadastrarpark.setOnClickListener(this);
		btnSobre.setOnClickListener(this);
		btnSair.setOnClickListener(this);
	
	}
	
	private void exportDB(){
		File sd = Environment.getExternalStorageDirectory();
      	File data = Environment.getDataDirectory();
	    FileChannel source=null;
	    FileChannel destination=null;
	    String currentDBPath = "/data/br.com.izzipark/databases/park";
	    String backupDBPath = "park.db";
	    File currentDB = new File(data, currentDBPath);
	    File backupDB = new File(sd, backupDBPath);
	    try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        } catch(IOException e) {
        	e.printStackTrace();
        }
	}
 
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
       
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnCadastrarpark:		
				Intent intentpark = new Intent(this, ParkActivity.class);
				this.startActivity(intentpark);
				//finish();
			break;
			case R.id.btnSobre:
				Intent intentSobre = new Intent(this, SobreActivity.class);
				this.startActivity(intentSobre);
				//finish();
			break;
			case R.id.btnSair:
				new AlertDialog.Builder(this)
				.setTitle("Sair")
				.setMessage("Deseja sair da aplicação?")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

				    public void onClick(DialogInterface dialog, int whichButton) {
				    	Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
						startActivity(intentLogin);
						finish();
				    }})
				.setNegativeButton(android.R.string.no, null).show();
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
    	Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
		startActivity(intentLogin);
		finish();
    }
}
