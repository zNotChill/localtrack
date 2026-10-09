package me.znotchill.localtrack.db

import kotlinx.serialization.Serializable
import me.znotchill.kelp.Database
import me.znotchill.kelp.conditions.eq
import me.znotchill.kelp.conditions.isLatest
import me.znotchill.localtrack.db.BeatmapEntryModel.update
import me.znotchill.localtrack.db.BeatmapEntryModel.where

class TrackRepo(
    val database: Database
) {
    suspend fun insertScore(score: Score) {
        ScoreModel.insert(database, score)
    }

    suspend fun getLatestProfileSnapshot(): ProfileSnapshot? {
        return ProfileSnapshotModel.where(database) {
            timestamp.isLatest
        }.firstOrNull()
    }

    suspend fun insertProfileSnapshot(snapshot: ProfileSnapshot) = database.transaction {
        ProfileSnapshotModel.insert(database, snapshot)
    }

    suspend fun recordScore(
        score: Score,
        profileSnapshot: ProfileSnapshot,
        beatmap: BeatmapEntry
    ) = database.transaction {
        insertScore(score)
        println("score inserted")

        val latest = getLatestProfileSnapshot()

        // if the snapshot has changed since the latest one,
        // insert a new one
        if (latest == null || !latest.isUnchanged(profileSnapshot)) {
            println("profile snapshot inserted")
            insertProfileSnapshot(profileSnapshot)
        } else {
            println("profile snapshot skipped")
        }

        println("beatmap upserted")
        upsertBeatmap(beatmap)
    }


    suspend fun upsertBeatmap(
        beatmap: BeatmapEntry
    ) {
        val existing = BeatmapEntryModel.where(database) {
            BeatmapEntryModel.checksum eq beatmap.checksum
        }.firstOrNull()

        if (existing == null) {
            BeatmapEntryModel.insert(database, beatmap)
        } else {
            BeatmapEntryModel.update(database, beatmap) {
                checksum eq beatmap.checksum
            }
        }
    }
    suspend fun getRecentScores(limit: Long): List<Score> {
        return ScoreModel.recent(
            database,
            ScoreModel.playedAt,
            limit
        )
    }

    suspend fun getBeatmap(id: Long): BeatmapEntry? {
        return BeatmapEntryModel.where(database) {
            BeatmapEntryModel.id eq id
        }.firstOrNull()
    }

    suspend fun getRecentScoresWithBeatmaps(limit: Long): ScoresResponse {
        val scores = getRecentScores(limit)

        val beatmaps = scores
            .distinctBy { it.beatmapId }
            .mapNotNull { getBeatmap(it.beatmapId) }

        return ScoresResponse(
            beatmaps = beatmaps,
            scores = scores
        )
    }
}


@Serializable
data class ScoresResponse(
    val beatmaps: List<BeatmapEntry>,
    val scores: List<Score>
)
