package nl.entreco.dartsscorecard.profile.select

import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.SwipeToDeleteCallback


class SelectProfileSwiper(view: View, onSwiped: (Int) -> Unit, deleteAction: () -> Unit, undoAction: () -> Unit) : ItemTouchHelper(object : SwipeToDeleteCallback(view.context) {
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwiped(viewHolder.adapterPosition)

        val snack = Snackbar.make(view, R.string.confirm_delete_profile, Snackbar.LENGTH_LONG)
        snack.setAction(R.string.undo, {})
        snack.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                    deleteAction()
                } else {
                    undoAction()
                }
                super.onDismissed(transientBottomBar, event)
            }
        })
        snack.show()
    }
})