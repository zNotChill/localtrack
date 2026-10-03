import { JSX } from "solid-js";
import "./Pill.css";

export interface PillProps {
    backgroundColor?: string;
    textColor?: string;
    children?: JSX.Element;
}

function Pill(props: PillProps) {
    return (
        <div
            class="pill"
            style={`
                background-color: ${props.backgroundColor};
                color: ${props.textColor};
            `}
        >
            {props.children}
        </div>
    )
}

export default Pill;