package nostallin.com.nostallinbeta.util

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    this.add(disposable)
}

fun <T> Observable<T>.ioTo(scheduler: Scheduler) : Observable<T> {
    return this.subscribeOn(Schedulers.io())
            .observeOn(scheduler)
}

fun <T> Observable<T>.ioToMain() : Observable<T> = this.ioTo(AndroidSchedulers.mainThread())

fun Completable.ioTo(scheduler: Scheduler) : Completable {
    return this.subscribeOn(Schedulers.io())
            .observeOn(scheduler)
}

fun Completable.ioToMain() : Completable = this.ioTo(AndroidSchedulers.mainThread())