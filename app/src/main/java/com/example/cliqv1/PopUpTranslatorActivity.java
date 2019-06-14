package com.example.cliqv1;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class PopUpTranslatorActivity extends Activity {

    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_translator);

        btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                onBackPressed();

            }
        });

    DisplayMetrics dm = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(dm);

    int width = dm.widthPixels;
    int height = dm.heightPixels;

    getWindow().setLayout((int)(width*.8),(int)(height*.6));

    WindowManager.LayoutParams params = getWindow().getAttributes();
    params.gravity = Gravity.CENTER;
    params.x = 0;
    params.y = -20;

    getWindow().setAttributes(params);

}
}
