import { MapStatus } from "~/classes/Map";
import "./MapStatusPill.css";

function MapStatusPill(props: { status: MapStatus }) {
    return (
        <div class={`map-status-pill ${props.status}`}>
            {props.status}
        </div>
    )
}

export default MapStatusPill;