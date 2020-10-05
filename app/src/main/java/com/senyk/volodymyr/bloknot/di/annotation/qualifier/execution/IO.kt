package com.senyk.volodymyr.bloknot.di.annotation.qualifier.execution

import javax.inject.Qualifier

@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER
)
@Qualifier
@MustBeDocumented
@Retention
annotation class IO
