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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Pazienti extends ActionBarActivity {

	private Long idMedico;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pazienti);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle b = getIntent().getExtras();
		idMedico = Long.valueOf(b.getLong("idMedico"));

	}

	@Override
	protected void onStart() {
		super.onStart();

		new Connection().execute(idMedico);

	}

	private class Connection extends AsyncTask<Long, Void, List<String[]>> {

		private ProgressDialog progDailog;

		@Override
		protected void onPreExecute() {
			progDailog = new ProgressDialog(Pazienti.this);
			progDailog.setMessage("Loading...");
			progDailog.setIndeterminate(false);
			progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDailog.setCancelable(true);
			progDailog.show();
		}

		@Override
		protected List<String[]> doInBackground(Long... params) {
			return trovaPazienti(params[0]);
		}

		@Override
		protected void onPostExecute(final List<String[]> pazientiConId) {
			List<String> listaPazienti = new ArrayList<String>();

			for (int i = 0; i < pazientiConId.size(); i++) {
				String[] elem = pazientiConId.get(i);
				listaPazienti.add(elem[1]);
			}

			ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
					Pazienti.this, R.layout.row_pazienti, listaPazienti);
			ListView mainListView = (ListView) findViewById(R.id.listView);
			mainListView.setAdapter(listAdapter);
			
			progDailog.dismiss();
			
			mainListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> a, View v, int position,
						long id) {
					String[] elem = pazientiConId.get(position);
					Long idPaziente = Long.valueOf(elem[0]);
					String paziente = elem[1];

					Intent intent = new Intent(Pazienti.this,
							CartellaClinica.class);
					Bundle b = new Bundle();
					b.putLong("idPaziente", idPaziente.longValue());
					b.putString("paziente", paziente);
					intent.putExtras(b);
					startActivity(intent);
				}
			});
		}

		/** Metodo invocato per ottenere i pazienti di un medico */
		private List<String[]> trovaPazienti(Long idMedico) {

			Log.i("trovaPazienti", "fase iniziale");

			SoapObject request = new SoapObject(getString(R.string.NAMESPACE),
					"trovaPazienti");
			request.addProperty("idMedico", idMedico);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER10);
			envelope.setAddAdornments(false);
			envelope.implicitTypes = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					getString(R.string.URL));

			androidHttpTransport.debug = true;

			List<String[]> pazienti = new ArrayList<String[]>();

			try {
				String soapAction = "\"" + getString(R.string.NAMESPACE)
						+ "trovaPazienti" + "\"";
				androidHttpTransport.call(soapAction, envelope);

				Log.i("trovaPazienti", "inviata richiesta");

				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

				Log.i("trovaPazienti", "ricevuta risposta");

				String responseData = response.toString();

				JSONObject obj = new JSONObject(responseData);

				Log.i("response", obj.toString());

				JSONArray arr = obj.getJSONArray("mieiPazienti");

				for (int i = 0; i < arr.length(); i++) {
					String[] paziente = new String[2];
					JSONObject objPaz = new JSONObject(arr.getString(i));
					paziente[0] = objPaz.get("idPaziente").toString();
					paziente[1] = objPaz.get("nome").toString() + " "
							+ objPaz.get("cognome").toString();
					pazienti.add(paziente);
				}

			} catch (Exception e) {
				Log.e("WS Error->", e.toString());
			}
			return pazienti;
		}
	}

}
