package nostallin.com.nostallinbeta.model

import android.util.SparseArray

enum class StallCount(val count: Int) {
    AT_LEAST_1(1),
    MORE_THAN_ONE(2),
    NONE(0);

    companion object {
        private val entryArray = SparseArray<StallCount>()

        init {
            for (value in values()) {
                entryArray.put(value.count, value)
            }
        }

        fun getFromValue(value: Int) : StallCount {
            if (entryArray[value] == null) {
                throw IllegalArgumentException("$value is not a valid StallCount value")
            } else {
                return entryArray[value]
            }
        }
    }
}