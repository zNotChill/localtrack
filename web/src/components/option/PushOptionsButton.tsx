import { Show } from "solid-js";
import { useOptions } from "./OptionsContext";

export function PushOptionsButton() {
  const { dirty, pushing, pushOptions, error } = useOptions();

  return (
    <>
      <button
        type="button"
        class="dropdown-toggle"
        disabled={!dirty() || pushing()}
        onClick={pushOptions}
      >
        {pushing() ? "Pushing..." : dirty() ? "Push to server" : "Synced with server"}
      </button>
      <Show when={error()}>
        <span style={{ color: "#ff6b6b" }}>{error()}</span>
      </Show>
    </>
  );
}