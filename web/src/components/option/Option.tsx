import { Match, Show, Switch } from "solid-js";
import { useOptions, type OptionId } from "./OptionsContext";

export function Option(props: { id: OptionId; label: string }) {
  const { options, setOption } = useOptions();
  const value = () => options[props.id];
  
  function label() {
    return (
        <span class="label">
            {props.label}
        </span>
    )
  }

  return (
    <Show when={value() !== undefined}>
      <Switch>
        <Match when={typeof value() === "boolean"}>
          <label>
            <input
              type="checkbox"
              checked={value() as boolean}
              onChange={(e) => setOption(props.id, e.currentTarget.checked as never)}
            />{" "}
            {label()}
          </label>
        </Match>

        <Match when={typeof value() === "number"}>
          <label>
            {label()}
            <input
              type="number"
              value={value() as unknown as number}
              onChange={(e) => setOption(props.id, Number(e.currentTarget.value) as never)}
            />
          </label>
        </Match>

        <Match when={true}>
          <label>
            {label()}
            <input
              type="text"
              value={String(value())}
              onChange={(e) => setOption(props.id, e.currentTarget.value as never)}
            />
          </label>
        </Match>
      </Switch>
    </Show>
  );
}