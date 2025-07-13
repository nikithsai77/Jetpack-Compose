package com.android.compose.jobScheduler

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Bundle
import android.widget.Toast
import androidx.compose.ui.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import com.android.compose.compose.MButton

class JobSchedulerActivity: ComponentActivity() {
    private lateinit var jobScheduler: JobScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        setContent {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                MButton(name = "Start Service") {
                    startJob()
                }

                MButton(name = "Stop Service") {
                    jobScheduler.cancel(100)
                }
            }
        }
    }

    private fun startJob() {
        val componentName = ComponentName(this, MyJobService::class.java)
        val jobInfo = JobInfo.Builder(100,  componentName)
            .setPeriodic(15*60*1000)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
            .setRequiresCharging(false)
            .setPersisted(true)
            .build()
        if (jobScheduler.schedule(jobInfo) == JobScheduler.RESULT_SUCCESS) Toast.makeText(this@JobSchedulerActivity,"Job Scheduled", Toast.LENGTH_SHORT).show()
        else Toast.makeText(this@JobSchedulerActivity,"Job Not Scheduled.", Toast.LENGTH_SHORT).show()
    }

}