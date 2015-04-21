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

        this.narrativeManager = new NarrativeManager(0, 0, Constants.win_width, Constants.win_height);
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

                    // Tell the story
                    case 0:
                        narrativeManager.addParagraph(new NarrativePhrase(Assets.font16,
                                "You walk into a library"
                        ));
                        stepPhrase = narrativeManager.addPhraseToCurrentParagraph(new NarrativePhrase("...  ", 3));
//                        stepPhrase = narrativeManager.addParagraph(new NarrativePhrase(
//                                "woeinfwoienf"
//                        ));
                        nextStep();
                        break;
                    case 1:
                        WAIT_FOR_STEP_PHRASE_COMPLETE();
                        break;

                    // Move the narrative to fight screen position
                    case 2:
                        narrativeManager.setHeight(Constants.NARRATIVE_MANAGER_FIGHT_HEIGHT, 2f);
                        narrativeManager.setY(Constants.NARRATIVE_MANAGER_FIGHT_Y, 2f);
                        nextStep();
                        break;
                    case 3:
                        WAIT_FOR_NARRATIVE_TWEEN_COMPLETE();
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

    public void WAIT(float duration) {
        if (stepTimer >= duration) { nextStep(); }
    }
    public void WAIT_FOR_STEP_PHRASE_COMPLETE() {
        if (stepPhrase.isComplete()) { nextStep(); }
    }
    public void WAIT_FOR_NARRATIVE_TWEEN_COMPLETE() {
        if (!narrativeManager.isTweenInProgress()) { nextStep(); }
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
