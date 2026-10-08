package me.znotchill.localtrack.tosu

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess
import me.znotchill.kelp.Database
import me.znotchill.kelp.conditions.eq
import me.znotchill.localtrack.db.Score
import me.znotchill.localtrack.db.ScoreModel

class PpRecalculator(
    private val db: Database,
    private val http: HttpClient,
    private val tosu: String = "http://127.0.0.1:24050",
) {
    suspend fun recalculateAll(onProgress: (done: Int, total: Int) -> Unit = { _, _ -> }) {
        val scores = ScoreModel.getAll(db)

        scores.forEachIndexed { i, s ->
            val path = s.beatmapPath
            if (path != null) {
                val pp = calc(s, path, fc = false)
                val ppFc = calc(s, path, fc = true)
                if (pp != null) {
                    with(ScoreModel) {
                        update(db, s.copy(pp = pp, ppFc = ppFc)) { id eq s.id }
                    }
                }
            }
            onProgress(i + 1, scores.size)
        }
    }

    private suspend fun calc(s: Score, path: String, fc: Boolean): Double? {
        val response = http.get("$tosu/api/calculate/pp") {
            parameter("path", path)
            parameter("mode", s.mode.ordinal)
            s.isLazer?.let { parameter("lazer", it) }
            parameter("mods", s.mods.number)
            parameter("n300", if (fc) s.count300 + s.countMiss else s.count300)
            parameter("n100", s.count100)
            parameter("n50", s.count50)
            parameter("nMisses", if (fc) 0 else s.countMiss)
            parameter("combo", if (fc) s.maxComboAchievable else s.maxCombo)
            s.countGeki?.let { parameter("nGeki", it) }
            s.countKatu?.let { parameter("nKatu", it) }
            s.sliderEndHits?.let { parameter("sliderEndHits", it) }
            s.smallTickHits?.let { parameter("smallTickHits", it) }
            s.largeTickHits?.let { parameter("largeTickHits", it) }
        }
        if (!response.status.isSuccess()) return null
        return response.body<TosuPpResponse>().pp
    }
}