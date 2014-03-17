package com.example.ippocrate;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.support.v7.app.ActionBarActivity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.enableDefaults();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);

		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_main);

		Button accedi = (Button) findViewById(R.id.accedi);
		accedi.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				EditText et1 = (EditText) findViewById(R.id.username);
				EditText et2 = (EditText) findViewById(R.id.pincode);
				EditText et3 = (EditText) findViewById(R.id.password);

				String username = et1.getText().toString();
				String pincode = et2.getText().toString();
				String password = et3.getText().toString();

				if (login(username, pincode, password)) {
					// TODO: lanciare una nuova Activity e passare il parametro idM
				} else {
					LayoutInflater inflater = getLayoutInflater();
					View layout = inflater.inflate(R.layout.error_toast,
							(ViewGroup) findViewById(R.id.error_toast_layout));

					TextView text = (TextView) layout.findViewById(R.id.error_toast_text);
					text.setText("Login fallito!");
					text.setTypeface(null, Typeface.BOLD);

					Toast toast = new Toast(getApplicationContext());
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setView(layout);
					toast.show();
				}
			}
		});
	}

	/** Metodo invocato al click del pulsante di login */
	public boolean login(String username, String pincode, String password) {

		Log.i("login", "fase iniziale");

		SoapObject request = new SoapObject(getString(R.string.NAMESPACE),
				"effettuaLogin");
		request.addProperty("username", username);
		request.addProperty("pincode", pincode);
		request.addProperty("password", password);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setAddAdornments(false);
		envelope.implicitTypes = true;
		envelope.setOutputSoapObject(request);

		HttpTransportSE androidHttpTransport = new HttpTransportSE(
				getString(R.string.URL));

		androidHttpTransport.debug = true;
		try {
			androidHttpTransport.call("effettuaLogin", envelope);

			Log.i("login", "inviata richiesta");

			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

			Log.i("login", "ricevuta risposta");

			String responseData = response.toString();

			JSONObject obj = new JSONObject(responseData);
			String ris = (String) obj.get("loginOK");

			Log.i("loginOK", ris);

			if (ris.equals("-1") == false) {
				Log.i("login", "confermato");
				return true;
			} else {
				Log.i("login", "rifiutato");
				return false;
			}

		} catch (Exception e) {
			Log.e("WS Error->", e.toString());
			return false;
		}
	}
}
