package com.example.ippocrate;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class RefertoMedico extends ActionBarActivity {

	private String paziente;
	private Long idReferto;
	private String[] referto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_referto_medico);

		StrictMode.enableDefaults();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle b = getIntent().getExtras();
		paziente = b.getString("paziente");
		idReferto = b.getLong("idReferto");
		referto = b.getStringArray("referto");

		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText("Referto medico di " + paziente);

		tv = (TextView) findViewById(R.id.tipoDiVisita);
		tv.setText(referto[1]);

		tv = (TextView) findViewById(R.id.dataVisita);
		tv.setText(referto[2]);

		tv = (TextView) findViewById(R.id.medico);
		tv.setText(referto[3]);

		tv = (TextView) findViewById(R.id.diagnosi);
		tv.setText(referto[4]);
	}

	@Override
	protected void onStart() {
		super.onStart();
		//TODO
	}
}
