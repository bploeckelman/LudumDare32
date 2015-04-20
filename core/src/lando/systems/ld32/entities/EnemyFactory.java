package lando.systems.ld32.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld32.Assets;
import lando.systems.ld32.spellwords.SpellWord;

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
            new String[]{
                "SMASH",
                "BLARGH",
                "GLOOP",
                "FOOL",
                "PANCAKES"
            },
            new SpellWord.Type[]{
                SpellWord.Type.DARKNESS
            }
        ));

        killPhrases.put("Fubar", new String[]{
            "BACON PANCAKES",
            "WORDS AND STUFF",
            "I ATE IT ALL"
        });

        bosses.put("Gozer", new Config(
            Assets.defaultEnemyAnimation,
            3,
            new String[]{
                "ROAR",
                "BITE",
                "STINK",
                "TROUT SLAP",
                "YOUR MOM"
            },
            new SpellWord.Type[]{
                SpellWord.Type.SILENCE
            }
        ));

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
        SpellWord.Type[] spellDictionary;

        public Config(
            Animation animation,
            float attackDelay,
            String[] attackDictionary,
            SpellWord.Type[] spellDictionary)
        {
            this.attackDelay = attackDelay;
            this.animation = animation;
            this.attackDictionary = attackDictionary;
            this.spellDictionary = spellDictionary;
        }
    }

    private static Enemy createEnemy(Config config, String killPhrase) {
        return new Enemy(config.animation, config.attackDelay, config.attackDictionary, config.spellDictionary, killPhrase);
}

    public static Enemy getBoss(int level) {
        if (MathUtils.randomBoolean()) {
            String[] phrases = killPhrases.get("Gozer");
            return createEnemy(bosses.get("Gozer"), phrases[MathUtils.random(0, phrases.length - 1)]);
        } else {
            String[] phrases = killPhrases.get("Fubar");
            return createEnemy(bosses.get("Fubar"), phrases[MathUtils.random(0, phrases.length - 1)]);
        }
    }
}
