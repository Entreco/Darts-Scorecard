package nl.entreco.dartsscorecard.profile.edit

import android.transition.TransitionInflater
import android.view.View
import android.view.Window
import nl.entreco.dartsscorecard.base.RevealAnimator
import nl.entreco.dartsscorecard.databinding.ActivityEditPlayerNameBinding

/**
 * Created by entreco on 02/03/2018.
 */
class EditPlayerNameAnimator(binding: ActivityEditPlayerNameBinding, inflater: TransitionInflater, window: Window) {

    private val editArea: View = binding.editArea
    private val revealAnimator = RevealAnimator(editArea)

    init {
        revealAnimator.setupEnterAnimation(inflater, window, false)
    }
}
