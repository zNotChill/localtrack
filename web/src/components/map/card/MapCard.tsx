import { BeatmapEntry, MapStatus } from "~/classes/Map";
import "./MapCard.css";
import Container from "../../Container";
import MapStatusPill from "../../pill/status/MapStatusPill";
import StarPill from "../../pill/star/StarPill";

function MapCard(props: { map: BeatmapEntry }) {
  return (
    <Container class="map-card">
      <div
        class="background"
        style={`background-image:url(https://assets.ppy.sh/beatmaps/${props.map.setId}/covers/cover.jpg)`}
      />
      <div class="content">
        <div class="row">
          <MapStatusPill status={props.map.status}/>
          <StarPill starRating={props.map.starsTotal}/>
        </div>
        <div class="grid">
          <div class="map-info">
            <div class="map-artist">
              {props.map.artist}
            </div>
            <div class="map-title">
              {props.map.title}
            </div>
          </div>
          <div class="map-stats">
          </div>
        </div>
      </div>
    </Container>
  );
}

export default MapCard;