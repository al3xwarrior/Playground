package com.al3x.housing2.Utils
import com.google.gson.*
import java.lang.reflect.Type
import java.time.Instant
import java.time.format.DateTimeFormatter

class InstantTypeAdapter : JsonSerializer<Instant>, JsonDeserializer<Instant> {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT

    override fun serialize(src: Instant?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.let { formatter.format(it) })
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Instant? {
        return json?.asString?.let { Instant.from(formatter.parse(it)) }
    }
}
