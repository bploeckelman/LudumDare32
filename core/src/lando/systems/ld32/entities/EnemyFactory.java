package lando.systems.ld32.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld32.Assets;

import java.util.HashMap;
import java.util.Map;

public class EnemyFactory {

    private static Map<String, Config> bosses;
    private static Map<String, String[]> killPhrases;

    static {
        bosses = new HashMap<String, Config>();
        killPhrases = new HashMap<String, String[]>();

        bosses.put("Fubar", new Config(
                Assets.defaultEnemyAnimation2,
                3,
                new String[]{"SMASH", "BLARGH", "GLOOP", "FOOLISH MORTAL", "PANCAKES"}));

        killPhrases.put("Fubar", new String[]{
                "BACON PANCAKES",
                "WORDS AND STUFF",
                "I ATE IT ALL"
        });

        bosses.put("Gozer", new Config(
            Assets.defaultEnemyAnimation,
            3,
            new String[]{"ROAR", "BITE", "STINK", "TROUT SLAP", "YOUR MOM"}));

        killPhrases.put("Gozer", new String[]{
            "NOT MY MEATBALLS",
            "MY SHAMPOO",
            "WHY ARE YOU HERE"
        });
    }

    private static class Config {
        Animation animation;
        float attackDelay;
        String[] attackDictionary;

        public Config(Animation animation, float attackDelay, String[] attackDictionary) {
            this.attackDelay = attackDelay;
            this.animation = animation;
            this.attackDictionary = attackDictionary;
        }
    }

    private static Enemy createEnemy(BitmapFont font, Config config, String killPhrase) {
        return new Enemy(font, config.animation, config.attackDelay, config.attackDictionary, killPhrase);
    }

    public static Enemy getBoss(BitmapFont font, int level) {
        if (MathUtils.randomBoolean()) {
            String[] phrases = killPhrases.get("Gozer");
            return createEnemy(font, bosses.get("Gozer"), phrases[MathUtils.random(0, phrases.length - 1)]);
        } else {
            String[] phrases = killPhrases.get("Fubar");
            return createEnemy(font, bosses.get("Fubar"), phrases[MathUtils.random(0, phrases.length - 1)]);
        }
    }
}
