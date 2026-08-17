package me.znotchill.localtrack.events

import me.znotchill.localtrack.LocalTrack
import me.znotchill.localtrack.db.BeatmapEntry
import me.znotchill.localtrack.db.ProfileSnapshot
import me.znotchill.localtrack.db.Score
import me.znotchill.localtrack.payload.GameState
import me.znotchill.localtrack.payload.TosuState
import kotlin.time.Clock

class GameStateChange(
    val newGameState: GameState,
    val oldGameState: GameState,
    val newState: TosuState,
    val oldState: TosuState,
) : Event {
    fun onFire() {
        println("new: $newGameState old: $oldGameState")
        val repo = LocalTrack.instance.trackRepo
        if (newGameState == GameState.RESULT_SCREEN) {
            if (oldGameState != GameState.PLAY) return

            val results = newState.resultsScreen
            val beatmap = newState.beatmap
            val profile = newState.profile

            if (results.playerName != profile.name) return

            val now = Clock.System.now().toEpochMilliseconds()

            val score = Score(
                beatmapId = beatmap.id,
                beatmapChecksum = beatmap.checksum,
                playedAt = now,
                mods = results.mods.array.map { it.acronym },
                modRate = results.mods.rate,
                score = results.score,
                accuracy = results.accuracy,
                maxCombo = results.maxCombo,
                rank = results.rank,
                pp = results.pp.current.takeIf { it != 0.0 },
                ppFc = results.pp.fc.takeIf { it != 0.0 },
                count300 = results.hits.threeHundred,
                count100 = results.hits.hundred,
                count50 = results.hits.fifty,
                countMiss = results.hits.miss,
                unstableRate = newState.play.unstableRate.takeIf { it != 0.0 },
                hitErrorArray = newState.play.hitErrorArray
            )
            repo.insertScore(score)

            val newSnapshot = ProfileSnapshot(
                timestamp = now,
                pp = profile.pp,
                level = profile.level,
                globalRank = profile.globalRank,
                countryCode = profile.countryCode.name,
                accuracy = profile.accuracy,
                playCount = profile.playCount,
                rankedScore = profile.rankedScore,
                matchmakingRating = profile.matchmaking.rating,
                matchmakingRank = profile.matchmaking.rank,
                matchmakingPlays = profile.matchmaking.plays,
                matchmakingWins = profile.matchmaking.wins,
                matchmakingIsProvisional = profile.matchmaking.isProvisional
            )

            val latest = repo.getLatestProfileSnapshot()
            val isUnchanged = latest != null &&
                    latest.pp == newSnapshot.pp &&
                    latest.level == newSnapshot.level &&
                    latest.globalRank == newSnapshot.globalRank &&
                    latest.countryCode == newSnapshot.countryCode &&
                    latest.accuracy == newSnapshot.accuracy &&
                    latest.playCount == newSnapshot.playCount &&
                    latest.rankedScore == newSnapshot.rankedScore &&
                    latest.matchmakingRating == newSnapshot.matchmakingRating &&
                    latest.matchmakingRank == newSnapshot.matchmakingRank &&
                    latest.matchmakingPlays == newSnapshot.matchmakingPlays &&
                    latest.matchmakingWins == newSnapshot.matchmakingWins &&
                    latest.matchmakingIsProvisional == newSnapshot.matchmakingIsProvisional

            if (!isUnchanged) {
                repo.insertProfileSnapshot(newSnapshot)
                println("profile snapshot inserted (changed)")
            } else {
                println("profile snapshot skipped (unchanged)")
            }

            repo.upsertBeatmap(
                BeatmapEntry(
                    id = beatmap.id,
                    setId = beatmap.set,
                    checksum = beatmap.checksum,
                    artist = beatmap.artist,
                    artistUnicode = beatmap.artistUnicode,
                    title = beatmap.title,
                    titleUnicode = beatmap.titleUnicode,
                    mapper = beatmap.mapper,
                    version = beatmap.version,
                    source = beatmap.source,
                    tags = beatmap.tags,
                    status = beatmap.status.name,
                    starsTotal = beatmap.stats.stars.total,
                    starsAim = beatmap.stats.stars.aim ?: 0.0,
                    starsSpeed = beatmap.stats.stars.speed ?: 0.0,
                    ar = beatmap.stats.ar.original,
                    cs = beatmap.stats.cs.original,
                    od = beatmap.stats.od.original,
                    hp = beatmap.stats.hp.original,
                    bpm = beatmap.stats.bpm.common,
                    circles = beatmap.stats.objects.circles,
                    sliders = beatmap.stats.objects.sliders,
                    spinners = beatmap.stats.objects.spinners,
                    maxCombo = beatmap.stats.maxCombo,
                    mp3Length = beatmap.time.mp3Length,
                    firstSeenAt = now,
                    lastSeenAt = now
                )
            )

            println("new play set! score, profile snapshot, and beatmap info in/upserted")
        }
    }
}