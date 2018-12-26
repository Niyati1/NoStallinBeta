package nostallin.com.nostallinbeta.util

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Observable
import nostallin.com.nostallinbeta.model.BathroomInfo

fun FirebaseFirestore.getBathroomInfo(id: String) : Observable<BathroomInfo> {
    return Observable.create { emitter ->
        this.collection("locations")
                .document(id)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        documentSnapshot.data?.let { data ->
                            val bathroomInfo = BathroomInfo.fromMap(data.toMap())
                            emitter.onNext(bathroomInfo)
                        } ?: emitter.onError(SourceException(SourceException.SourceExceptionCode.DOES_NOT_EXIST))
                    } else {
                        emitter.onError(SourceException(SourceException.SourceExceptionCode.DOES_NOT_EXIST))
                    }
                }
                .addOnFailureListener {
                    emitter.onError(SourceException(SourceException.SourceExceptionCode.API_ERROR))
                }
    }
}

fun FirebaseFirestore.sendBathroomInfo(bathroomInfo: BathroomInfo) : Completable {
    return Completable.create { emitter ->
        this.collection("locations")
                .document(bathroomInfo.id)
                .set(bathroomInfo.toMap())
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
    }
}

fun FirebaseFirestore.updateBathroomInfo(bathroomInfo: BathroomInfo) : Completable {
    return Completable.create { emitter ->
        this.collection("locations")
                .document(bathroomInfo.id)
                .update(bathroomInfo.toMap())
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
    }
}