import "./StarPill.css";
import { getStarRatingColor } from "~/classes/StarRating";

function StarPill(props: { starRating: number }) {
    const color = getStarRatingColor(props.starRating)
    return (
        <div
            class={`star-pill`}
            style={`background-color: ${color}`}
        >
            ★{props.starRating}
        </div>
    )
}

export default StarPill;