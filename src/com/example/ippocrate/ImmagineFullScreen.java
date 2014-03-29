package com.example.ippocrate;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class ImmagineFullScreen extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// If the Android version is lower than Jellybean, use this call to hide
		// the status bar.
		if (Build.VERSION.SDK_INT < 16) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		setContentView(R.layout.activity_immagine_full_screen);

		View decorView = getWindow().getDecorView();
		// Hide the status bar.
		int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		decorView.setSystemUiVisibility(uiOptions);
		// Remember that you should never show the action bar if the
		// status bar is hidden, so hide that too if necessary.
		getActionBar().hide();

		Bitmap img = (Bitmap) getIntent().getParcelableExtra("img");
		((ImageView) findViewById(R.id.imgFullScreen)).setImageBitmap(img);

	}

	@Override
	public void onBackPressed() {
		// Destroy activity.
		finish();
	}

}
