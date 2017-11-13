package io.monkeypatch.talks.waterpouring.model

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

/** Create custom Jackson module to handle Move sealed class */
val moveModule = SimpleModule().apply {
    fun Move.toType(): String = when (this) {
        is Empty -> "Empty"
        is Fill  -> "Fill"
        is Pour  -> "Pour"
    }

    // Serializer for Move
    val moveSerializer = object : StdSerializer<Move>(Move::class.java) {
        override fun serialize(value: Move?, gen: JsonGenerator?, provider: SerializerProvider?) {
            gen?.apply {
                writeStartObject()
                if (value != null) {
                    writeStringField("type", value.toType())
                    when (value) {
                        is Empty -> writeNumberField("index", value.index)
                        is Fill  -> writeNumberField("index", value.index)
                        is Pour  -> {
                            writeNumberField("from", value.from)
                            writeNumberField("to", value.to)
                        }
                    }
                }
                writeEndObject()
            }
        }
    }

    // Deserializer for Move
    val moveDeserializer = object : StdDeserializer<Move>(Move::class.java) {
        override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Move {
            if (p != null) {
                val node = p.codec.readTree<JsonNode>(p)
                return when (node.get("type").textValue()) {
                    "Empty" -> Empty(node.get("index").intValue())
                    "Fill"  -> Fill(node.get("index").intValue())
                    "Pour"  -> Pour(from = node.get("from").intValue(),
                                      to = node.get("to").intValue())
                    else    -> throw IllegalStateException("Unknown type")
                }
            }
            throw IllegalStateException("WTF")
        }
    }
    this.addSerializer(moveSerializer)
    this.addDeserializer(Move::class.java, moveDeserializer)
}

/**
 * Configure an ObjectMapper with the moveModule
 */
val waterPouringMapper = jacksonObjectMapper()
    .registerModule(moveModule) ?: throw IllegalStateException("WTF !")
