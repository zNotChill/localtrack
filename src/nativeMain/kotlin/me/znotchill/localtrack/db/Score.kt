package me.znotchill.localtrack.db

import kotlinx.serialization.Serializable
import me.znotchill.kelp.Model
import me.znotchill.kelp.Row
import me.znotchill.localtrack.payload.Mods

@Serializable
data class Score(
    val id: Long = 0,
    val beatmapId: Long,
    val beatmapChecksum: String,
    val playedAt: Long,
    val player: String,
    val playerId: Long,
    val mods: Mods,
    val score: Long,
    val accuracy: Double,
    val maxCombo: Long,

    /**
     * This is stored on the score itself since LocalTrack doesn't
     * store different maps and their converts
     */
    val maxComboAchievable: Long,
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

    val countGeki: Long? = null,
    val countKatu: Long? = null,
    val sliderEndHits: Long? = null,
    val smallTickHits: Long? = null,
    val largeTickHits: Long? = null,
    val isLazer: Boolean? = null,
    val beatmapPath: String? = null,

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
    val player = column("player") { it.player }
    val playerId = column("playerId") { it.playerId }
    val mods = json(
        "mods",
        Mods.serializer()
    ) { it.mods }
    val score = column("score") { it.score }
    val accuracy = column("accuracy") { it.accuracy }
    val maxCombo = column("maxCombo") { it.maxCombo }
    val maxComboAchievable = column("maxComboAchievable") { it.maxComboAchievable }
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

    val countGeki = nullable("countGeki") { it.countGeki }
    val countKatu = nullable("countKatu") { it.countKatu }
    val sliderEndHits = nullable("sliderEndHits") { it.sliderEndHits }
    val smallTickHits = nullable("smallTickHits") { it.smallTickHits }
    val largeTickHits = nullable("largeTickHits") { it.largeTickHits }
    val isLazer = nullable("isLazer") { it.isLazer }
    val beatmapPath = nullable("beatmapPath") { it.beatmapPath }

    override fun decode(row: Row): Score {
        return Score(
            id = row[id],
            beatmapId = row[beatmapId],
            beatmapChecksum = row[beatmapChecksum],
            playedAt = row[playedAt],
            player = row[player],
            playerId = row[playerId],
            mods = row[mods],
            score = row[score],
            accuracy = row[accuracy],
            maxCombo = row[maxCombo],
            maxComboAchievable = row[maxComboAchievable],
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

            countGeki = row[countGeki],
            countKatu = row[countKatu],
            sliderEndHits = row[sliderEndHits],
            smallTickHits = row[smallTickHits],
            largeTickHits = row[largeTickHits],
            isLazer = row[isLazer],
            beatmapPath = row[beatmapPath],

            isConvert = row[isConvert],
            mode = row[mode],
        )
    }
}