package nl.entreco.dartsscorecard.profile.edit

import android.app.Activity

/**
 * Created by entreco on 03/03/2018.
 */
class EditPlayerNameNavigator(private val activity: EditPlayerNameActivity) {

    fun onCancel() : Boolean{
        activity.onBackPressed()
        return true
    }

    fun onDoneEditing(desiredName: String, desiredDouble: Int) : Boolean {
        val data = EditPlayerNameActivity.result(desiredName, desiredDouble)
        activity.setResult(Activity.RESULT_OK, data)
        activity.finishAfterTransition()
        return true
    }
}
