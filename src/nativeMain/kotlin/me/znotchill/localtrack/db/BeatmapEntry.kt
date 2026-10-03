package me.znotchill.localtrack.db

import kotlinx.serialization.Serializable
import me.znotchill.kelp.Model
import me.znotchill.kelp.Row

@Serializable
data class BeatmapEntry(
    val id: Long = 0,
    val setId: Long,
    val checksum: String,
    val artist: String,
    val artistUnicode: String,
    val title: String,
    val titleUnicode: String,
    val mapper: String,
    val version: String,
    val source: String,
    val tags: String,
    val status: String,
    val starsTotal: Double,
    val starsAim: Double,
    val starsSpeed: Double,
    val ar: Double,
    val cs: Double,
    val od: Double,
    val hp: Double,
    val bpm: Double,
    val circles: Long,
    val sliders: Long,
    val spinners: Long,
    val maxCombo: Long,
    val mp3Length: Long,
    val firstSeenAt: Long,
    val lastSeenAt: Long
)

object BeatmapEntryModel : Model<BeatmapEntry>("beatmaps") {
    val id = column("id") { it.id }
    val setId = column("setId") { it.setId }
    val checksum = column("checksum") { it.checksum }
    val artist = column("artist") { it.artist }
    val artistUnicode = column("artistUnicode") { it.artistUnicode }
    val title = column("title") { it.title }
    val titleUnicode = column("titleUnicode") { it.titleUnicode }
    val mapper = column("mapper") { it.mapper }
    val version = column("version") { it.version }
    val source = column("source") { it.source }
    val tags = column("tags") { it.tags }
    val status = column("status") { it.status }
    val starsTotal = column("starsTotal") { it.starsTotal }
    val starsAim = column("starsAim") { it.starsAim }
    val starsSpeed = column("starsSpeed") { it.starsSpeed }
    val ar = column("ar") { it.ar }
    val cs = column("cs") { it.cs }
    val od = column("od") { it.od }
    val hp = column("hp") { it.hp }
    val bpm = column("bpm") { it.bpm }
    val circles = column("circles") { it.circles }
    val sliders = column("sliders") { it.sliders }
    val spinners = column("spinners") { it.spinners }
    val maxCombo = column("maxCombo") { it.maxCombo }
    val mp3Length = column("mp3Length") { it.mp3Length }
    val firstSeenAt = column("firstSeenAt") { it.firstSeenAt }
    val lastSeenAt = column("lastSeenAt") { it.lastSeenAt }

    override fun decode(row: Row): BeatmapEntry {
        return BeatmapEntry(
            id = row[id],
            setId = row[setId],
            checksum = row[checksum],
            artist = row[artist],
            artistUnicode = row[artistUnicode],
            title = row[title],
            titleUnicode = row[titleUnicode],
            mapper = row[mapper],
            version = row[version],
            source = row[source],
            tags = row[tags],
            status = row[status],
            starsTotal = row[starsTotal],
            starsAim = row[starsAim],
            starsSpeed = row[starsSpeed],
            ar = row[ar],
            cs = row[cs],
            od = row[od],
            hp = row[hp],
            bpm = row[bpm],
            circles = row[circles],
            sliders = row[sliders],
            spinners = row[spinners],
            maxCombo = row[maxCombo],
            mp3Length = row[mp3Length],
            firstSeenAt = row[firstSeenAt],
            lastSeenAt = row[lastSeenAt]
        )
    }
}