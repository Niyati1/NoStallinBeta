package nostallin.com.nostallinbeta.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceStub (
        val id: String,
        val address: String,
        val name: String
) : Parcelable