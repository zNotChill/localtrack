package me.znotchill.localtrack.search.map

import me.znotchill.kelp.Database
import me.znotchill.kelp.UserModel.where
import me.znotchill.kelp.column.Column
import me.znotchill.kelp.conditions.Condition
import me.znotchill.kelp.conditions.and
import me.znotchill.kelp.conditions.contains
import me.znotchill.kelp.conditions.eq
import me.znotchill.kelp.conditions.gt
import me.znotchill.kelp.conditions.gte
import me.znotchill.kelp.conditions.lt
import me.znotchill.kelp.conditions.lte
import me.znotchill.kelp.conditions.neq
import me.znotchill.localtrack.LocalTrack
import me.znotchill.localtrack.api.v1.queries.MapQuery
import me.znotchill.localtrack.db.BeatmapEntry
import me.znotchill.localtrack.db.BeatmapEntryModel

object MapSearch {
    val db: Database
        get() = LocalTrack.instance.db

    suspend fun queryBeatmaps(query: MapQuery): List<BeatmapEntry> {
        val conditions = query.predicates.mapNotNull(::predicate)

        return BeatmapEntryModel.where(
            db,
            orderBy = query.sortBy?.let(::sortColumn),
            descending = query.sortDescending,
            offset = query.offset,
            limit = query.limit
        ) {
            conditions.reduceOrNull { left, right -> left and right }
                ?: (BeatmapEntryModel.id gt 0L)
        }
    }

    private fun predicate(predicate: FilterPredicate): Condition? {
        return when (predicate.field) {
            "artist" -> stringPredicate(BeatmapEntryModel.artist, predicate)
            "artistUnicode" -> stringPredicate(BeatmapEntryModel.artistUnicode, predicate)
            "title" -> stringPredicate(BeatmapEntryModel.title, predicate)
            "titleUnicode" -> stringPredicate(BeatmapEntryModel.titleUnicode, predicate)
            "mapper" -> stringPredicate(BeatmapEntryModel.mapper, predicate)
            "version" -> stringPredicate(BeatmapEntryModel.version, predicate)
            "source" -> stringPredicate(BeatmapEntryModel.source, predicate)
            "status" -> stringPredicate(BeatmapEntryModel.status, predicate)

            "id" -> longPredicate(BeatmapEntryModel.id, predicate)
            "setId" -> longPredicate(BeatmapEntryModel.setId, predicate)
            "circles" -> longPredicate(BeatmapEntryModel.circles, predicate)
            "sliders" -> longPredicate(BeatmapEntryModel.sliders, predicate)
            "spinners" -> longPredicate(BeatmapEntryModel.spinners, predicate)
            "maxCombo" -> longPredicate(BeatmapEntryModel.maxCombo, predicate)
            "mp3Length" -> longPredicate(BeatmapEntryModel.mp3Length, predicate)

            "starsTotal" -> doublePredicate(BeatmapEntryModel.starsTotal, predicate)
            "starsAim" -> doublePredicate(BeatmapEntryModel.starsAim, predicate)
            "starsSpeed" -> doublePredicate(BeatmapEntryModel.starsSpeed, predicate)
            "ar" -> doublePredicate(BeatmapEntryModel.ar, predicate)
            "cs" -> doublePredicate(BeatmapEntryModel.cs, predicate)
            "od" -> doublePredicate(BeatmapEntryModel.od, predicate)
            "hp" -> doublePredicate(BeatmapEntryModel.hp, predicate)
            "bpm" -> doublePredicate(BeatmapEntryModel.bpm, predicate)

            else -> null
        }
    }

    private fun stringPredicate(
        column: Column<String>,
        predicate: FilterPredicate
    ): Condition? {
        return when (predicate.op) {
            FilterOp.EQ -> column eq predicate.value
            FilterOp.NEQ -> column neq predicate.value
            FilterOp.CONTAINS -> column contains predicate.value
            else -> null
        }
    }

    private fun longPredicate(
        column: Column<Long>,
        predicate: FilterPredicate
    ): Condition? {
        val value = predicate.value.toLongOrNull() ?: return null

        return when (predicate.op) {
            FilterOp.EQ -> column eq value
            FilterOp.NEQ -> column neq value
            FilterOp.GT -> column gt value
            FilterOp.GTE -> column gte value
            FilterOp.LT -> column lt value
            FilterOp.LTE -> column lte value
            FilterOp.CONTAINS -> null
        }
    }

    private fun doublePredicate(
        column: Column<Double>,
        predicate: FilterPredicate
    ): Condition? {
        val value = predicate.value.toDoubleOrNull() ?: return null

        return when (predicate.op) {
            FilterOp.EQ -> column eq value
            FilterOp.NEQ -> column neq value
            FilterOp.GT -> column gt value
            FilterOp.GTE -> column gte value
            FilterOp.LT -> column lt value
            FilterOp.LTE -> column lte value
            FilterOp.CONTAINS -> null
        }
    }

    private fun sortColumn(field: String) =
        when (field) {
            "id" -> BeatmapEntryModel.id
            "setId" -> BeatmapEntryModel.setId
            "checksum" -> BeatmapEntryModel.checksum
            "artist" -> BeatmapEntryModel.artist
            "artistUnicode" -> BeatmapEntryModel.artistUnicode
            "title" -> BeatmapEntryModel.title
            "titleUnicode" -> BeatmapEntryModel.titleUnicode
            "mapper" -> BeatmapEntryModel.mapper
            "version" -> BeatmapEntryModel.version
            "source" -> BeatmapEntryModel.source
            "status" -> BeatmapEntryModel.status
            "starsTotal" -> BeatmapEntryModel.starsTotal
            "starsAim" -> BeatmapEntryModel.starsAim
            "starsSpeed" -> BeatmapEntryModel.starsSpeed
            "ar" -> BeatmapEntryModel.ar
            "cs" -> BeatmapEntryModel.cs
            "od" -> BeatmapEntryModel.od
            "hp" -> BeatmapEntryModel.hp
            "bpm" -> BeatmapEntryModel.bpm
            "circles" -> BeatmapEntryModel.circles
            "sliders" -> BeatmapEntryModel.sliders
            "spinners" -> BeatmapEntryModel.spinners
            "maxCombo" -> BeatmapEntryModel.maxCombo
            "mp3Length" -> BeatmapEntryModel.mp3Length
            "firstSeenAt" -> BeatmapEntryModel.firstSeenAt
            "lastSeenAt" -> BeatmapEntryModel.lastSeenAt
            else -> BeatmapEntryModel.id
        }
}