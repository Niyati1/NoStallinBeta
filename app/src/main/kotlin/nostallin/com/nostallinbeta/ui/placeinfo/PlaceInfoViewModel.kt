package nostallin.com.nostallinbeta.ui.placeinfo

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import nostallin.com.nostallinbeta.model.BathroomInfo
import nostallin.com.nostallinbeta.source.BathroomInfoSource
import nostallin.com.nostallinbeta.util.ioToMain
import nostallin.com.nostallinbeta.util.plusAssign

class PlaceInfoViewModel : ViewModel() {

    val placeLivedata = MutableLiveData<BathroomInfoResult>()

    private val bathroomInfoSource: BathroomInfoSource by lazy { BathroomInfoSource() }
    private val disposable = CompositeDisposable()

    fun fetchBathroomInfo(id: String) {
        disposable += bathroomInfoSource.getPlaceById(id)
                .ioToMain()
                .subscribe({ placeLivedata.value = BathroomInfoResult.Info(it)}, { placeLivedata.value = BathroomInfoResult.Error })
    }

    fun uploadBathroomInfo(bathroomInfo: BathroomInfo) {
        disposable += bathroomInfoSource.uploadBathroomInfo(bathroomInfo)
                .ioToMain()
                .subscribe({ placeLivedata.value = BathroomInfoResult.UploadSuccess }, { placeLivedata.value = BathroomInfoResult.Error })
    }

    fun updateBathroomInfo(bathroomInfo: BathroomInfo) {
        disposable += bathroomInfoSource.uploadBathroomInfo(bathroomInfo)
                .ioToMain()
                .subscribe({ placeLivedata.value = BathroomInfoResult.UpdateSuccess },
                        { placeLivedata.value = BathroomInfoResult.Error })
    }

    override fun onCleared() {
        super.onCleared()

        if (!disposable.isDisposed) {
            disposable.clear()
        }
    }

    sealed class BathroomInfoResult {

        class Info(val result: BathroomInfo) : BathroomInfoResult()

        object UploadSuccess : BathroomInfoResult()

        object UpdateSuccess : BathroomInfoResult()

        object Error : BathroomInfoResult()
    }
}