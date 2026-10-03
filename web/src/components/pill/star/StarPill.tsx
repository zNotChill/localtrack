import { GameMode } from "~/classes/Score";
import "./StarPill.css";
import { getStarRatingColor, getStarRatingTextColor } from "~/classes/StarRating";

function StarPill(props: { mode?: GameMode | null, starRating: number }) {
    const backgroundColor = getStarRatingColor(props.starRating)
    const textColor = getStarRatingTextColor(props.starRating)

    return (
        <div
            class="pill star-pill"
            style={`
                background-color: ${backgroundColor};
                color: ${textColor};
            `}
        >
            {props.mode != null && (
                <img src={`/icons/${props.mode.toString().toLowerCase()}.png`} />
            )}

            ★{props.starRating}
        </div>
    )
}

export default StarPill;