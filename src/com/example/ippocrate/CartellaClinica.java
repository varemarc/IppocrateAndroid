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

public class CartellaClinica extends ActionBarActivity {

	private Long idPaziente;
	private String paziente;
	private List<Object> ccConReferti;

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
		paziente = b.getString("paziente");

		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText("Cartella clinica di " + paziente);
	}

	@Override
	protected void onStart() {
		super.onStart();

		ccConReferti = ottieniCC(idPaziente);
		String anamnesi = ccConReferti.get(1).toString();

		TextView tvAnamnesi = (TextView) findViewById(R.id.anamnesi);
		tvAnamnesi.setText(anamnesi);

		List<String[]> referti = (List<String[]>) ccConReferti.get(2);

		TableLayout table = (TableLayout) findViewById(R.id.tabellaReferti);
		for (int i = 0; i < referti.size(); i++) {
			String[] r = referti.get(i);
			TableRow row = (TableRow) View.inflate(this, R.layout.row_referti,
					null);
			row.setId(i);
			((TextView) row.findViewById(R.id.tipoVisita)).setText(r[1]);
			((TextView) row.findViewById(R.id.data)).setText(r[2]);
			((TextView) row.findViewById(R.id.medico)).setText(r[3]);
			table.addView(row);
		}
	}

	public void clickRow(View v) {
		// TODO
	}

	/** Metodo invocato per ottenere la cartella clinica di un paziente */
	public List<Object> ottieniCC(Long idPaziente) {

		Log.i("ottieniCC", "fase iniziale");

		SoapObject request = new SoapObject(getString(R.string.NAMESPACE),
				"ottieniCC");
		request.addProperty("idPaziente", idPaziente);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.setAddAdornments(false);
		envelope.implicitTypes = true;
		envelope.setOutputSoapObject(request);

		HttpTransportSE androidHttpTransport = new HttpTransportSE(
				getString(R.string.URL));

		androidHttpTransport.debug = true;

		List<Object> cc = new ArrayList<Object>();

		try {
			androidHttpTransport.call("ottieniCC", envelope);

			Log.i("ottieniCC", "inviata richiesta");

			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

			Log.i("ottieniCC", "ricevuta risposta");

			String responseData = response.toString();

			JSONObject obj = new JSONObject(responseData);

			Log.i("response", obj.toString());

			cc.add(obj.get("idCC").toString());
			cc.add(obj.get("anamnesi").toString());

			List<String[]> referti = new ArrayList<String[]>();
			JSONArray arr = obj.getJSONArray("referti");

			for (int i = 0; i < arr.length(); i++) {
				String[] referto = new String[5];
				JSONObject objRef = new JSONObject(arr.getString(i));
				referto[0] = objRef.get("idRM").toString();
				referto[1] = objRef.get("tipoVisita").toString();
				referto[2] = objRef.get("data").toString();
				referto[3] = objRef.get("medico").toString();
				referto[4] = objRef.get("diagnosi").toString();
				referti.add(referto);
			}
			cc.add(referti);

		} catch (Exception e) {
			Log.e("WS Error->", e.toString());
		}
		return cc;
	}

}
