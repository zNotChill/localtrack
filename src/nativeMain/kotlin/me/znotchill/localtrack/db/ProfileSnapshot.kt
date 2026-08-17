package me.znotchill.localtrack.db

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ProfileSnapshot(
    val id: Long = 0,
    val timestamp: Long,
    val pp: Double,
    val level: Double,
    val globalRank: Long,
    val countryCode: String,
    val accuracy: Double,
    val playCount: Long,
    val rankedScore: Long,
    val matchmakingRating: Double,
    val matchmakingRank: Long,
    val matchmakingPlays: Long,
    val matchmakingWins: Long,
    val matchmakingIsProvisional: Boolean
)

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
    val misses: List<Long> = emptyList()
)

class LocalTrackRepository(
    private val db: LocalTrackDatabase,
    private val json: Json
) {

    fun insertProfileSnapshot(snapshot: ProfileSnapshot) {
        db.profileSnapshotQueries.insertProfileSnapshot(
            timestamp = snapshot.timestamp,
            pp = snapshot.pp,
            level = snapshot.level,
            globalRank = snapshot.globalRank,
            countryCode = snapshot.countryCode,
            accuracy = snapshot.accuracy,
            playCount = snapshot.playCount,
            rankedScore = snapshot.rankedScore,
            matchmakingRating = snapshot.matchmakingRating,
            matchmakingRank = snapshot.matchmakingRank,
            matchmakingPlays = snapshot.matchmakingPlays,
            matchmakingWins = snapshot.matchmakingWins,
            matchmakingIsProvisional = if (snapshot.matchmakingIsProvisional) 1L else 0L
        )
    }

    fun getLatestProfileSnapshot(): ProfileSnapshot? =
        db.profileSnapshotQueries.getLatestSnapshot().executeAsOneOrNull()?.toProfileSnapshot()

    fun getAllProfileSnapshots(): List<ProfileSnapshot> =
        db.profileSnapshotQueries.getAllSnapshots().executeAsList().map { it.toProfileSnapshot() }

    fun getProfileSnapshots(from: Long, to: Long): List<ProfileSnapshot> =
        db.profileSnapshotQueries.getSnapshotsBetween(from, to).executeAsList().map { it.toProfileSnapshot() }

    fun insertScore(scoreData: Score): Long {
        var newId = 0L
        db.transaction {
            db.scoreQueries.insertScore(
                beatmapId = scoreData.beatmapId,
                beatmapChecksum = scoreData.beatmapChecksum,
                playedAt = scoreData.playedAt,
                mods = scoreData.mods.joinToString(","),
                modRate = scoreData.modRate,
                score = scoreData.score,
                accuracy = scoreData.accuracy,
                maxCombo = scoreData.maxCombo,
                rank = scoreData.rank,
                pp = scoreData.pp,
                ppFc = scoreData.ppFc,
                count300 = scoreData.count300,
                count100 = scoreData.count100,
                count50 = scoreData.count50,
                countMiss = scoreData.countMiss,
                unstableRate = scoreData.unstableRate,
                hitErrorArray = json.encodeToString(scoreData.hitErrorArray)
            )

            newId = db.scoreQueries.lastInsertRowId().executeAsOne()

        }
        return newId
    }

    fun upsertBeatmap(beatmap: BeatmapInfo) {
        val existing = db.beatmapEntryQueries.getBeatmapById(beatmap.id).executeAsOneOrNull()
        db.beatmapEntryQueries.insertOrReplaceBeatmap(
            id = beatmap.id,
            setId = beatmap.setId,
            checksum = beatmap.checksum,
            artist = beatmap.artist,
            artistUnicode = beatmap.artistUnicode,
            title = beatmap.title,
            titleUnicode = beatmap.titleUnicode,
            mapper = beatmap.mapper,
            version = beatmap.version,
            source = beatmap.source,
            tags = beatmap.tags,
            status = beatmap.status,
            starsTotal = beatmap.starsTotal,
            starsAim = beatmap.starsAim,
            starsSpeed = beatmap.starsSpeed,
            ar = beatmap.ar,
            cs = beatmap.cs,
            od = beatmap.od,
            hp = beatmap.hp,
            bpm = beatmap.bpm,
            circles = beatmap.circles,
            sliders = beatmap.sliders,
            spinners = beatmap.spinners,
            maxCombo = beatmap.maxCombo,
            mp3Length = beatmap.mp3Length,
            firstSeenAt = existing?.firstSeenAt ?: beatmap.firstSeenAt,
            lastSeenAt = beatmap.lastSeenAt
        )
    }

    fun getBeatmapByChecksum(checksum: String): BeatmapInfo? =
        db.beatmapEntryQueries.getBeatmapByChecksum(checksum).executeAsOneOrNull()?.toBeatmapInfo()

    fun getBeatmap(id: Long): BeatmapInfo? =
        db.beatmapEntryQueries.getBeatmapById(id).executeAsOneOrNull()?.toBeatmapInfo()

    private fun BeatmapEntry.toBeatmapInfo() = BeatmapInfo(
        id = id, setId = setId, checksum = checksum, artist = artist, artistUnicode = artistUnicode,
        title = title, titleUnicode = titleUnicode, mapper = mapper, version = version,
        source = source, tags = tags, status = status, starsTotal = starsTotal, starsAim = starsAim,
        starsSpeed = starsSpeed, ar = ar, cs = cs, od = od, hp = hp, bpm = bpm, circles = circles,
        sliders = sliders, spinners = spinners, maxCombo = maxCombo, mp3Length = mp3Length,
        firstSeenAt = firstSeenAt, lastSeenAt = lastSeenAt
    )

    fun getRecentScores(limit: Long = 50): List<Score> =
        db.scoreQueries.getRecentScores(limit).executeAsList().map { it.toScore() }

    fun getScoresForBeatmap(beatmapId: Long): List<Score> =
        db.scoreQueries.getScoresForBeatmap(beatmapId).executeAsList().map { it.toScore() }

    fun getBestScoreForBeatmap(beatmapId: Long): Score? =
        db.scoreQueries.getBestScoreForBeatmap(beatmapId).executeAsOneOrNull()?.toScore()

    fun getTopScoresByPp(limit: Long = 100): List<Score> =
        db.scoreQueries.getTopScoresByPp(limit).executeAsList().map { it.toScore() }

    fun countScores(): Long =
        db.scoreQueries.countScores().executeAsOne()

    private fun ProfileSnapshotEntry.toProfileSnapshot() = ProfileSnapshot(
        id = id,
        timestamp = timestamp,
        pp = pp,
        level = level,
        globalRank = globalRank,
        countryCode = countryCode,
        accuracy = accuracy,
        playCount = playCount,
        rankedScore = rankedScore,
        matchmakingRating = matchmakingRating,
        matchmakingRank = matchmakingRank,
        matchmakingPlays = matchmakingPlays,
        matchmakingWins = matchmakingWins,
        matchmakingIsProvisional = matchmakingIsProvisional == 1L
    )

    private fun GetTopScoresByPp.toScore(): Score = Score(
        id = id,
        beatmapId = beatmapId,
        beatmapChecksum = beatmapChecksum,
        playedAt = playedAt,
        mods = mods.split(",").filter { it.isNotBlank() },
        modRate = modRate,
        score = score,
        accuracy = accuracy,
        maxCombo = maxCombo,
        rank = rank,
        pp = pp,
        ppFc = ppFc,
        count300 = count300,
        count100 = count100,
        count50 = count50,
        countMiss = countMiss,
        unstableRate = unstableRate,
        hitErrorArray = hitErrorArray?.let { json.decodeFromString<List<Double>>(it) } ?: emptyList()
    )

    private fun ScoreEntry.toScore(): Score = Score(
        id = id,
        beatmapId = beatmapId,
        beatmapChecksum = beatmapChecksum,
        playedAt = playedAt,
        mods = mods.split(",").filter { it.isNotBlank() },
        modRate = modRate,
        score = score,
        accuracy = accuracy,
        maxCombo = maxCombo,
        rank = rank,
        pp = pp,
        ppFc = ppFc,
        count300 = count300,
        count100 = count100,
        count50 = count50,
        countMiss = countMiss,
        unstableRate = unstableRate,
        hitErrorArray = hitErrorArray?.let { json.decodeFromString<List<Double>>(it) } ?: emptyList()
    )
}