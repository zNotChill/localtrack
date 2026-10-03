import { BeatmapEntry } from "./Map";
import { Score } from "./Score";

export function getScorePost(score: Score, beatmap: BeatmapEntry): string {
    let modCombo = "";
    if (score.mods.number != 0)
        modCombo = "+" + score.mods.name

    let missLabel = "";
    let ppLabel = `${(score.pp ?? 0).toFixed(0)}pp`
    if (score.countMiss > 0) {
        missLabel = `${score.countMiss}xMiss`
        ppLabel += ` (${score.ppFc?.toFixed(0)}pp if FC)`
    }
    return `${score.player} | ${beatmap.artist} - ${beatmap.title} [${beatmap.version}] ${modCombo} (${beatmap.mapper}, ${beatmap.starsTotal}*) ${score.accuracy.toFixed(2)}% ${score.maxCombo}/${score.maxComboAchievable}x ${missLabel} | ${ppLabel}`
}