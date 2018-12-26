package nostallin.com.nostallinbeta.model

/**
 * Defines a contract for a model that can convert to and from a map.  This is meant to ease
 * interfacing with Firestore
 */
interface FirestoreModel {

    /**
     * Converts this object into a [Map] and populates the map with the intrinsic data
     */
    fun toMap() : Map<String, Any>
}