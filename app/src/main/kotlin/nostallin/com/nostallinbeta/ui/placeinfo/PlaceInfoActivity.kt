package nostallin.com.nostallinbeta.ui.placeinfo

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_place_info.*
import nostallin.com.nostallinbeta.R
import nostallin.com.nostallinbeta.model.BathroomInfo
import nostallin.com.nostallinbeta.model.DoorCount
import nostallin.com.nostallinbeta.model.PlaceStub
import nostallin.com.nostallinbeta.model.StallCount
import nostallin.com.nostallinbeta.ui.ProgressDialogFragment

class PlaceInfoActivity : AppCompatActivity() {

    companion object {
        private const val KEY_PLACE = "place"
        private const val TAG_DIALOG = "dialogFragment"

        @JvmStatic
        fun startActivity(context: Context, placeStub: PlaceStub) {
            val intent = Intent(context, PlaceInfoActivity::class.java)
            intent.putExtra(KEY_PLACE, placeStub)
            context.startActivity(intent)
        }
    }

    private val placeStub: PlaceStub
        get() = intent.getParcelableExtra(KEY_PLACE)

    private lateinit var placeViewModel: PlaceInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_info)

        placeViewModel = ViewModelProviders.of(this).get(PlaceInfoViewModel::class.java)

        placeViewModel.placeLivedata.observe(this, Observer<PlaceInfoViewModel.BathroomInfoResult> { result ->
            dismissDialog()
            when (result) {
                is PlaceInfoViewModel.BathroomInfoResult.Info -> {
                    populateWithInfo(result.result)
                    placeInfoFlipper.displayedChild = 0
                }

                is PlaceInfoViewModel.BathroomInfoResult.Error -> {
                    placeInfoFlipper.displayedChild = 1
                }

                else -> finish()
            }

        })

        initButtons()

        title = placeStub.name
    }

    override fun onStart() {
        super.onStart()

        placeViewModel.fetchBathroomInfo(placeStub.id)
    }

    private fun initButtons() {
        placeInfoCorrect.setOnClickListener { view ->
            val currentResult = (placeViewModel.placeLivedata.value!! as PlaceInfoViewModel.BathroomInfoResult.Info).result
            val totalHits = currentResult.totalHits + 1
            val newInfo = currentResult.copy(
                    totalHits = totalHits
            )

            placeViewModel.updateBathroomInfo(newInfo)

            val progressDialog = ProgressDialogFragment.newInstance()
            progressDialog.show(supportFragmentManager, TAG_DIALOG)
        }

        placeInfoIncorrect.setOnClickListener { view ->
            PlaceInfoSurveyActivity.startActivity(view.context, placeStub)
        }

        placeInfoAdd.setOnClickListener { view ->
            PlaceInfoSurveyActivity.startActivity(view.context, placeStub)
        }
    }

    private fun dismissDialog() {
        val dialog = supportFragmentManager.findFragmentByTag(TAG_DIALOG) as ProgressDialogFragment?
        dialog?.dismiss()
    }

    private fun populateWithInfo(bathroomInfo: BathroomInfo) {
        placeInfoAddress.text = bathroomInfo.address

        when (bathroomInfo.numStalls) {
            StallCount.AT_LEAST_1 -> placeInfoNumStalls.text = getString(R.string.PlaceInfo_Radio_HowMany_AtLeastOne)
            StallCount.MORE_THAN_ONE -> placeInfoNumStalls.text = getString(R.string.PlaceInfo_Radio_HowMany_MoreThanOne)
            StallCount.NONE -> placeInfoNumStalls.text = getString(R.string.PlaceInfo_Radio_HowMany_None)
        }

        when (bathroomInfo.doorCount) {
            DoorCount.AT_LEAST_1 -> placeInfoNumDoors.text = getString(R.string.PlaceInfo_Radio_Doors_AtLeastOne)
            DoorCount.ALL -> placeInfoNumDoors.text = getString(R.string.PlaceInfo_Radio_Doors_All)
            DoorCount.SOME -> placeInfoNumDoors.text = getString(R.string.PlaceInfo_Radio_Doors_Some)
            DoorCount.NONE -> placeInfoNumDoors.text = getString(R.string.PlaceInfo_Radio_Doors_None)
        }

        if (bathroomInfo.isGenderNeutral) {
            placeInfoGenderNeutral.text = getString(R.string.PlaceInfo_Radio_Yes)
        } else {
            placeInfoGenderNeutral.text = getString(R.string.PlaceInfo_Radio_No)
        }
    }
}