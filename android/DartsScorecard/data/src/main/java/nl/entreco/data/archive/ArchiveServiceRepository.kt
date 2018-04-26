package nl.entreco.data.archive

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import nl.entreco.domain.repository.ArchiveRepository


class ArchiveServiceRepository(private val context: Context) : ArchiveRepository {

    override fun archive(gameId: Long): Int {
        return startService()
    }

    private fun startService(): Int {
        val job = JobInfo.Builder(180, ComponentName(context, ArchiveJobService::class.java))
                .setOverrideDeadline(0)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                .setRequiresCharging(false)
                .build()
        val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        return scheduler.schedule(job)
    }
}