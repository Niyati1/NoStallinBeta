package nostallin.com.nostallinbeta.source;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.places.Place;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Retrieves a {@link Place} from the Firebase db
 */
public class PlaceSource {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRef = database.getReference().child("locations");

    private PublishSubject<Boolean> placeSubject = PublishSubject.create();

    public Observable<Boolean> get(final String id) {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean wasFound = false;
                for (DataSnapshot obj : dataSnapshot.getChildren()) {
                    if (obj.getKey() != null && obj.getKey().equalsIgnoreCase(id)) {

                        wasFound = true;
                    }
                }

                placeSubject.onNext(wasFound);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PLACE_SOURCE", databaseError.getDetails());
            }
        });

        return placeSubject;
    }
}
