package com.senyk.volodymyr.bloknot.domain.util

import javax.inject.Inject

class CheckSumCalculator @Inject constructor(
    private val aes: Aes256
) {

    operator fun invoke(data: String): String =
        aes.encrypt(
            data = data,
            password = data.hashCode().toString()
        )
}
