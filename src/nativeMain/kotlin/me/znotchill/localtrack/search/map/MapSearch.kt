package me.znotchill.localtrack.search.map

import me.znotchill.localtrack.LocalTrack
import me.znotchill.localtrack.api.v1.queries.MapQuery
import me.znotchill.localtrack.db.BeatmapEntry
import me.znotchill.localtrack.db.LocalTrackDatabase

object MapSearch {
    val db: LocalTrackDatabase
        get() = LocalTrack.instance.db

    fun queryBeatmaps(query: MapQuery): List<BeatmapEntry> {
        var results = db.beatmapEntryQueries.getAllBeatmaps()
            .executeAsList()

        for (predicate in query.predicates) {
            results = results.filter { matchesPredicate(it, predicate) }
        }

        query.sortBy?.let { field ->
            val comparator = beatmapComparator(field)
            results = if (query.sortDescending) results.sortedWith(comparator.reversed())
            else results.sortedWith(comparator)
        }

        return results.drop(query.offset.toInt()).take(query.limit.toInt())
    }

    private fun matchesPredicate(map: BeatmapEntry, predicate: FilterPredicate): Boolean {
        val fieldValue: Any = when (predicate.field) {
            "artist" -> map.artist
            "title" -> map.title
            "mapper" -> map.mapper
            "status" -> map.status
            "starsTotal" -> map.starsTotal
            "starsAim" -> map.starsAim
            "starsSpeed" -> map.starsSpeed
            "spinners" -> map.spinners
            "id" -> map.id
            "setId" -> map.setId
            "circles" -> map.circles
            "sliders" -> map.sliders
            "maxCombo" -> map.maxCombo
            "mp3Length" -> map.mp3Length
            "ar" -> map.ar
            "cs" -> map.cs
            "od" -> map.od
            "hp" -> map.hp
            "bpm" -> map.bpm
            else -> return false
        }

        return when (fieldValue) {
            is String -> compareString(fieldValue, predicate.op, predicate.value)
            is Double -> compareDouble(fieldValue, predicate.op, predicate.value.toDoubleOrNull())
            else -> false
        }
    }

    private fun compareString(actual: String, op: FilterOp, expected: String): Boolean = when (op) {
        FilterOp.EQ -> actual.equals(expected, ignoreCase = true)
        FilterOp.NEQ -> !actual.equals(expected, ignoreCase = true)
        FilterOp.CONTAINS -> actual.contains(expected, ignoreCase = true)
        else -> false
    }

    private fun compareDouble(actual: Double, op: FilterOp, expected: Double?): Boolean {
        if (expected == null) return false
        return when (op) {
            FilterOp.EQ -> actual == expected
            FilterOp.NEQ -> actual != expected
            FilterOp.GT -> actual > expected
            FilterOp.GTE -> actual >= expected
            FilterOp.LT -> actual < expected
            FilterOp.LTE -> actual <= expected
            FilterOp.CONTAINS -> false
        }
    }

    private fun beatmapComparator(field: String): Comparator<BeatmapEntry> = when (field) {
        "id" -> compareBy { it.id }
        "setId" -> compareBy { it.setId }
        "checksum" -> compareBy { it.checksum }
        "artist" -> compareBy { it.artist }
        "artistUnicode" -> compareBy { it.artistUnicode }
        "title" -> compareBy { it.title }
        "titleUnicode" -> compareBy { it.titleUnicode }
        "mapper" -> compareBy { it.mapper }
        "version" -> compareBy { it.version }
        "source" -> compareBy { it.source }
        "status" -> compareBy { it.status }
        "starsTotal" -> compareBy { it.starsTotal }
        "starsAim" -> compareBy { it.starsAim }
        "starsSpeed" -> compareBy { it.starsSpeed }
        "ar" -> compareBy { it.ar }
        "cs" -> compareBy { it.cs }
        "od" -> compareBy { it.od }
        "hp" -> compareBy { it.hp }
        "bpm" -> compareBy { it.bpm }
        "circles" -> compareBy { it.circles }
        "sliders" -> compareBy { it.sliders }
        "spinners" -> compareBy { it.spinners }
        "maxCombo" -> compareBy { it.maxCombo }
        "mp3Length" -> compareBy { it.mp3Length }
        "firstSeenAt" -> compareBy { it.firstSeenAt }
        "lastSeenAt" -> compareBy { it.lastSeenAt }
        else -> compareBy { it.id }
    }
}