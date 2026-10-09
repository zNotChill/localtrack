import { JSX } from "solid-js";
import "./Container.css";

interface ContainerProps {
  class?: string;
  header?: JSX.Element;
  headerElements?: JSX.Element;
  children: JSX.Element;
}

function Container(props: ContainerProps) {
  return (
    <div class={`container ${props.class}`}>
      {props.header &&
        <div class="header">
          <div class="left">{props.header}</div>
          <div class="right">{props.headerElements}</div>
        </div>
      }
      <div class="body">{props.children}</div>
    </div>
  );
}


export default Container;