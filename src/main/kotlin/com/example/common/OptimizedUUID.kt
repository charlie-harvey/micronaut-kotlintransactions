package com.example.common

import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.model.DataType
import java.util.*

@TypeDef(type = DataType.BYTE_ARRAY)
data class OptimizedUUID(val uuid: UUID)
