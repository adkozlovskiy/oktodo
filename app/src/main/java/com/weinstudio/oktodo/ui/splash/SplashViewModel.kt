package com.weinstudio.oktodo.ui.splash

import androidx.lifecycle.ViewModel
import com.weinstudio.oktodo.util.WorkerEnquirer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val workerEnquirer: WorkerEnquirer,
) : ViewModel() {

    fun enqueueDailyNotifications() {
        workerEnquirer.enqueueDailyNotifications()
    }

    fun enqueuePeriodicallySync() {
        workerEnquirer.enqueuePeriodicallySync()
    }
}