package com.example.db;

import io.micronaut.context.annotation.Factory;
import io.micronaut.core.convert.TypeConverter;
import jakarta.inject.Singleton;

import java.util.Optional;
import java.util.UUID;

@Factory
public class TypeConvertersFactory {

    @Singleton
    public TypeConverter<OptimizedUUID, byte[]> optimizedUuidByteArrayTypeConverter() {
        return (uuidId, targetType, context) -> Optional.of(UUIDByteArrayConverter.uuidToByteArray(uuidId.getUuid()));
    }

    @Singleton
    public TypeConverter<byte[], OptimizedUUID> byteArrayOptimizedUuidTypeConverter() {
        return (byteArrayId, targetType, context) -> Optional.of(new OptimizedUUID(UUIDByteArrayConverter.byteArrayToUUID(byteArrayId)));
    }

    @Singleton
    public TypeConverter<UUID, byte[]> uuidByteArrayTypeConverter() {
        return (uuid, targetType, context) -> Optional.of(UUIDByteArrayConverter.uuidToByteArray(uuid));
    }

    @Singleton
    public TypeConverter<byte[], UUID> byteArrayUuidTypeConverter() {
        return (byteArrayId, targetType, context) -> Optional.of(UUIDByteArrayConverter.byteArrayToUUID(byteArrayId));
    }
}
