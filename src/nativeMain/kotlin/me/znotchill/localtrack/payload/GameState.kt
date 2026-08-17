package me.znotchill.localtrack.payload;

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = GameStateSerializer::class)
enum class GameState(val number: Int) {
    MENU(0),
    EDIT(1),
    PLAY(2),
    SELECT_PLAY(5),
    RESULT_SCREEN(7),
    LOBBY(11),
    SELECT_MULTI(13),
    UNKNOWN(-1);
 
    companion object {
        fun fromNumber(number: Int): GameState =
            entries.find { it.number == number } ?: UNKNOWN
    }
}

object GameStateSerializer : KSerializer<GameState> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("GameState") {
            element<Int>("number")
            element<String>("name")
        }

    override fun deserialize(decoder: Decoder): GameState {
        val jsonDecoder = decoder as? JsonDecoder
            ?: error("GameStateSerializer only supports JSON")
        val element = jsonDecoder.decodeJsonElement()
        val number = element.jsonObject["number"]?.jsonPrimitive?.int
            ?: error("Missing 'number' field in state object")
        return GameState.fromNumber(number)
    }

    override fun serialize(encoder: Encoder, value: GameState) {
        encoder.encodeStructure(descriptor) {
            encodeIntElement(descriptor, 0, value.number)
            encodeStringElement(descriptor, 1, value.name)
        }
    }
}