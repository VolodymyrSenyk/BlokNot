package com.senyk.volodymyr.bloknot.domain.util

import com.senyk.volodymyr.bloknot.domain.exception.WrongPasswordException
import java.security.GeneralSecurityException
import javax.inject.Inject

class Aes256EncoderDecoder @Inject constructor(
    private val aes256: Aes256
) {

    fun encode(data: String, password: String = DEFAULT_PASSWORD): String =
        if (password.isEmpty()) {
            aes256.encrypt(data = addCheckSum(data = data), password = DEFAULT_PASSWORD)
        } else {
            aes256.encrypt(data = addCheckSum(data = data), password = password)
        }

    fun decode(data: String, password: String = DEFAULT_PASSWORD): String {
        try {
            val decryptedData = if (password.isEmpty()) {
                aes256.decrypt(data = data, password = DEFAULT_PASSWORD)
            } else {
                aes256.decrypt(data = data, password = password)
            }

            if (checkCheckSum(decryptedData = decryptedData)) {
                return decryptedData.substring(
                    startIndex = 0,
                    endIndex = decryptedData.length - CHECK_SUM.length
                )
            } else {
                throw WrongPasswordException(password = password)
            }
        } catch (exception: GeneralSecurityException) {
            throw WrongPasswordException(password = password)
        }
    }

    private fun addCheckSum(data: String): String = data + CHECK_SUM

    private fun checkCheckSum(decryptedData: String): Boolean =
        decryptedData.substring(
            startIndex = decryptedData.length - CHECK_SUM.length,
            endIndex = decryptedData.length
        ) == CHECK_SUM
}
