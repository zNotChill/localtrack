import { BeatmapEntry, MapStatus } from "~/classes/Map";
import "../../map/card/MapCard.css";
import "./ScoreCard.css";
import Container from "../../Container";
import MapStatusPill from "../../pill/status/MapStatusPill";
import StarPill from "../../pill/star/StarPill";
import { Score } from "~/classes/Score";
import { getStarRatingSoloTextColor, getStarRatingTextColor } from "~/classes/StarRating";
import { useOption } from "~/components/option/OptionsContext";

function ScoreCard(props: { map: BeatmapEntry, score: Score }) {
	const score = props.score
	const map = props.map

	const showDecimals = useOption("showDecimalPPValues");
	const formatPP = (pp: number | null | undefined) =>
		showDecimals() === false
			? Math.round(pp ?? 0).toLocaleString()
			: (pp ?? 0).toFixed(2);

	const renderMapBackgrounds = useOption("renderMapBackgrounds");
	const mapBackground = () =>
		renderMapBackgrounds() === true
			? <div
				class="background"
				style={`background-image: url(https://assets.ppy.sh/beatmaps/${map.setId}/covers/cover.jpg)`}
			/> : <></>


	const isMaxPP = props.score.pp == props.score.ppFc;

	return (
		<Container class="map-card score-card">
			{mapBackground()}

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
								{formatPP(score.ppFc)}
							</span>
						) : ``}

						<span class={"stat-value " + (isMaxPP ? `perfect-value` : ``)}>
							{formatPP(score.pp)}
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