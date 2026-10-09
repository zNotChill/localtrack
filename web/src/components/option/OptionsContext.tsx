import {
  createContext,
  createMemo,
  createSignal,
  onMount,
  useContext,
  type JSX,
} from "solid-js";
import { createStore, reconcile } from "solid-js/store";

export interface WebsiteOptions {
  showDecimalPPValues: boolean;
  renderMapBackgrounds: boolean;
}

export type OptionId = keyof WebsiteOptions;

export const DEFAULT_OPTIONS: WebsiteOptions = {
  showDecimalPPValues: false,
  renderMapBackgrounds: true,
};

interface OptionsContextValue {
  options: WebsiteOptions;
  setOption: <K extends OptionId>(id: K, value: WebsiteOptions[K]) => void;
  dirty: () => boolean;
  pushing: () => boolean;
  pushOptions: () => Promise<void>;
  error: () => string | null;
}

const API_URL = "http://127.0.0.1:1727/api/v1/options";
const STORAGE_KEY = "localtrack:options";
const OptionsContext = createContext<OptionsContextValue>();

export function OptionsProvider(props: { children: JSX.Element }) {
  const [options, setOptions] = createStore<WebsiteOptions>({ ...DEFAULT_OPTIONS });
  const [serverOptions, setServerOptions] = createSignal<Partial<WebsiteOptions> | null>(null);
  const [pushing, setPushing] = createSignal(false);
  const [error, setError] = createSignal<string | null>(null);

  const ids = () => Object.keys(DEFAULT_OPTIONS) as OptionId[];

  const dirty = createMemo(() => {
    const server = serverOptions();
    if (!server) return false;
    return ids().some((id) => options[id] !== server[id]);
  });

  onMount(async () => {
    let hasLocal = false;
    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      if (raw) {
        setOptions(reconcile({ ...DEFAULT_OPTIONS, ...JSON.parse(raw) }));
        hasLocal = true;
      }
    } catch {}

    try {
      const res = await fetch(API_URL);
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const server = await res.json();
      setServerOptions(server);
      if (!hasLocal) setOptions(reconcile({ ...DEFAULT_OPTIONS, ...server }));
    } catch (e) {
      setError(e instanceof Error ? e.message : "Failed to load options from server");
    }
  });

  const setOption: OptionsContextValue["setOption"] = (id, value) => {
    setOptions(id, value);
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(options));
    } catch {
    }
  };

  const pushOptions = async () => {
    setPushing(true);
    setError(null);
    try {
      const server = serverOptions() ?? {};
      for (const id of ids().filter((id) => options[id] !== server[id])) {
        const res = await fetch(API_URL, {
          method: "PATCH",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ path: id, value: options[id] }),
        });
        if (!res.ok) throw new Error(await res.text());
        setServerOptions(await res.json());
      }
    } catch (e) {
      setError(e instanceof Error ? e.message : "Failed to push options");
    } finally {
      setPushing(false);
    }
  };

  return (
    <OptionsContext.Provider
      value={{ options, setOption, dirty, pushing, pushOptions, error }}
    >
      {props.children}
    </OptionsContext.Provider>
  );
}

export function useOptions() {
  const ctx = useContext(OptionsContext);
  if (!ctx) throw new Error("useOptions must be used inside <OptionsProvider>");
  return ctx;
}

export function useOption<K extends OptionId>(id: K) {
  const { options } = useOptions();
  return () => options[id];
}