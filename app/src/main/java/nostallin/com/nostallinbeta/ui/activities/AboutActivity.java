package nostallin.com.nostallinbeta.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nostallin.com.nostallinbeta.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
