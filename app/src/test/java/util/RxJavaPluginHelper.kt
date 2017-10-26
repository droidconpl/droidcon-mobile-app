package util

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

object RxJavaPluginHelper {

    fun setup(scheduler: Scheduler = Schedulers.trampoline()) {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> scheduler }
        RxJavaPlugins.setComputationSchedulerHandler { scheduler }
        RxJavaPlugins.setIoSchedulerHandler { scheduler }
        RxJavaPlugins.setNewThreadSchedulerHandler { scheduler }
        RxJavaPlugins.setSingleSchedulerHandler { scheduler }
    }

    fun teardown() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }
}