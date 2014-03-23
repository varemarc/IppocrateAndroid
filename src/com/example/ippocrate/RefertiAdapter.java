package com.example.ippocrate;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RefertiAdapter extends BaseAdapter {

	private static List<String[]> listaRM;

	private LayoutInflater mInflater;

	public RefertiAdapter(Context context, List<String[]> referti) {
		listaRM = referti;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return listaRM.size();
	}

	public String[] getItem(int position) {
		return listaRM.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_referti, null);
			holder = new ViewHolder();
			holder.txtTipoVisita = (TextView) convertView
					.findViewById(R.id.tipoVisita);
			holder.txtData = (TextView) convertView.findViewById(R.id.data);
			holder.txtMedico = (TextView) convertView.findViewById(R.id.medico);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String[] item = getItem(position);
		holder.txtTipoVisita.setText(item[1]);
		holder.txtData.setText("Data: " + item[2]);
		holder.txtMedico.setText("Medico: " + item[3]);

		return convertView;
	}

	static class ViewHolder {
		TextView txtTipoVisita;
		TextView txtData;
		TextView txtMedico;

	}
}
