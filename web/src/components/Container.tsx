import { JSX } from "solid-js";
import "./Container.css";

interface ContainerProps {
  class?: string;
  header?: JSX.Element;
  children: JSX.Element;
}

function Container(props: ContainerProps) {
  return (
    <div class={`container ${props.class}`}>
      {props.header && <div class="header">{props.header}</div>}
      <div class="body">{props.children}</div>
    </div>
  );
}


export default Container;