package com.senyk.volodymyr.bloknot.domain.util

import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class Aes256 @Inject constructor() {

    fun encrypt(data: String, password: String): String {
        val iv = ByteArray(IV_ARRAY_SIZE) { IV_ARRAY_VALUE }
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(KEY_FACTORY_ALGORITHM_NAME)
        val spec = PBEKeySpec(
            password.toCharArray(),
            SALT.toByteArray(),
            ITERATION_COUNT,
            KEY_LENGTH
        )
        val tmp: SecretKey = factory.generateSecret(spec)
        val secretKey = SecretKeySpec(tmp.encoded, ALGORITHM_NAME)
        val cipher: Cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))
        return Base64.getEncoder()
            .encodeToString(cipher.doFinal(data.toByteArray(charset(CHARSET))))
    }

    fun decrypt(data: String, password: String): String {
        val iv = ByteArray(IV_ARRAY_SIZE) { IV_ARRAY_VALUE }
        val factory = SecretKeyFactory.getInstance(KEY_FACTORY_ALGORITHM_NAME)
        val spec = PBEKeySpec(
            password.toCharArray(),
            SALT.toByteArray(),
            ITERATION_COUNT,
            KEY_LENGTH
        )
        val secretKey = SecretKeySpec(factory.generateSecret(spec).encoded, ALGORITHM_NAME)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        return String(cipher.doFinal(Base64.getDecoder().decode(data)))
    }

    companion object {
        private const val ALGORITHM_NAME = "AES"
        private const val IV_ARRAY_SIZE = 16
        private const val IV_ARRAY_VALUE: Byte = 0
        private const val KEY_FACTORY_ALGORITHM_NAME = "PBKDF2WithHmacSHA256"
        private const val ITERATION_COUNT = 65536
        private const val KEY_LENGTH = 256
        private const val TRANSFORMATION = "AES/CBC/PKCS5PADDING"
        private const val CHARSET = "UTF-8"
    }
}
