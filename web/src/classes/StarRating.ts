interface ColorStop {
    rating: number
    color: [number, number, number]
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
]

const TEXT_COLORS: ColorStop[] = [
    { rating: 9.0, color: [0xf6, 0xf0, 0x5c] },
    { rating: 9.9, color: [0xff, 0x80, 0x68] },
    { rating: 10.6, color: [0xff, 0x4e, 0x6f] },
    { rating: 11.5, color: [0xc6, 0x45, 0xb8] },
    { rating: 12.4, color: [0x65, 0x63, 0xde] },
]

function clamp(v: number, min: number, max: number): number {
    return Math.min(Math.max(v, min), max)
}

function lerp(a: number, b: number, t: number): number {
    return a + (b - a) * t
}

function sampleGradient(
    colors: ColorStop[],
    rating: number
): [number, number, number] {
    let lower = colors[0]
    let upper = colors[colors.length - 1]

    if (rating <= colors[0].rating) {
        lower = upper = colors[0]
    } else if (rating >= colors[colors.length - 1].rating) {
        lower = upper = colors[colors.length - 1]
    } else {
        for (let i = 0; i < colors.length - 1; i++) {
            if (
                rating >= colors[i].rating &&
                rating <= colors[i + 1].rating
            ) {
                lower = colors[i]
                upper = colors[i + 1]
                break
            }
        }
    }

    const range = upper.rating - lower.rating
    const t = range === 0
        ? 0
        : clamp((rating - lower.rating) / range, 0, 1)

    return [
        Math.round(lerp(lower.color[0], upper.color[0], t)),
        Math.round(lerp(lower.color[1], upper.color[1], t)),
        Math.round(lerp(lower.color[2], upper.color[2], t)),
    ]
}

function toHex(color: [number, number, number]): string {
    return "#" + color
        .map(c => clamp(c, 0, 255).toString(16).padStart(2, "0"))
        .join("")
}

export function getStarRatingColor(starRating: number): string {
    return toHex(sampleGradient(COLORS, Math.max(0, starRating)))
}

export function getStarRatingTextColor(starRating: number): string {
    starRating = Math.max(0, starRating)

    if (starRating < 6.5) {
        return "rgba(0, 0, 0, 0.75)"
    }

    if (starRating < 9.0) {
        return "#ffd966"
    }

    return toHex(sampleGradient(TEXT_COLORS, starRating))
}