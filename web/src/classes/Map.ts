export interface BeatmapEntry {
  id: number;
  setId: number;
  checksum: string;
  artist: string;
  artistUnicode: string;
  title: string;
  titleUnicode: string;
  mapper: string;
  version: string;
  source: string;
  tags: string;
  status: MapStatus;
  starsTotal: number;
  starsAim: number;
  starsSpeed: number;
  ar: number;
  cs: number;
  od: number;
  hp: number;
  bpm: number;
  circles: number;
  sliders: number;
  spinners: number;
  maxCombo: number;
  mp3Length: number;
  firstSeenAt: number;
  lastSeenAt: number;
}

export enum MapStatus {
  WIP = "wip",
  GRAVEYARD = "graveyard",
  RANKED = "ranked",
  LOVED = "loved",
  UNSUBMITTED = "notsubmitted",
  QUALIFIED = "qualified",
  PENDING = "pending"
}

interface MapApiResponse {
  type: string;
  message: string | null;
  success: boolean;
  details: BeatmapEntry[];
}

export async function fetchMaps(): Promise<BeatmapEntry[]> {
  console.log("HI")
  const res = await fetch("http://127.0.0.1:1727/api/v1/map", {
    method: "QUERY",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ limit: 50 }),
  });

  if (!res.ok) {
    throw new Error(`Failed to fetch songs: ${res.status}`);
  }

  const json: MapApiResponse = await res.json();

  if (!json.success) {
    throw new Error(json.message ?? "API returned failure");
  }

  return json.details;
}