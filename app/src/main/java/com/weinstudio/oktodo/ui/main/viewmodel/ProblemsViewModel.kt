package com.weinstudio.oktodo.ui.main.viewmodel

import androidx.lifecycle.*
import com.weinstudio.oktodo.data.ProblemsRepository
import com.weinstudio.oktodo.data.model.Hike
import com.weinstudio.oktodo.data.model.Problem
import com.weinstudio.oktodo.util.WorkerEnquirer
import com.weinstudio.oktodo.util.connectivity.base.BaseConnectivityProvider
import com.weinstudio.oktodo.util.connectivity.base.ConnectivityProvider
import com.weinstudio.oktodo.util.hasInternet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProblemsViewModel @Inject constructor(
    private val repository: ProblemsRepository,
    private val workerEnquirer: WorkerEnquirer,
    private val connectivityProvider: BaseConnectivityProvider

) : ViewModel(), ConnectivityProvider.ConnectivityStateListener {

    private val filteredFlow = MutableStateFlow(false)

    @ExperimentalCoroutinesApi
    val allProblems = filteredFlow.flatMapLatest { filtered ->
        repository.getProblemsFlow(filtered)
    }.asLiveData()

    val doneCount = repository.getCountFlow(true).asLiveData()

    fun changeDoneFlag(problem: Problem, done: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        val entryProblem = problem.copy(
            done = done
        )

        repository.updateProblem(entryProblem)
        workerEnquirer.enqueueUpdate(entryProblem)
    }

    fun deleteProblem(problem: Problem) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteProblem(problem)
        workerEnquirer.enqueueDelete(problem)
    }

    @ExperimentalCoroutinesApi
    fun setFilterFlag(flag: Boolean) {
        filteredFlow.value = flag
    }

    private val _hikeState = MutableLiveData<Hike>()
    val hikeState: LiveData<Hike> = _hikeState

    fun enqueueRefreshProblems() = viewModelScope.launch(Dispatchers.IO) {
        _hikeState.postValue(Hike.Loading)
        try {
            val hike = repository.refreshProblems()
            _hikeState.postValue(hike)

        } catch (ex: Exception) {
            ex.printStackTrace()
            _hikeState.postValue(Hike.Error(ex))
        }
    }

    private val _networkState = MutableLiveData<ConnectivityProvider.NetworkState>()

    val networkState: LiveData<ConnectivityProvider.NetworkState> = _networkState

    fun isNetworkConnectionGranted(): Boolean {
        return connectivityProvider.getNetworkState().hasInternet()
    }

    override fun onStateChange(state: ConnectivityProvider.NetworkState) {
        _networkState.postValue(state)
    }

    fun subscribeNetworkStateChanges() {
        connectivityProvider.addListener(this)
    }

    fun unsubscribeNetworkStateChanges() {
        connectivityProvider.removeListener(this)
    }
}