package com.example.common

import com.example.db.UUIDByteArrayConverter
import io.micronaut.core.convert.ConversionContext
import io.micronaut.data.model.runtime.convert.AttributeConverter
import jakarta.inject.Singleton

@Singleton
class OptimizedUUIDAttributeConverter : AttributeConverter<OptimizedUUID?, ByteArray?> {

    override fun convertToPersistedValue(optimizedUUID: OptimizedUUID?, context: ConversionContext): ByteArray? {
        return if (optimizedUUID == null) {
            null
        } else {
            UUIDByteArrayConverter.uuidToByteArray(optimizedUUID.uuid)
        }
    }

    override fun convertToEntityValue(byteArrayId: ByteArray?, context: ConversionContext): OptimizedUUID? {
        return if (byteArrayId == null) {
            null
        } else {
            OptimizedUUID(UUIDByteArrayConverter.byteArrayToUUID(byteArrayId))
        }
    }
}
