package com.senyk.volodymyr.bloknot.presentation.view.util.extensions.rx

import io.reactivex.Observable

fun <FROM, TO> Observable<List<FROM>>.mapList(mapItem: (FROM) -> TO): Observable<List<TO>> =
    this.flatMapSingle { list ->
        Observable.fromIterable(list)
            .map(mapItem)
            .toList()
    }

fun <T> Observable<T>.bufferList(list: List<T>): Observable<List<T>> =
    if (list.isNullOrEmpty()) Observable.fromCallable { emptyList<T>() } else buffer(list.size)
