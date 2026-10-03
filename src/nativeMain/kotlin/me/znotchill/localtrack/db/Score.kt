package me.znotchill.localtrack.db

import kotlinx.serialization.Serializable
import me.znotchill.kelp.Model
import me.znotchill.kelp.Row

@Serializable
data class Score(
    val id: Long = 0,
    val beatmapId: Long,
    val beatmapChecksum: String,
    val playedAt: Long,
    val mods: List<String>,
    val modRate: Double,
    val score: Long,
    val accuracy: Double,
    val maxCombo: Long,
    val rank: String,
    val pp: Double?,
    val ppFc: Double?,
    val count300: Long,
    val count100: Long,
    val count50: Long,
    val countMiss: Long,
    val unstableRate: Double?,
    val hitErrorArray: List<Double>,
    val misses: List<Long> = emptyList(),

    val isConvert: Boolean = true,
    val mode: GameMode,
)

@Serializable
enum class GameMode {
    STANDARD,
    TAIKO,
    CATCH,
    MANIA;

    companion object {
        fun fromNumber(number: Int) = when(number) {
            0 -> STANDARD
            1 -> TAIKO
            2 -> CATCH
            3 -> MANIA
            else -> STANDARD
        }
    }
}

object ScoreModel : Model<Score>("scores") {
    val id = column("id") { it.id }
    val beatmapId = column("beatmapId") { it.beatmapId }
    val beatmapChecksum = column("beatmapChecksum") { it.beatmapChecksum }
    val playedAt = column("playedAt") { it.playedAt }
    val mods = list("mods") { it.mods }
    val modRate = column("modRate") { it.modRate }
    val score = column("score") { it.score }
    val accuracy = column("accuracy") { it.accuracy }
    val maxCombo = column("maxCombo") { it.maxCombo }
    val rank = column("rank") { it.rank }
    val pp = nullable("pp") { it.pp }
    val ppFc = nullable("ppFc") { it.ppFc }
    val count300 = column("count300") { it.count300 }
    val count100 = column("count100") { it.count100 }
    val count50 = column("count50") { it.count50 }
    val countMiss = column("countMiss") { it.countMiss }
    val unstableRate = nullable("unstableRate") { it.unstableRate }
    val hitErrorArray = list("hitErrorArray") { it.hitErrorArray }
    val misses = list("misses") { it.misses }
    val isConvert = column("isConvert") { it.isConvert }
    val mode = enum("mode", GameMode.serializer()) { it.mode }

    override fun decode(row: Row): Score {
        return Score(
            id = row[id],
            beatmapId = row[beatmapId],
            beatmapChecksum = row[beatmapChecksum],
            playedAt = row[playedAt],
            mods = row[mods],
            modRate = row[modRate],
            score = row[score],
            accuracy = row[accuracy],
            maxCombo = row[maxCombo],
            rank = row[rank],
            pp = row[pp],
            ppFc = row[ppFc],
            count300 = row[count300],
            count100 = row[count100],
            count50 = row[count50],
            countMiss = row[countMiss],
            unstableRate = row[unstableRate],
            hitErrorArray = row[hitErrorArray],
            misses = row[misses],

            isConvert = row[isConvert],
            mode = row[mode],
        )
    }
}