package me.znotchill.localtrack

import co.touchlab.kermit.Logger
import io.github.smyrgeorge.sqlx4k.sqlite.SQLite
import kotlinx.coroutines.*
import me.znotchill.kelp.Database
import me.znotchill.kelp.dialects.SqliteDialect
import me.znotchill.localtrack.api.LocalTrackAPI
import me.znotchill.localtrack.db.BeatmapEntryModel
import me.znotchill.localtrack.db.ProfileSnapshotModel
import me.znotchill.localtrack.db.ScoreModel
import me.znotchill.localtrack.db.TrackRepo
import me.znotchill.localtrack.storage.config.ConfigManager
import me.znotchill.localtrack.tosu.GameManager
import me.znotchill.localtrack.tosu.PpRecalculator
import me.znotchill.localtrack.tosu.TosuProcess
import me.znotchill.localtrack.tosu.TosuSetup

object LocalTrack {
    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    lateinit var repo: TrackRepo
    lateinit var db: Database

    suspend fun start() {
        ConfigManager.init()
        TosuSetup.init()

        val isTosuRunning = GameManager.isTosuRunning()
        if (!ConfigManager.config.setup.spinUpProcess)
            Logger.w("config value setup.spinUpProcess is false, finding existing tosu process")
        else {
            if (!isTosuRunning) {
                val tosuStarted = TosuProcess.start()
                if (tosuStarted) {
                    Logger.i("tosu started!")

                    val hasPtraceCap = TosuProcess.hasPtraceCap()
                    if (!hasPtraceCap) {
                        TosuSetup.alertPtraceCap()
                    }
                }
                else Logger.e("tosu failed to start.")
            } else Logger.i("found existing tosu process!")
        }

        val database = Database(
            driver = SQLite(
                url = "sqlite://localtrack.db"
            ),
            dialect = SqliteDialect
        )

        try {
            database.createTable(ScoreModel)
            database.createTable(BeatmapEntryModel)
            database.createTable(ProfileSnapshotModel)
            println(ScoreModel.migrate(database))
            BeatmapEntryModel.migrate(database)
            ProfileSnapshotModel.migrate(database)

            PpRecalculator(database, GameManager.client).recalculateAll(
                onProgress = { done, total ->
                    Logger.i("Recalculating ALL scores: $done/$total")
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Logger.i("DB opened!")

        db = database
        repo = TrackRepo(database)

        println("ALL SCORES:")
        println(
            ScoreModel.getAll(database).size
        )

        scope.launch {
            GameManager.websocketLoop()
        }

        LocalTrackAPI.start()
    }
}

fun main() = runBlocking {
    LocalTrack.start()
    LocalTrack.scope.launch {
        ShutdownHook.register()
    }
    Unit
//    api.start()
}