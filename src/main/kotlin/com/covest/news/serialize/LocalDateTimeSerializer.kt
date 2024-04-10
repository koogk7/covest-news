package com.covest.news.serialize

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializer(forClass = LocalDateTime::class)
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    /*
        descriptor는 직렬화 대상 타입의 구조와 형식을 설명하는 메타데이터를 제공합니다.
        커스텀 LocalDateTime 직렬화기에서 descriptor는 주로 문자열 타입을 나타내는 데 사용됩니다.
        PrimitiveSerialDescriptor를 사용하여 LocalDateTime 타입이 문자열 형식으로 직렬화될 것임을 나타냅니다.
        PrimitiveSerialDescriptor의 첫 번째 인자는 해당 직렬화기의 이름으로 사용되며,
        두 번째 인자는 직렬화되는 데이터의 종류를 나타내는 PrimitiveKind입니다. 이 경우 LocalDateTime은 문자열 형태로 직렬화되므로 PrimitiveKind.STRING을 사용합니다.
        이렇게 descriptor를 정의함으로써, 직렬화 라이브러리는 이 커스텀 직렬화기가 어떤 타입의 데이터를 어떻게 처리할지에 대한 정보를 갖게 됩니다.
        이는 직렬화 및 역직렬화 과정에서 타입 안정성을 보장하고, 직렬화 체계의 일관성을 유지하는 데 도움이 됩니다.
     */
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), formatter)
    }
}
