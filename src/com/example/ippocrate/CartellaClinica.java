package com.example.ippocrate;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;

public class CartellaClinica extends ActionBarActivity {

	private Long idPaziente;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cartella_clinica);

		StrictMode.enableDefaults();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle b = getIntent().getExtras();
		idPaziente = Long.valueOf(b.getLong("idPaziente"));

	}

	@Override
	protected void onStart() {
		super.onStart();

	}
}
