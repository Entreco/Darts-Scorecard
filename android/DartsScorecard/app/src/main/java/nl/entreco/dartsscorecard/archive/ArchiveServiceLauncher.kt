package nl.entreco.dartsscorecard.archive

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.PersistableBundle
import javax.inject.Inject


class ArchiveServiceLauncher @Inject constructor(
        private val context: Context,
        private val componentName: ComponentName,
) {

    companion object {
        internal const val EXTRA_GAME_ID = "gameId"
    }

    fun launch(gameId: Long): Int {
        val bundle = PersistableBundle(1)
        bundle.putLong(EXTRA_GAME_ID, gameId)
        return startService(bundle)
    }

    private fun startService(bundle: PersistableBundle): Int {
        val job = JobInfo.Builder(180, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                .setBackoffCriteria(10000, JobInfo.BACKOFF_POLICY_EXPONENTIAL)
                .setRequiresCharging(false)
                .setOverrideDeadline(0)
                .setExtras(bundle)
                .build()
        val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        return scheduler.schedule(job)
    }
}