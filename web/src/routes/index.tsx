import Container from "~/components/Container";
import MapList from "~/components/map/MapList";
import ScoreList from "~/components/score/ScoreList";

export default function Home() {
  return (
    <main>
      <Container header={
        "Score List"
      }>
        <ScoreList/>
      </Container>
    </main>
  );
}
