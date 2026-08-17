interface ColorStop {
  rating: number;
  color: [number, number, number];
}

const COLORS: ColorStop[] = [
  { rating: 0.1, color: [0xaa, 0xaa, 0xaa] },
  { rating: 0.1, color: [0x4e, 0xbf, 0xff] },
  { rating: 1.25, color: [0x4e, 0xbf, 0xff] },
  { rating: 2.0, color: [0x4f, 0xff, 0xd5] },
  { rating: 2.5, color: [0x7c, 0xff, 0x4f] },
  { rating: 3.3, color: [0xf6, 0xf5, 0x05] },
  { rating: 4.2, color: [0xff, 0x80, 0x68] },
  { rating: 4.9, color: [0xff, 0x4e, 0x6f] },
  { rating: 5.8, color: [0xc6, 0x4e, 0xff] },
  { rating: 6.7, color: [0x9d, 0x3c, 0xff] },
  { rating: 7.7, color: [0x64, 0x19, 0xff] },
  { rating: 9.0, color: [0x00, 0x00, 0x00] },
  { rating: 11.0, color: [0x00, 0x00, 0x00] },
];

function clamp(v: number, min: number, max: number): number {
  return Math.min(Math.max(v, min), max);
}

function lerp(a: number, b: number, t: number): number {
  return a + (b - a) * t;
}

export function getStarRatingColor(starRating: number): string {
  starRating = Math.max(0, starRating);

  let lower = COLORS[0];
  let upper = COLORS[COLORS.length - 1];

  if (starRating <= COLORS[0].rating) {
    lower = upper = COLORS[0];
  } else if (starRating >= COLORS[COLORS.length - 1].rating) {
    lower = upper = COLORS[COLORS.length - 1];
  } else {
    for (let i = 0; i < COLORS.length - 1; i++) {
      if (starRating >= COLORS[i].rating && starRating <= COLORS[i + 1].rating) {
        lower = COLORS[i];
        upper = COLORS[i + 1];
        break;
      }
    }
  }

  const range = upper.rating - lower.rating;
  const t = range === 0 ? 0 : clamp((starRating - lower.rating) / range, 0, 1);

  const r = Math.round(lerp(lower.color[0], upper.color[0], t));
  const g = Math.round(lerp(lower.color[1], upper.color[1], t));
  const b = Math.round(lerp(lower.color[2], upper.color[2], t));

  return "#" + [r, g, b].map((c) => clamp(c, 0, 255).toString(16).padStart(2, "0")).join("");
}