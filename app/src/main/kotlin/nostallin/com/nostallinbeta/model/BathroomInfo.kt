package nostallin.com.nostallinbeta.model

data class BathroomInfo (
        val id: String,
        val numStalls: StallCount,
        val name: String,
        val address: String,
        val floorNumber: Int,
        val totalHits: Int,
        val doorCount: DoorCount,
        val isGenderNeutral: Boolean
) : FirestoreModel {

    companion object {

        fun fromMap(map: Map<String, Any>) : BathroomInfo {
            val id = map["id"] as String
            val numStalls = StallCount.getFromValue((map["numStalls"] as Long).toInt())
            val name = map["name"] as String
            val address = map["address"] as String
            val floorNumber = (map["floor"] as Long).toInt()
            val totalHits = (map["totalHits"] as Long).toInt()
            val doorCount = DoorCount.getFromCount((map["doorCount"] as Long).toInt())
            val genderNeutral = map["genderNeutral"] as Boolean

            return BathroomInfo(
                    id = id,
                    numStalls = numStalls,
                    name = name,
                    address = address,
                    floorNumber = floorNumber,
                    totalHits = totalHits,
                    doorCount = doorCount,
                    isGenderNeutral = genderNeutral
            )
        }
    }

    override fun toMap(): Map<String, Any> {
        return mapOf("id" to id,
                "numStalls" to numStalls.count,
                "name" to name,
                "address" to address,
                "floor" to floorNumber,
                "totalHits" to totalHits,
                "doorCount" to doorCount.count,
                "genderNeutral" to isGenderNeutral)
    }

}