package com.broliveira.result

open class Result<out T>(val value: T? = null, val error: Throwable? = null) {
  val isSuccess: Boolean = value != null
  val isFailure: Boolean = error != null
}

class ResultSuccess<out T>(value: T): Result<T>(value, null)

class ResultFailure<out T>(error: Throwable): Result<T>(null, error)
