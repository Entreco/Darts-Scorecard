package nl.entreco.dartsscorecard.profile.select

import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.View
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.SwipeToDeleteCallback


class SelectProfileSwiper(view: View, onSwiped: (Int) -> Unit, deleteAction: () -> Unit, undoAction: () -> Unit) : ItemTouchHelper(object : SwipeToDeleteCallback(view.context) {
    override fun onSwiped(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, p1: Int) {
        onSwiped(viewHolder.adapterPosition)

        val snack = Snackbar.make(view, R.string.confirm_delete_profile, Snackbar.LENGTH_LONG)
        snack.setAction(R.string.undo) {}
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

    override fun onMove(p0: androidx.recyclerview.widget.RecyclerView, p1: androidx.recyclerview.widget.RecyclerView.ViewHolder, p2: androidx.recyclerview.widget.RecyclerView.ViewHolder): Boolean {
        return false
    }
})