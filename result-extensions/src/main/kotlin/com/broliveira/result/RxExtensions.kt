package com.broliveira.result

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.Subject

fun <T> Observable<Result<T>>.bindSuccessTo(subject: Subject<T>): Disposable {
  return this.filterSuccess().subscribe { subject.onNext(it) }
}

fun <T> Observable<Result<T>>.filterSuccess(): Observable<T> {
  return this.flatMap {
    it.value?.let { Observable.just(it) } ?: Observable.empty<T>()
  }
}

fun <T> Observable<T>.toResult(): Observable<Result<T>> {
  return map {
    ResultSuccess(it) as Result<T>
  }.onErrorReturn {
    it.printStackTrace()
    ResultFailure(it)
  }
}

fun <T> Observable<Result<T>>.doOnSuccess(block: (T) -> Unit): Observable<Result<T>> {
  return this
      .doOnNext {
        it.value?.let(block)
      }
}

fun <T> Observable<Result<T>>.subscribeSuccess(block: (T) -> Unit): Disposable {
  return this
      .subscribe { it.value?.let(block) }
}

fun <T> Observable<Result<T>>.doOnFailure(block: (Throwable) -> Unit): Observable<Result<T>> {
  return this
      .doOnNext {
        it.error?.let(block)
      }
}

fun <T> Observable<Result<T>>.subscribeFailure(block: (Throwable) -> Unit): Disposable {
  return this
      .subscribe { it.error?.let(block) }
}
