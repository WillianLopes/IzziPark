package views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import br.com.izzipark.R;

public class SobreActivity extends Activity implements OnClickListener {

	private Button btnSair_sobre;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_sobre);

		btnSair_sobre = (Button) findViewById(R.id.btnSair_sobre);
		btnSair_sobre.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnSair_sobre:
				finish();
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
    }
}
