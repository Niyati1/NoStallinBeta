package nostallin.com.nostallinbeta.ui.placeinfo

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_place_info.*
import nostallin.com.nostallinbeta.R
import nostallin.com.nostallinbeta.model.BathroomInfo
import nostallin.com.nostallinbeta.model.DoorCount
import nostallin.com.nostallinbeta.model.PlaceStub
import nostallin.com.nostallinbeta.model.StallCount
import nostallin.com.nostallinbeta.ui.ProgressDialogFragment

class PlaceInfoSurveyActivity : AppCompatActivity() {

    companion object {
        private const val KEY_PLACE = "placeId"
        private const val TAG_DIALOG = "progressDialog"

        @JvmStatic
        fun startActivity(context: Context, placeId:PlaceStub) {
            val intent = Intent(context, PlaceInfoSurveyActivity::class.java)

            intent.putExtra(KEY_PLACE, placeId)

            context.startActivity(intent)
        }
    }

    private val placeId: PlaceStub
        get() = intent.getParcelableExtra(KEY_PLACE)

    private var stallCount: StallCount = StallCount.NONE
    private var doorCount: DoorCount = DoorCount.NONE
    private var genderNeutral: Boolean = false
    private var placeHits = 0

    private lateinit var placeViewModel: PlaceInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_info)

        stallCountRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.atLeastOneStall -> stallCount = StallCount.AT_LEAST_1
                R.id.moreThanOneStall -> stallCount = StallCount.MORE_THAN_ONE
                R.id.noStalls -> stallCount = StallCount.NONE
            }
        }

        doorCountRadioGroup.setOnCheckedChangeListener{ _, checkedId ->
            when(checkedId) {
                R.id.atLeastOneDoor -> doorCount = DoorCount.AT_LEAST_1
                R.id.moreThanOneDoor -> doorCount = DoorCount.SOME
                R.id.allDoors -> doorCount = DoorCount.ALL
                R.id.noDoors -> doorCount = DoorCount.ALL
            }
        }

        genderNeutralGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.genderNeutralYes -> genderNeutral = true
                R.id.genderNeutralNo -> genderNeutral = false
            }
        }

        placeViewModel = ViewModelProviders.of(this).get(PlaceInfoViewModel::class.java)
        placeViewModel.placeLivedata.observe(this, Observer<PlaceInfoViewModel.BathroomInfoResult> { result ->
            dismissDialog()
            result?.let {
                when (it) {
                    is PlaceInfoViewModel.BathroomInfoResult.Info -> {
                        populateWithBathroomInfo(it.result)
                    }

                    is PlaceInfoViewModel.BathroomInfoResult.UploadSuccess -> {
                        Toast.makeText(this@PlaceInfoSurveyActivity, R.string.PlaceInfo_Success, Toast.LENGTH_LONG).show()
                        finish()
                    }

                    is PlaceInfoViewModel.BathroomInfoResult.UpdateSuccess -> {
                        Toast.makeText(this@PlaceInfoSurveyActivity, R.string.PlaceInfo_Success, Toast.LENGTH_LONG).show()
                        finish()
                    }
                    is PlaceInfoViewModel.BathroomInfoResult.Error -> {
                        Toast.makeText(this@PlaceInfoSurveyActivity, R.string.PlaceInfo_Success, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

        submitButton.setOnClickListener { view ->
            if (isFloorNumberValid(floorNumberEditText.text.toString())) {
                val progressDialogFragment = ProgressDialogFragment.newInstance()
                progressDialogFragment.show(supportFragmentManager, TAG_DIALOG)
                val info = generateBathroomInfo()

                if (placeHits == 0) {
                    placeViewModel.uploadBathroomInfo(info)
                } else {
                    placeViewModel.updateBathroomInfo(info)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        placeViewModel.fetchBathroomInfo(placeId.id)
    }

    private fun generateBathroomInfo() : BathroomInfo {
        val floorNumber = floorNumberEditText.text?.toString()?.toInt() ?: 1

        return BathroomInfo(
                id = placeId.id,
                numStalls = stallCount,
                name = placeId.name,
                address = placeId.address,
                floorNumber = floorNumber,
                totalHits = ++placeHits,
                doorCount = doorCount,
                isGenderNeutral = genderNeutral
        )
    }

    private fun populateWithBathroomInfo(info: BathroomInfo) {
        when (info.numStalls) {
            StallCount.AT_LEAST_1 -> atLeastOneStall.isChecked = true
            StallCount.MORE_THAN_ONE -> moreThanOneStall.isChecked = true
            StallCount.NONE -> noStalls.isChecked = true
        }

        when (info.doorCount) {
            DoorCount.AT_LEAST_1 -> atLeastOneDoor.isChecked = true
            DoorCount.SOME -> moreThanOneDoor.isChecked = true
            DoorCount.ALL -> allDoors.isChecked = true
            DoorCount.NONE -> noDoors.isChecked = true
        }

        if (info.isGenderNeutral) {
            genderNeutralYes.isChecked = true
        } else {
            genderNeutralNo.isChecked = true
        }

        placeHits = info.totalHits

        floorNumberEditText.setText(info.floorNumber.toString())

        // TODO use place location to generate static map
    }

    private fun dismissDialog() {
        val progressDialog = supportFragmentManager.findFragmentByTag(TAG_DIALOG) as ProgressDialogFragment?
        progressDialog?.dismiss()
    }

    private fun isFloorNumberValid(floorNumber: String?) : Boolean = !TextUtils.isEmpty(floorNumber)
}