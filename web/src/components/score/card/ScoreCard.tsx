import { BeatmapEntry, MapStatus } from "~/classes/Map";
import "../../map/card/MapCard.css";
import "./ScoreCard.css";
import Container from "../../Container";
import MapStatusPill from "../../pill/status/MapStatusPill";
import StarPill from "../../pill/star/StarPill";
import { Score } from "~/classes/Score";
import Pill from "~/components/pill/Pill";
import { getStarRatingSoloTextColor, getStarRatingTextColor } from "~/classes/StarRating";
import { getScorePost } from "~/classes/ScorePost";

function ScoreCard(props: { map: BeatmapEntry, score: Score }) {
	const score = props.score
	const map = props.map

	const isMaxPP = props.score.pp == props.score.ppFc;

	console.log(getScorePost(props.score, props.map));

	return (
		<Container class="map-card score-card">
			<div
				class="background"
				style={`background-image: url(https://assets.ppy.sh/beatmaps/${map.setId}/covers/cover.jpg)`}
			/>

			<div class="content">
				<div class="row">
					<div class="left">
						<MapStatusPill status={map.status} />
						<StarPill
							starRating={map.starsTotal}
						/>

					  <div class="map-diff" style={`color:${getStarRatingSoloTextColor(map.starsTotal)}`}>
						  {map.version}
					  </div>
					</div>
				</div>

				<div class="columns">
				  <div class="map-info">
						<div class="map-title">
							{map.title}
						</div>
					  <div class="map-artist">
							{map.artist}
					  </div>
				  </div>

				  <div class="score-stats">
					  <div class="stat rank">
						  <span class={`stat-value rank rank-${score.rank}`}>
							  {score.rank}
						  </span>
						  <span class="stat-label">Rank</span>
					  </div>
					  
					  <div class="stat pp">
						  
						  {!isMaxPP ? (
							<span class="stat-max-value">
								{(score.ppFc ?? 0).toFixed(2)}
							</span>
						  ) : ``}

						  <span
							class={
							  "stat-value " +
							  (isMaxPP ? `perfect-value` : ``)
						  }>
							  {(score.pp ?? 0).toFixed(2)}
						  </span>
						  <span class="stat-label">PP</span>
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
						  <span class="stat-label">
							{score.countMiss != 1 ? `Misses` : `Miss`}
						  </span>
					  </div>
				  </div>
				</div>

				<div class="score-footer">
					{/* <div class="mods">
						{score.mods.length > 0
							? score.mods.join(" · ")
							: "NM"}
					</div> */}

					{/* <div class="played-at">
						{new Date(score.playedAt).toLocaleString()}
					</div> */}
				</div>
			</div>
		</Container>
	)
}

export default ScoreCard;