import { MapStatus } from "~/classes/Map";
import "../Pill.css";
import "./MapStatusPill.css";

function MapStatusPill(props: { status: MapStatus }) {
    return (
        <div class={`map-status-pill pill ${props.status}`}>
            {props.status}
        </div>
    )
}

export default MapStatusPill;