package nl.entreco.dartsscorecard.faq

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import android.net.Uri
import android.view.View
import nl.entreco.domain.wtf.WtfItem

class WtfModel(private val item: WtfItem, private val toggler: WtfToggler, collapse: Boolean = true) {
    val title = ObservableField(item.title)
    val description = ObservableField(item.description)
    val image = ObservableField<String>(item.image)
    val showImage = ObservableInt(if(!collapse && item.image?.isNotBlank() == true) View.VISIBLE else View.GONE)
    val showVideo = ObservableInt(if(!collapse && item.video?.isNotBlank() == true) View.VISIBLE else View.GONE)
    val collapsed = ObservableBoolean(collapse)

    fun launchVideo(view: View){
        if(showVideo.get() == View.VISIBLE){
            val uri = Uri.parse(item.video)
            val id = uri.getQueryParameter( "v" )
            val app = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
            val web = Intent(Intent.ACTION_VIEW, uri)

            try {
                view.context.startActivity(app)
            } catch(youtubeNotInstalled: ActivityNotFoundException){
                view.context.startActivity(web)
            }
        }
    }

    fun toggle(){
        toggler.toggle(item)
    }
}