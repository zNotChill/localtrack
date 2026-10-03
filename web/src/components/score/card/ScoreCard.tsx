import { BeatmapEntry, MapStatus } from "~/classes/Map";
import "../../map/card/MapCard.css";
import "./ScoreCard.css";
import Container from "../../Container";
import MapStatusPill from "../../pill/status/MapStatusPill";
import StarPill from "../../pill/star/StarPill";
import { Score } from "~/classes/Score";

function ScoreCard(props: { map: BeatmapEntry, score: Score }) {
    const score = props.score
    const map = props.map

    return (
        <Container class="map-card score-card">
            <div
                class="background"
                style={`background-image: url(https://assets.ppy.sh/beatmaps/${map.setId}/covers/cover.jpg)`}
            />

            <div class="content">
                <div class="row">
                    <MapStatusPill status={map.status} />

                    <StarPill
                        // mode={score.mode}
                        starRating={map.starsTotal}
                    />

                    {/* <div class="score-pp">
                        {score.pp != null
                            ? `${score.pp.toFixed(2)}pp`
                            : "—"}
                    </div> */}
                </div>

                <div class="columns">
                  <div class="map-info">
                      <div class="map-artist">
                          {map.artist}
                      </div>

                      <div class="map-title">
                          {map.title}
                      </div>
                  </div>

                  <div class="score-stats">
                      <div class="stat rank">
                          <span class={`stat-value rank rank-${score.rank}`}>
                              {score.rank}
                          </span>
                          <span class="stat-label">Rank</span>
                      </div>

                      <div class="stat accuracy">
                          <span
                            class={
                              "stat-value " +
                              (score.accuracy == 100.0 ? `perfect-value` : ``)
                          }>
                              {score.accuracy.toFixed(2)}%
                          </span>
                          <span class="stat-label">Accuracy</span>
                      </div>

                      <div class="stat score">
                          <span
                            class={
                              "stat-value " +
                              (score.score >= 1_000_000.0 ? `perfect-value` : ``)
                          }>
                              {score.score.toLocaleString()}
                          </span>
                          <span class="stat-label">Score</span>
                      </div>

                      <div class="stat combo">
                          <span
                            class={
                              "stat-value " +
                              (score.maxCombo == score.maxComboAchievable ? `perfect-value` : ``)
                          }>
                              {score.maxCombo}x
                          </span>
                          <span class="stat-label">Combo</span>
                      </div>

                      <div class="stat misses">
                          <span
                            class={
                              "stat-value " +
                              (score.countMiss === 0 ? `perfect-value` : ``)
                          }>
                              {score.countMiss.toLocaleString()}
                          </span>
                          <span class="stat-label">Misses</span>
                      </div>
                  </div>
                </div>

                <div class="score-footer">
                    <div class="mods">
                        {score.mods.length > 0
                            ? score.mods.join(" · ")
                            : "NM"}
                    </div>

                    <div class="played-at">
                        {new Date(score.playedAt).toLocaleString()}
                    </div>
                </div>
            </div>
        </Container>
    )
}

export default ScoreCard;