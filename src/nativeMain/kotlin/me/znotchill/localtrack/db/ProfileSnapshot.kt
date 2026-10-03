package me.znotchill.localtrack.db

import me.znotchill.kelp.Model
import me.znotchill.kelp.Row

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
) {
    fun isUnchanged(other: ProfileSnapshot?) = other != null &&
            pp == other.pp &&
            level == other.level &&
            globalRank == other.globalRank &&
            countryCode == other.countryCode &&
            accuracy == other.accuracy &&
            playCount == other.playCount &&
            rankedScore == other.rankedScore &&
            matchmakingRating == other.matchmakingRating &&
            matchmakingRank == other.matchmakingRank &&
            matchmakingPlays == other.matchmakingPlays &&
            matchmakingWins == other.matchmakingWins &&
            matchmakingIsProvisional == other.matchmakingIsProvisional

}

object ProfileSnapshotModel : Model<ProfileSnapshot>("profile_snapshots") {
    val id = column("id") { it.id }
    val timestamp = column("timestamp") { it.timestamp }
    val pp = column("pp") { it.pp }
    val level = column("level") { it.level }
    val globalRank = column("globalRank") { it.globalRank }
    val countryCode = column("countryCode") { it.countryCode }
    val accuracy = column("accuracy") { it.accuracy }
    val playCount = column("playCount") { it.playCount }
    val rankedScore = column("rankedScore") { it.rankedScore }
    val matchmakingRating = column("matchmakingRating") { it.matchmakingRating }
    val matchmakingRank = column("matchmakingRank") { it.matchmakingRank }
    val matchmakingPlays = column("matchmakingPlays") { it.matchmakingPlays }
    val matchmakingWins = column("matchmakingWins") { it.matchmakingWins }
    val matchmakingIsProvisional = column("matchmakingIsProvisional") {
        it.matchmakingIsProvisional
    }

    override fun decode(row: Row): ProfileSnapshot {
        return ProfileSnapshot(
            id = row[id],
            timestamp = row[timestamp],
            pp = row[pp],
            level = row[level],
            globalRank = row[globalRank],
            countryCode = row[countryCode],
            accuracy = row[accuracy],
            playCount = row[playCount],
            rankedScore = row[rankedScore],
            matchmakingRating = row[matchmakingRating],
            matchmakingRank = row[matchmakingRank],
            matchmakingPlays = row[matchmakingPlays],
            matchmakingWins = row[matchmakingWins],
            matchmakingIsProvisional = row[matchmakingIsProvisional]
        )
    }
}