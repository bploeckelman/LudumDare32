package lando.systems.ld32.story;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld32.Assets;
import lando.systems.ld32.Constants;
import lando.systems.ld32.GameInstance;
import lando.systems.ld32.narrative.NarrativeManager;
import lando.systems.ld32.narrative.NarrativePhrase;
import lando.systems.ld32.screens.FightScreen;

public class StoryManager {

    private enum Stage {
        INTRO,
        LIBRARY_TUTORIAL,
        FOREST,
        CAVE,
        DESERT,
        WATER,
        DUNGEON,
        TOWER,
        GAMEOVER
    }

    public GameInstance game;

    private NarrativeManager narrativeManager;
    private Stage stage;

    private int step;
    private float stepTimer;
    private NarrativePhrase stepPhrase;

    // -----------------------------------------------------------------------------------------------------------------


    public StoryManager() {

        this.narrativeManager = new NarrativeManager(0, 0, Constants.win_width, Constants.NARRATIVE_MANAGER_FIGHT_HEIGHT + 20);// Constants.win_height);
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
        ++step;
    }

    private void tellStory() {

        switch (stage) {

            case INTRO:

                switch (step) {

                    // Tell the story
                    case 0:
                        game.changeScreen(Constants.game_screen);
                        narrativeManager.addParagraph(new NarrativePhrase(
                                Assets.font16,
//                                "You walk into a library"
                                "You're an illiterate mud person who wants to write! "// are an illiterate mud person who wants to learn to write"
                        ));
                        String text = "Spell effects: hold SHIFT and type the spell BACKWARDS to counter..."
                                    + "Type words to reveal the enemy's fear, once revealed, type the fear word to defeat the enemy";
                        stepPhrase = narrativeManager.addPhraseToCurrentParagraph(new NarrativePhrase(text, 40));
//                        stepPhrase = narrativeManager.addParagraph(new NarrativePhrase(
//                                "woeinfwoienf"
//                        ));
                        nextStep();
                        break;
                    case 1:
                        WAIT_FOR_STEP_PHRASE_COMPLETE();
                        break;
                    case 2:
                        WAIT_FOR_NARRATIVE_TWEEN_COMPLETE();
                        break;
                    // Move the narrative to fight screen position
                    case 3:
                        narrativeManager.setHeight(Constants.NARRATIVE_MANAGER_FIGHT_HEIGHT, 2f);
                        narrativeManager.setY(Constants.NARRATIVE_MANAGER_FIGHT_Y, 2f);
                        nextStep();
                        break;
                    default:
                        nextStep();
                        if (step > 2) { goToStage(Stage.LIBRARY_TUTORIAL); }

                }

                break;

            case LIBRARY_TUTORIAL:
                switch (step) {
                    case 0:
                        FightScreen.enableAttackWords = true;
                        FightScreen.enableKillPhrase = true;
                        FightScreen.enableSpellWords = true;
                        FightScreen.fightComplete = false;
                        game.changeScreen(Constants.fight_screen);
                        nextStep();
                        break;
                    case 1:
                        WAIT_FOR_FIGHT_COMPLETE();
                        break;
                    default:
//                        nextStep();
//                        if (step > 1) {
//                            FightScreen.enableAttackWords = false;
//                            FightScreen.enableKillPhrase = false;
//                            FightScreen.enableSpellWords = false;
//                            FightScreen.fightComplete = false;
//                            goToStage(Stage.FOREST);
//                        }
//                        break;
                }
                break;

            /*
            case FOREST:
                switch (step) {
                    case 0:
                        // TODO: go to overworld at forest level, expository things, then transition to fight screen lvl 1
                        FightScreen.currentLevel = 1;
                        FightScreen.enableAttackWords = true;
                        FightScreen.enableKillPhrase = true;
                        FightScreen.enableSpellWords = true;
                        FightScreen.fightComplete = false;
                        game.changeScreen(Constants.fight_screen);
                        nextStep();
                        break;
                    case 1:
                        WAIT_FOR_FIGHT_COMPLETE();
                        break;
                    default:
                        nextStep();
                        if (step > 1) {
                            FightScreen.enableAttackWords = false;
                            FightScreen.enableKillPhrase = false;
                            FightScreen.enableSpellWords = false;
                            FightScreen.fightComplete = false;
                            goToStage(Stage.CAVE);
                        }
                        break;
                }
                break;

            case CAVE:
                switch (step) {
                    case 0:
                        // TODO: go to overworld at cave level, expository things, then transition to fight screen lvl 2
                        FightScreen.currentLevel = 2;
                        FightScreen.enableAttackWords = true;
                        FightScreen.enableKillPhrase = true;
                        FightScreen.enableSpellWords = true;
                        FightScreen.fightComplete = false;
                        game.changeScreen(Constants.fight_screen);
                        break;
                    case 1:
                        WAIT_FOR_FIGHT_COMPLETE();
                        break;
                    case 2:
                        FightScreen.enableAttackWords = false;
                        FightScreen.enableKillPhrase = false;
                        FightScreen.enableSpellWords = false;
                        FightScreen.fightComplete = false;
                        goToStage(Stage.WATER);
                        break;
                    default:
                        nextStep();
                        if (step > 1) {
                            FightScreen.enableAttackWords = false;
                            FightScreen.enableKillPhrase = false;
                            FightScreen.enableSpellWords = false;
                            FightScreen.fightComplete = false;
                            goToStage(Stage.WATER);
                        }
                        break;
                }
                break;

            case WATER:
                switch (step) {
                    case 0:
                        // TODO: go to overworld at water level, expository things, then transition to fight screen lvl
                        FightScreen.currentLevel = 3;
                        FightScreen.enableAttackWords = true;
                        FightScreen.enableKillPhrase = true;
                        FightScreen.enableSpellWords = true;
                        FightScreen.fightComplete = false;
                        game.changeScreen(Constants.fight_screen);
                        break;
                    case 1:
                        WAIT_FOR_FIGHT_COMPLETE();
                        break;
                    default:
                        nextStep();
                        if (step > 1) {
                            FightScreen.enableAttackWords = false;
                            FightScreen.enableKillPhrase = false;
                            FightScreen.enableSpellWords = false;
                            FightScreen.fightComplete = false;
                            goToStage(Stage.DESERT);
                        }
                        break;
                }
                break;

            case DESERT:
                switch (step) {
                    case 0:
                        // TODO: go to overworld at desert level, expository things, then transition to fight screen lvl
                        FightScreen.currentLevel = 4;
                        FightScreen.enableAttackWords = true;
                        FightScreen.enableKillPhrase = true;
                        FightScreen.enableSpellWords = true;
                        FightScreen.fightComplete = false;
                        game.changeScreen(Constants.fight_screen);
                        break;
                    case 1:
                        WAIT_FOR_FIGHT_COMPLETE();
                        break;
                    default:
                        nextStep();
                        if (step > 1) {
                            FightScreen.enableAttackWords = false;
                            FightScreen.enableKillPhrase = false;
                            FightScreen.enableSpellWords = false;
                            FightScreen.fightComplete = false;
                            goToStage(Stage.DUNGEON);
                        }
                        break;
                }
                break;

            case DUNGEON:
                switch (step) {
                    case 0:
                        // TODO: go to overworld at dungeon level, expository things, then transition to fight screen lvl
                        FightScreen.currentLevel = 5;
                        FightScreen.enableAttackWords = true;
                        FightScreen.enableKillPhrase = true;
                        FightScreen.enableSpellWords = true;
                        FightScreen.fightComplete = false;
                        game.changeScreen(Constants.fight_screen);
                        break;
                    case 1:
                        WAIT_FOR_FIGHT_COMPLETE();
                        break;
                    default:
                        nextStep();
                        if (step > 1) {
                            FightScreen.enableAttackWords = false;
                            FightScreen.enableKillPhrase = false;
                            FightScreen.enableSpellWords = false;
                            FightScreen.fightComplete = false;
                            goToStage(Stage.TOWER);
                        }
                        break;
                }
                break;

            case TOWER:
                switch (step) {
                    case 0:
                        // TODO: go to overworld at tower level, expository things, then transition to fight screen lvl
                        FightScreen.currentLevel = 6;
                        FightScreen.enableAttackWords = true;
                        FightScreen.enableKillPhrase = true;
                        FightScreen.enableSpellWords = true;
                        FightScreen.fightComplete = false;
                        game.changeScreen(Constants.fight_screen);
                        nextStep();
                        break;
                    case 1:
                        WAIT_FOR_FIGHT_COMPLETE();
                        break;
                    default:
                        nextStep();
                        if (step > 1) {
                            FightScreen.enableAttackWords = false;
                            FightScreen.enableKillPhrase = false;
                            FightScreen.enableSpellWords = false;
                            FightScreen.fightComplete = false;
                            goToStage(Stage.GAMEOVER);
                        }
                        break;
                }
                break;

            case GAMEOVER:
                switch (step) {
                    case 0:
                        game.changeScreen(Constants.game_over_screen);
                        break;
                    case 1:
                        WAIT(100000f);
                        break;
                }
                break;

*/
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
    public void WAIT_FOR_FIGHT_COMPLETE() {
        if (FightScreen.fightComplete) {
            nextStep();
        } else Gdx.app.log("WAIT", "not done");
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
