package nostallin.com.nostallinbeta.model

import android.util.SparseArray

enum class DoorCount(val count: Int) {
    AT_LEAST_1(1),
    SOME(2),
    ALL(3),
    NONE(0);

    companion object {
        private val entryArray = SparseArray<DoorCount>()

        init {
            for(value in values()) {
                entryArray.put(value.count, value)
            }
        }

        fun getFromCount(count: Int) : DoorCount {
            if (entryArray[count] == null) {
                throw IllegalArgumentException("$count is not a valid DoorCount value")
            } else {
                return entryArray[count]
            }
        }
    }
}