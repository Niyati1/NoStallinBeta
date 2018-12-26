package nostallin.com.nostallinbeta.ui

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import nostallin.com.nostallinbeta.R

class ProgressDialogFragment : DialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = ProgressDialogFragment()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return with (AlertDialog.Builder(activity!!)) {
            setView(R.layout.dialog_progress)
            this.create()
        }
    }
}