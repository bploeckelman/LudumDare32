package lando.systems.ld32.story;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld32.Assets;
import lando.systems.ld32.Constants;
import lando.systems.ld32.narrative.NarrativeManager;
import lando.systems.ld32.narrative.NarrativePhrase;

public class StoryManager {

    private enum Stage {
        INTRO,
        LIBRARY_TUTORIAL
    }

    private NarrativeManager narrativeManager;
    private Stage stage;

    private int step;
    private float stepTimer;
    private NarrativePhrase stepPhrase;

    // -----------------------------------------------------------------------------------------------------------------


    public StoryManager() {

        this.narrativeManager = new NarrativeManager(0, 0, Constants.win_width, Constants.win_height/3);
        goToStage(Stage.INTRO);

    }

    // -----------------------------------------------------------------------------------------------------------------


    private void goToStage(Stage stage) {
        this.stage = stage;
        goToStep(0);
    }
    private void goToStep(int step) {
        this.step = step;
        this.stepTimer = 0;
        this.stepPhrase = null;
    }

    private void nextStep() {
        step++;
    }

    private void tellStory() {

        switch (stage) {

            case INTRO:

                switch (step) {

                    case 0:
                        stepPhrase = narrativeManager.addParagraph(new NarrativePhrase(Assets.font16,
                                "A dude walks into a library.  Stuff happens."
                        ));
                        nextStep();
                        break;

                    case 1:
                        WAIT_FOR_STEP_PHRASE_COMPLETE();
                        break;

                    case 2:
                        narrativeManager.setHeight(Constants.win_height, 2f);
                        nextStep();
                        break;

                    default:
                        nextStep();
                        if (step > 2) { goToStage(Stage.LIBRARY_TUTORIAL); }

                }

                break;

            case LIBRARY_TUTORIAL:

                break;

        }

    }

    // -----------------------------------------------------------------------------------------------------------------

    public void WAIT_FOR_STEP_PHRASE_COMPLETE() {
        if (stepPhrase.isComplete()) { nextStep(); }
    }


    // -----------------------------------------------------------------------------------------------------------------

    public void render(SpriteBatch batch) {
        update(Gdx.graphics.getDeltaTime());
        narrativeManager.render(batch);
    }

    public void update(float delta) {
        this.stepTimer += delta;
        tellStory();
        narrativeManager.update(delta);
    }

}
