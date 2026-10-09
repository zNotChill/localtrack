import Container from "~/components/Container";
import Dropdown from "~/components/dropdown/Dropdown";
import MapList from "~/components/map/MapList";
import { Option } from "~/components/option/Option";
import { OptionsProvider } from "~/components/option/OptionsContext";
import { PushOptionsButton } from "~/components/option/PushOptionsButton";
import ScoreList from "~/components/score/ScoreList";

export default function Home() {
  return (
    <OptionsProvider>
      <main>
        <Container
          header={"Score List"}
          headerElements={
           <Dropdown label="Options" align="right">
              <Option id="showDecimalPPValues" label="Show Decimal PP Values" />
              <Option id="renderMapBackgrounds" label="Render Map Backgrounds" />
              <PushOptionsButton />
            </Dropdown>
          }
        >
          <ScoreList/>
        </Container>
      </main>
    </OptionsProvider>
  );
}
