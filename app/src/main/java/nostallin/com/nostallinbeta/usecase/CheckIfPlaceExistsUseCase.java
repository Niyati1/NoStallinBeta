package nostallin.com.nostallinbeta.usecase;

import com.google.android.gms.location.places.Place;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import nostallin.com.nostallinbeta.source.PlaceSource;

/**
 * Class responsible for using the {@link PlaceSource} to retrieve a place and check
 * if that place exists
 */
public class CheckIfPlaceExistsUseCase {

    private PlaceSource source;

    public CheckIfPlaceExistsUseCase(PlaceSource source) {
        this.source = source;
    }

    public Observable<Boolean> execute(final String id) {
        return source.get(id)
                .map(new Function<Place, Boolean>() {
                    @Override
                    public Boolean apply(Place place) {
                        return place.getId().equalsIgnoreCase(id);
                    }
                });
    }
}
