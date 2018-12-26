package nostallin.com.nostallinbeta.source

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Observable
import nostallin.com.nostallinbeta.model.BathroomInfo
import nostallin.com.nostallinbeta.util.getBathroomInfo
import nostallin.com.nostallinbeta.util.sendBathroomInfo
import nostallin.com.nostallinbeta.util.updateBathroomInfo

class BathroomInfoSource {

    private val firestore = FirebaseFirestore.getInstance()

    fun getPlaceById(id: String) : Observable<BathroomInfo> {
        return firestore.getBathroomInfo(id)
    }

    fun uploadBathroomInfo(bathroomInfo: BathroomInfo) : Completable {
        return firestore.sendBathroomInfo(bathroomInfo)
    }

    fun updateBathroomInfo(bathroomInfo: BathroomInfo) : Completable {
        return firestore.updateBathroomInfo(bathroomInfo)
    }
}