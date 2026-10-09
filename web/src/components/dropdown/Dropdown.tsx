import { createSignal, onCleanup, onMount, Show, type JSX } from "solid-js";
import "./Dropdown.css";

interface DropdownProps {
  label: string;
  children: JSX.Element;
  align?: "left" | "right";
}

export default function Dropdown(props: DropdownProps) {
  const [open, setOpen] = createSignal(false);
  let root: HTMLDivElement | undefined;

  const onPointerDown = (e: PointerEvent) => {
    if (root && !root.contains(e.target as Node)) setOpen(false);
  };
  const onKeyDown = (e: KeyboardEvent) => {
    if (e.key === "Escape") setOpen(false);
  };

  onMount(() => {
    document.addEventListener("pointerdown", onPointerDown);
    document.addEventListener("keydown", onKeyDown);

    onCleanup(() => {
        document.removeEventListener("pointerdown", onPointerDown);
        document.removeEventListener("keydown", onKeyDown);
    });
    });

  return (
    <div class="dropdown" ref={root}>
      <button
        type="button"
        class="dropdown-toggle"
        aria-haspopup="true"
        aria-expanded={open()}
        onClick={() => setOpen((o) => !o)}
      >
        {props.label}
        <svg
            class="dropdown-arrow"
            classList={{ open: open() }}
            viewBox="0 0 12 8"
            width="12"
            height="8"
            aria-hidden="true"
        >
            <path
                d="M1 1.5 L6 6.5 L11 1.5"
                fill="none"
                stroke="currentColor"
                stroke-width="1.8"
                stroke-linecap="round"
                stroke-linejoin="round"
            />
        </svg>
      </button>

      <Show when={open()}>
        <div class="dropdown-panel" classList={{ right: props.align === "right" }}>
          {props.children}
        </div>
      </Show>
    </div>
  );
}