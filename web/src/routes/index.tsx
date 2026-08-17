import Container from "~/components/Container";
import MapList from "~/components/map/MapList";

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
    </main>
  );
}
