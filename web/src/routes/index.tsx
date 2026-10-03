import Container from "~/components/Container";
import MapList from "~/components/map/MapList";
import ScoreList from "~/components/score/ScoreList";

export default function Home() {
  return (
    <main>
      <Container header={
        "Profile Snapshots"
      }>
        <div>hi</div>
      </Container>
      <Container header={
        "Map List"
      }>
        <MapList/>
      </Container>
      <Container header={
        "Score List"
      }>
        <ScoreList/>
      </Container>
    </main>
  );
}
