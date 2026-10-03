import { fetchMaps } from "~/classes/Map";
import { createResource, For, Show } from "solid-js";
import MapCard from "./card/MapCard";

function MapList() {
  const [maps] = createResource(fetchMaps);

  return (
      <Show when={!maps.loading} fallback={<p>Loading...</p>}>
        <Show when={!maps.error} fallback={<p>Error: {maps.error.message}</p>}>
          <For each={maps()}>
            {(map) => <MapCard map={map} />}
          </For>
        </Show>
      </Show>
  );
}

export default MapList;