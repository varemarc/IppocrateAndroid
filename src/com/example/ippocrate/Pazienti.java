package com.example.ippocrate;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.os.Bundle;
import android.os.StrictMode;

public class Pazienti extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.enableDefaults();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);

		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_pazienti);

		Bundle b = getIntent().getExtras();
		Long idM = new Long(b.getLong("idM"));

		List<String[]> pazienti = trovaPazienti(idM);

	}

	/** Metodo invocato per ottenere i pazienti di un medico */
	public List<String[]> trovaPazienti(Long idM) {

		Log.i("trovaPazienti", "fase iniziale");

		SoapObject request = new SoapObject(getString(R.string.NAMESPACE),
				"trovaPazienti");
		request.addProperty("idM", idM);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setAddAdornments(false);
		envelope.implicitTypes = true;
		envelope.setOutputSoapObject(request);

		HttpTransportSE androidHttpTransport = new HttpTransportSE(
				getString(R.string.URL));

		androidHttpTransport.debug = true;
		
		ArrayList<String[]> pazienti = new ArrayList<String[]>();
		
		try {
			androidHttpTransport.call("trovaPazienti", envelope);

			Log.i("trovaPazienti", "inviata richiesta");

			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

			Log.i("trovaPazienti", "ricevuta risposta");

			String responseData = response.toString();

			JSONObject obj = new JSONObject(responseData);
			JSONArray arr = obj.getJSONArray("mieiPazienti");

			Log.i("mieiPazienti", arr.toString());

			for (int i = 0; i < arr.length(); i++) {
				String[] paziente = new String[2];
				JSONObject objPaz = new JSONObject(arr.getString(i));
				paziente[0] = objPaz.get("idP").toString();
				paziente[1] = objPaz.get("nome").toString() + " " + objPaz.get("cognome").toString();
				pazienti.add(paziente);
			}

		} catch (Exception e) {
			Log.e("WS Error->", e.toString());
		}
		return pazienti;
	}

}
