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

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RefertoMedico extends ActionBarActivity {

	private Long idRM;

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
		String paziente = b.getString("paziente");
		idRM = b.getLong("idRM");
		String[] referto = b.getStringArray("referto");

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

		// TODO multimedia

		List<String[]> prescrizioni = ottieniPM(idRM);

		TableLayout table = (TableLayout) findViewById(R.id.tabellaPrescrizioni);
		table.removeAllViews(); // utile per evitare la duplicazione delle righe
		for (int i = 0; i < prescrizioni.size(); i++) {
			String[] pm = prescrizioni.get(i);
			TableRow row = (TableRow) View.inflate(this,
					R.layout.row_prescrizioni, null);
			row.setId(i);
			((TextView) row.findViewById(R.id.dataPrescrizioneRow))
					.setText(pm[1]);
			((TextView) row.findViewById(R.id.dataScadenzaRow)).setText(pm[2]);
			((TextView) row.findViewById(R.id.medicinaleRow)).setText(pm[3]);
			((TextView) row.findViewById(R.id.quantitaRow)).setText(pm[4]);
			table.addView(row);
		}

	}

	/** Metodo invocato per ottenere le prescrizioni mediche di un referto */
	public List<String[]> ottieniPM(Long idRM) {

		Log.i("ottieniPM", "fase iniziale");

		SoapObject request = new SoapObject(getString(R.string.NAMESPACE),
				"ottieniPM");
		request.addProperty("idRM", idRM);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.setAddAdornments(false);
		envelope.implicitTypes = true;
		envelope.setOutputSoapObject(request);

		HttpTransportSE androidHttpTransport = new HttpTransportSE(
				getString(R.string.URL));

		androidHttpTransport.debug = true;

		List<String[]> prescrizioni = new ArrayList<String[]>();

		try {
			androidHttpTransport.call("ottieniPM", envelope);

			Log.i("ottieniPM", "inviata richiesta");

			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

			Log.i("ottieniPM", "ricevuta risposta");

			String responseData = response.toString();

			JSONObject obj = new JSONObject(responseData);

			Log.i("response", obj.toString());

			JSONArray arr = obj.getJSONArray("prescrizioni");

			for (int i = 0; i < arr.length(); i++) {
				String[] prescrizione = new String[5];
				JSONObject objPres = new JSONObject(arr.getString(i));
				prescrizione[0] = objPres.get("idPM").toString();
				prescrizione[1] = objPres.get("dataPrescrizione").toString();
				prescrizione[2] = objPres.get("dataScadenza").toString();
				prescrizione[3] = objPres.get("medicinale").toString();
				prescrizione[4] = objPres.get("quantita").toString();
				prescrizioni.add(prescrizione);
			}

		} catch (Exception e) {
			Log.e("WS Error->", e.toString());
		}
		return prescrizioni;
	}
}
