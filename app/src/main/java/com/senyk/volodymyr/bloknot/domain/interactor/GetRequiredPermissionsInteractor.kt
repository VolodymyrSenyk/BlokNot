package com.senyk.volodymyr.bloknot.domain.interactor

import io.reactivex.Single
import javax.inject.Inject

class GetRequiredPermissionsInteractor @Inject constructor() {

    operator fun invoke(): Single<List<String>> =
        Single.fromCallable {
            val permissions = mutableListOf<String>()
            permissions.add(WRITE_EXTERNAL_STORAGE)
            permissions.add(READ_EXTERNAL_STORAGE)
            permissions.toList()
        }

    companion object {
        private const val WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE"
        private const val READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE"
    }
}
