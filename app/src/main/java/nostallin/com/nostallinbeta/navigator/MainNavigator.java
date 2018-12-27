package nostallin.com.nostallinbeta.navigator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import nostallin.com.nostallinbeta.model.PlaceStub;
import nostallin.com.nostallinbeta.ui.activities.AboutActivity;
import nostallin.com.nostallinbeta.ui.activities.ContactActivity;
import nostallin.com.nostallinbeta.ui.activities.FaqActivity;
import nostallin.com.nostallinbeta.ui.activities.MainActivity;
import nostallin.com.nostallinbeta.ui.placeinfo.PlaceInfoActivity;
import nostallin.com.nostallinbeta.ui.placeinfo.PlaceInfoSurveyActivity;

/**
 * Class that handles navagation from the {@link MainActivity}
 */
public class MainNavigator {

    private static final String KEY_LAT_LNG = "LatLng";
    private static final String KEY_NAME = "Name";
    private static final String KEY_ID = "Id";
    private static final String KEY_ADDR = "Addr";

    private Context context;

    public MainNavigator(Context context) {
        this.context = context;
    }

    public void goToInfoActivity(Place place) {
        PlaceStub stub = new PlaceStub(place.getId(), place.getAddress().toString(), place.getName().toString());

        PlaceInfoActivity.startActivity(context, stub);
    }

    public void goToInfoAvailableActivity(Place place) {

        PlaceStub stub = new PlaceStub(place.getId(), place.getAddress().toString(), place.getName().toString());

        PlaceInfoSurveyActivity.startActivity(context, stub);
    }

    public void goToAboutActivity() {
        Intent about = new Intent(context, AboutActivity.class);
        context.startActivity(about);
    }

    public void goToFaqActivity() {
        Intent faqIntent = new Intent(context, FaqActivity.class);
        context.startActivity(faqIntent);
    }

    public void goToContactActivity() {
        Intent contactIntent = new Intent(context, ContactActivity.class);
        context.startActivity(contactIntent);
    }

    public void goToPlacePicker(int code) {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();

        try {
            Intent intent = intentBuilder.build((MainActivity) context);
            ((MainActivity) context).startActivityForResult(intent, code);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private Intent generateIntent(Place place, Class<? extends AppCompatActivity> clazz) {
        Intent infoIntent = new Intent(context, clazz);
        infoIntent.putExtra(KEY_LAT_LNG, place.getLatLng());
        infoIntent.putExtra(KEY_NAME, place.getName());
        infoIntent.putExtra(KEY_ID, place.getId());
        infoIntent.putExtra(KEY_ADDR, place.getAddress());

        return infoIntent;
    }
}
