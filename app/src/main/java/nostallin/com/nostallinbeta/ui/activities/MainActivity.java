package nostallin.com.nostallinbeta.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import nostallin.com.nostallinbeta.R;
import nostallin.com.nostallinbeta.navigator.MainNavigator;
import nostallin.com.nostallinbeta.source.PlaceSource;
import nostallin.com.nostallinbeta.usecase.CheckIfPlaceExistsUseCase;

public class MainActivity extends AppCompatActivity {

    // region Constants

    private static final int PLACE_PICKER_CODE = 1;

    // endregion

    // region Views

    private Button lokkingButton;
    private Button wentButton;

    // endregion
    
    // region Private Members

    CheckIfPlaceExistsUseCase useCase;
    Disposable useCaseDisposable;
    MainNavigator navigator;

    // endregion

    // region Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        useCase = new CheckIfPlaceExistsUseCase(new PlaceSource());
        navigator = new MainNavigator(this);

        lokkingButton = findViewById(R.id.activity_main_looking);
        wentButton = findViewById(R.id.activity_main_went);

        lokkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.goToPlacePicker(PLACE_PICKER_CODE);
            }
        });

        wentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.goToPlacePicker(PLACE_PICKER_CODE);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_CODE) {
            if (resultCode == RESULT_OK) {
                final Place place = PlacePicker.getPlace(this, data);

                useCaseDisposable = useCase.execute(place.getId())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean isPresent) {
                                if (!isPresent) {
                                    navigator.goToInfoActivity(place);
                                } else {
                                    navigator.goToInfoAvailableActivity(place);

                                }
                            }
                        });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (useCaseDisposable != null && !useCaseDisposable.isDisposed()) {
            useCaseDisposable.dispose();
        }

        navigator = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                navigator.goToAboutActivity();
                return (true);
            case R.id.faq:
                navigator.goToFaqActivity();
                return (true);
            case R.id.contact:
                navigator.goToContactActivity();
                return (true);

        }

        return (super.onOptionsItemSelected(item));
    }

    // endregion
}
