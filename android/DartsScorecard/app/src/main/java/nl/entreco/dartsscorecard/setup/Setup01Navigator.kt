package nl.entreco.dartsscorecard.setup

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.support.v4.view.PagerAdapter.POSITION_NONE
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.dartsscorecard.setup.edit.EditPlayerActivity
import nl.entreco.dartsscorecard.setup.players.PlayerEditor
import nl.entreco.dartsscorecard.setup.players.PlayerViewModel
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.setup.game.CreateGameResponse

/**
 * Created by Entreco on 02/01/2018.
 */
class Setup01Navigator(private val activity: Setup01Activity) : PlayerEditor {

    fun launch(req: CreateGameResponse) {
        Play01Activity.startGame(activity, req)
        activity.finish()
    }

    override fun onEditPlayer(position: Int, player: PlayerViewModel) {
        editPlayerRequest(activity, player.name.get(), player.teamIndex.get(), position)
    }

    override fun onAddNewPlayer(index: Int) {
        val vm = PlayerViewModel(index + 1)
        editPlayerRequest(activity, vm.name.get(), vm.teamIndex.get(), POSITION_NONE)
    }

    private fun editPlayerRequest(activity: Activity, suggestion: CharSequence, teamIndex: Int, positionInList: Int) {
        val request = Intent(activity, EditPlayerActivity::class.java)
        request.putExtra(EXTRA_SUGGESTION, suggestion)
        request.putExtra(EXTRA_TEAM_INDEX, teamIndex)
        request.putExtra(EXTRA_POSITION_IN_LIST, positionInList)
        activity.startActivityForResult(request, REQUEST_CODE)
    }

    override fun handleResult(requestCode: Int, resultCode: Int, data: Intent?, callback: PlayerEditor.Callback) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            val suggestion = data.getStringExtra(EXTRA_SUGGESTION)
            val playerName = data.getStringExtra(EXTRA_PLAYER_NAME)
            val teamIndex = data.getIntExtra(EXTRA_TEAM_INDEX, POSITION_NONE)
            val index = data.getIntExtra(EXTRA_POSITION_IN_LIST, POSITION_NONE)

            if (isNewPlayer(index)) {

                if(suggestion.isEmpty()){
                    callback.onPlayerAdded(playerName)
                } else if(suggestion == "Player 1"){
                    callback.onPlayerAdded(suggestion)
                }

            } else {
                callback.onPlayerEdited(index, teamIndex, playerName)
            }
        }
    }

    private fun isNewPlayer(index: Int) = index == POSITION_NONE

    companion object {

        internal const val REQUEST_CODE = 1002
        internal const val EXTRA_SUGGESTION = "suggestion"
        internal const val EXTRA_TEAM_INDEX = "teamIndex"
        internal const val EXTRA_POSITION_IN_LIST = "positionInList"
        internal const val EXTRA_PLAYER_NAME = "playerName"

        @JvmStatic
        fun editPlayerResponse(player: Player, request: Intent): Intent {
            val response = Intent()
            response.putExtra(EXTRA_SUGGESTION, "")
            response.putExtra(EXTRA_PLAYER_NAME, player.name)
            response.putExtra(EXTRA_TEAM_INDEX, request.getIntExtra(EXTRA_TEAM_INDEX, POSITION_NONE))
            response.putExtra(EXTRA_POSITION_IN_LIST, request.getIntExtra(EXTRA_POSITION_IN_LIST, POSITION_NONE))
            return response
        }

        @JvmStatic
        fun cancelPlayerResponse(request: Intent): Intent {
            val response = Intent()
            response.putExtra(EXTRA_SUGGESTION, request.getStringExtra(EXTRA_SUGGESTION))
            response.putExtra(EXTRA_PLAYER_NAME, "")
            response.putExtra(EXTRA_TEAM_INDEX, request.getIntExtra(EXTRA_TEAM_INDEX, POSITION_NONE))
            response.putExtra(EXTRA_POSITION_IN_LIST, request.getIntExtra(EXTRA_POSITION_IN_LIST, POSITION_NONE))
            return response
        }
    }
}