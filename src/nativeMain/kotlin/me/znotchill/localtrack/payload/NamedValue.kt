package me.znotchill.localtrack.payload

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class NamedValue(
    val number: Int,
    val name: String
)

@Serializable
data class TosuState(
    val game: Game,
    val client: String,
    val server: String,
    val state: GameState,
    val session: Session,
    val settings: Settings,
    val profile: Profile,
    val beatmap: Beatmap,
    val play: Play,
    val leaderboard: List<LeaderboardEntry> = emptyList(),
    val performance: Performance,
    val resultsScreen: ResultsScreen,
    val folders: Folders,
    val files: Files,
    val directPath: DirectPath,
    val tourney: Tourney
)

@Serializable
data class Game(
    val paused: Boolean
)

@Serializable
data class Session(
    val playTime: Long,
    val playCount: Long
)

@Serializable
data class Settings(
    val interfaceVisible: Boolean,
    val replayUIVisible: Boolean,
    val chatVisibilityStatus: NamedValue,
    val leaderboard: LeaderboardSettings,
    val progressBar: NamedValue,
    val bassDensity: Double,
    val resolution: Resolution,
    val client: ClientInfo,
    val scoreMeter: ScoreMeter,
    val cursor: Cursor,
    val mouse: Mouse,
    val tablet: Tablet,
    val mania: Mania,
    val sort: NamedValue,
    val group: NamedValue,
    val skin: Skin,
    val mode: NamedValue,
    val audio: Audio,
    val background: Background,
    val keybinds: Keybinds
)

@Serializable
data class LeaderboardSettings(
    val visible: Boolean,
    val type: NamedValue
)

@Serializable
data class Resolution(
    val fullscreen: Boolean,
    val width: Int,
    val height: Int,
    val widthFullscreen: Int,
    val heightFullscreen: Int
)

@Serializable
data class ClientInfo(
    val updateAvailable: Boolean,
    val branch: Int,
    val version: String
)

@Serializable
data class ScoreMeter(
    val type: NamedValue,
    val size: Double
)

@Serializable
data class Cursor(
    val useSkinCursor: Boolean,
    val autoSize: Boolean,
    val size: Double,
    val menuSize: Double
)

@Serializable
data class Mouse(
    val rawInput: Boolean,
    val disableButtons: Boolean,
    val disableWheel: Boolean,
    val sensitivity: Double
)

@Serializable
data class Tablet(
    val enabled: Boolean,
    val x: Double,
    val y: Double,
    val width: Double,
    val height: Double,
    val rotation: Double,
    val pressureThreshold: Double
)

@Serializable
data class Mania(
    val speedBPMScale: Boolean,
    val usePerBeatmapSpeedScale: Boolean,
    val scrollSpeed: Int,
    val scrollDirection: NamedValue
)

@Serializable
data class Skin(
    val useDefaultSkinInEditor: Boolean,
    val ignoreBeatmapSkins: Boolean,
    val tintSliderBall: Boolean,
    val useTaikoSkin: Boolean,
    val name: String
)

@Serializable
data class Audio(
    val ignoreBeatmapSounds: Boolean,
    val useSkinSamples: Boolean,
    val volume: Volume,
    val offset: AudioOffset
)

@Serializable
data class Volume(
    val masterInactive: Double,
    val master: Double,
    val music: Double,
    val effect: Double
)

@Serializable
data class AudioOffset(
    val universal: Double
)

@Serializable
data class Background(
    val storyboard: Boolean,
    val video: Boolean,
    val blur: Double,
    val dim: Double
)

@Serializable
data class Keybinds(
    val osu: OsuKeybinds,
    val fruits: FruitsKeybinds,
    val taiko: TaikoKeybinds,
    val quickRetry: String
)

@Serializable
data class OsuKeybinds(
    val k1: String,
    val k2: String,
    val smokeKey: String
)

@Serializable
data class FruitsKeybinds(
    val k1: String,
    val k2: String,
    @SerialName("Dash") val dash: String
)

@Serializable
data class TaikoKeybinds(
    val innerLeft: String,
    val innerRight: String,
    val outerLeft: String,
    val outerRight: String
)

@Serializable
data class Profile(
    val userStatus: NamedValue,
    val banchoStatus: NamedValue,
    val id: Long,
    val name: String,
    val mode: NamedValue,
    val rankedScore: Long,
    val level: Double,
    val accuracy: Double,
    val pp: Double,
    val playCount: Long,
    val globalRank: Long,
    val countryCode: NamedValue,
    val backgroundColour: String,
    val matchmaking: Matchmaking
)

@Serializable
data class Matchmaking(
    val rating: Double,
    val rank: Long,
    val plays: Long,
    val wins: Long,
    val isProvisional: Boolean
)

@Serializable
data class Beatmap(
    val isKiai: Boolean,
    val isBreak: Boolean,
    val isConvert: Boolean,
    val time: BeatmapTime,
    val status: NamedValue,
    val checksum: String,
    val id: Long,
    val set: Long,
    val mode: NamedValue,
    val artist: String,
    val artistUnicode: String,
    val title: String,
    val titleUnicode: String,
    val mapper: String,
    val version: String,
    val source: String,
    val tags: String,
    val stats: BeatmapStats
)

@Serializable
data class BeatmapTime(
    val live: Long,
    val firstObject: Long,
    val lastObject: Long,
    val mp3Length: Long
)

@Serializable
data class BeatmapStats(
    val stars: Stars,
    val ar: ConvertibleStat,
    val cs: ConvertibleStat,
    val od: ConvertibleStat,
    val hp: ConvertibleStat,
    val bpm: Bpm,
    val objects: BeatmapObjects,
    val maxCombo: Long
)

@Serializable
data class Stars(
    val live: Double,
    val aim: Double,
    val speed: Double,
    val sliderFactor: Double,
    val reading: Double? = null,
    val hitWindow: Double,
    val total: Double
)

@Serializable
data class ConvertibleStat(
    val original: Double,
    val converted: Double
)

@Serializable
data class Bpm(
    val realtime: Double,
    val common: Double,
    val min: Double,
    val max: Double
)

@Serializable
data class BeatmapObjects(
    val circles: Long,
    val sliders: Long,
    val spinners: Long,
    val holds: Long,
    val total: Long
)

@Serializable
data class Play(
    val failed: Boolean,
    val playerName: String,
    val mode: NamedValue,
    val score: Long,
    val accuracy: Double,
    val healthBar: HealthBar,
    val hits: Hits,
    val hitErrorArray: List<Double> = emptyList(),
    val combo: Combo,
    val mods: Mods,
    val rank: PlayRank,
    val pp: PlayPp,
    val unstableRate: Double
)

@Serializable
data class HealthBar(
    val normal: Double,
    val smooth: Double
)

@Serializable
data class Hits(
    @SerialName("0") val miss: Long,
    @SerialName("50") val fifty: Long,
    @SerialName("100") val hundred: Long,
    @SerialName("300") val threeHundred: Long,
    val geki: Long,
    val katu: Long,
    val sliderBreaks: Long = 0,
    val sliderEndHits: Long,
    val smallTickHits: Long,
    val largeTickHits: Long
)

@Serializable
data class Combo(
    val current: Long,
    val max: Long
)

@Serializable
data class Mods(
    val checksum: String,
    val number: Long,
    val name: String,
    val array: List<ModEntry> = emptyList(),
    val rate: Double
)

@Serializable
data class ModEntry(
    val acronym: String,
    val settings: Map<String, JsonElement> = emptyMap()
)

@Serializable
data class PlayRank(
    val current: String,
    val maxThisPlay: String
)

@Serializable
data class PlayPp(
    val current: Double,
    val fc: Double,
    val maxAchieved: Double,
    val maxAchievable: Double,
    val detailed: DetailedPp
)

@Serializable
data class DetailedPp(
    val current: PpBreakdown,
    val fc: PpBreakdown
)

@Serializable
data class PpBreakdown(
    val aim: Double,
    val speed: Double,
    val accuracy: Double,
    val difficulty: Double,
    val flashlight: Double,
    val total: Double
)

@Serializable
data class LeaderboardEntry(
    val isFailed: Boolean = false,
    val position: Long = 0,
    val team: Long = 0,
    val id: Long = 0,
    val name: String = "",
    val score: Long = 0,
    val accuracy: Double = 0.0,
    val combo: Long = 0,
    val maxCombo: Long = 0,
    val mods: Mods? = null,
    val rank: String = "",
    val hits: Hits? = null,
    val isPassing: Boolean = false
)

@Serializable
data class Performance(
    val accuracy: Map<String, Double>,
    val graph: Graph
)

@Serializable
data class Graph(
    val series: List<GraphSeries>,
    val xaxis: List<Double>
)

@Serializable
data class GraphSeries(
    val name: String,
    val data: List<Double>
)

@Serializable
data class ResultsScreen(
    val scoreId: Long,
    val playerName: String,
    val mode: NamedValue,
    val score: Long,
    val accuracy: Double,
    val name: String,
    val hits: ResultsHits,
    val mods: Mods,
    val maxCombo: Long,
    val rank: String,
    val pp: ResultsPp,
    val createdAt: String
)

@Serializable
data class ResultsHits(
    @SerialName("0") val miss: Long,
    @SerialName("50") val fifty: Long,
    @SerialName("100") val hundred: Long,
    @SerialName("300") val threeHundred: Long,
    val geki: Long,
    val katu: Long,
    val sliderEndHits: Long,
    val smallTickHits: Long,
    val largeTickHits: Long
)

@Serializable
data class ResultsPp(
    val current: Double,
    val fc: Double
)

@Serializable
data class Folders(
    val game: String,
    val skin: String,
    val songs: String,
    val beatmap: String
)

@Serializable
data class Files(
    val beatmap: String,
    val background: String,
    val audio: String
)

@Serializable
data class DirectPath(
    val beatmapFile: String,
    val beatmapBackground: String,
    val beatmapAudio: String,
    val beatmapFolder: String,
    val skinFolder: String
)

@Serializable
data class Tourney(
    val scoreVisible: Boolean,
    val starsVisible: Boolean,
    val ipcState: Int,
    val bestOF: Int,
    val team: TourneyTeamNames,
    val points: TourneyPoints,
    val chat: List<TourneyChatMessage> = emptyList(),
    val totalScore: TourneyPoints,
    val clients: List<TourneyClient> = emptyList()
)

@Serializable
data class TourneyTeamNames(
    val left: String,
    val right: String
)

@Serializable
data class TourneyPoints(
    val left: Long,
    val right: Long
)

@Serializable
data class TourneyChatMessage(
    val team: String = "",
    val name: String = "",
    val message: String = "",
    val timestamp: String = ""
)

@Serializable
data class TourneyClient(
    val team: String = "",
    val user: Profile? = null,
    val play: Play? = null
)