import { createResource, For, Show } from "solid-js";
import { fetchScores } from "~/classes/Score";
import ScoreCard from "./card/ScoreCard";

function ScoreList() {
    const [scores] = createResource(fetchScores);

    return (
        <Show
            when={!scores.loading}
            fallback={<p>Loading...</p>}
        >
            <Show
                when={!scores.error}
                fallback={<p>Error: {scores.error.message}</p>}
            >
                <div class="card-grid">
                    <For each={scores()}>
                        {(score) => (
                            <ScoreCard
                                score={score}
                                map={score.map}
                            />
                        )}
                    </For>
                </div>
            </Show>
        </Show>
    );
}

export default ScoreList;