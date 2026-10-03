import { BeatmapEntry } from "./Map"

export enum GameMode {
    STANDARD = 0,
    TAIKO = 1,
    CATCH = 2,
    MANIA = 3,
}

export interface Score {
    id: number
    beatmapId: number
    beatmapChecksum: string
    playedAt: number
    mods: string[]
    modRate: number
    score: number
    accuracy: number
    maxCombo: number
    rank: string
    pp: number | null
    ppFc: number | null
    count300: number
    count100: number
    count50: number
    countMiss: number
    unstableRate: number | null
    hitErrorArray: number[]
    misses: number[]
    isConvert: boolean
    mode: GameMode
}

export interface ScoreWithBeatmap extends Score {
    map: BeatmapEntry
}

interface ScoreApiResponse {
    beatmaps: BeatmapEntry[]
    scores: Score[]
}

export async function fetchScores(): Promise<ScoreWithBeatmap[]> {
    const res = await fetch("http://127.0.0.1:1727/api/v1/scores", {
        method: "QUERY",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ limit: 50 }),
    })

    if (!res.ok) {
        throw new Error(`Failed to fetch scores: ${res.status}`)
    }

    const json: ScoreApiResponse = await res.json()

    const beatmaps = new Map(
        json.beatmaps.map(map => [map.id, map])
    )

    return json.scores
        .map(score => {
            const map = beatmaps.get(score.beatmapId)

            if (!map) {
                return null
            }

            return {
                ...score,
                map,
            }
        })
        .filter((score): score is ScoreWithBeatmap => score !== null)
}
