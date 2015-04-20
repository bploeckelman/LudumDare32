package lando.systems.ld32.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld32.Assets;
import lando.systems.ld32.spellwords.SpellWord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnemyFactory {

    private static Map<String, Config>   bosses;
    private static Map<String, String[]> killPhrases;
    private static List<String>          bossKeys;

    static {
        bosses = new HashMap<String, Config>();
        killPhrases = new HashMap<String, String[]>();
        bossKeys = new ArrayList<String>();

        //---------------------------------------
//        bosses.put("Fubar", new Config(
//            Assets.defaultEnemyAnimation2,
//            3,
//            new String[]{
//                "SMASH",
//                "BLARGH",
//                "GLOOP",
//                "FOOL",
//                "PANCAKES"
//            },
//            new SpellWord.Type[]{
//                SpellWord.Type.DARKNESS
//            }
//        ));
//        killPhrases.put("Fubar", new String[]{
//            "BACON PANCAKES",
//            "WORDS AND STUFF",
//            "I ATE IT ALL"
//        });
//
//        //---------------------------------------
//        bosses.put("Gozer", new Config(
//            Assets.defaultEnemyAnimation,
//            3,
//            new String[]{
//                "ROAR",
//                "BITE",
//                "STINK",
//                "TROUT SLAP",
//                "YOUR MOM"
//            },
//            new SpellWord.Type[]{
//                SpellWord.Type.QUIET
//            }
//        ));
//        killPhrases.put("Gozer", new String[]{
//            "NOT MY MEATBALLS",
//            "MY SHAMPOO",
//            "WHY ARE YOU HERE"
//        });

        //---------------------------------------
        bosses.put("Ogre", new Config(
                Assets.ogreAnimation,
                3,
                new String[]{
                        "SMASH",
                        "CRUSH",
                        "ROAR",
                        "SCRATCH",
                        "GROWL",
                        "GRUNT"
                },
                new SpellWord.Type[]{
                        SpellWord.Type.QUIET
                }
        ));
        killPhrases.put("Ogre", new String[]{
                "BACON PANCAKES",
                "NO UNDERWEAR",
                "MATTED FUR"
        });

        //---------------------------------------
        bosses.put("Bat", new Config(
                Assets.batAnimation,
                3,
                new String[]{
                        "FLAP",
                        "FLUTTER",
                        "SWOOP",
                        "SCRATCH",
                        "BLEED"
                },
                new SpellWord.Type[]{
                        SpellWord.Type.QUIET,
                        SpellWord.Type.DARKNESS
                }
        ));
        killPhrases.put("Bat", new String[]{
                "FANG CAVITIES",
                "BADLY TORN WINGS",
                "LOSS OF HEARING"
        });

        //---------------------------------------
        bosses.put("Scorpion", new Config(
                Assets.scorpionAnimation,
                3,
                new String[]{
                        "STAB",
                        "SKITTER",
                        "POISON",
                        "CREEP",
                        "CRAWL"
                },
                new SpellWord.Type[]{
                        SpellWord.Type.QUIET,
                        SpellWord.Type.DARKNESS,
                        SpellWord.Type.SANDSTORM
                }
        ));
        killPhrases.put("Scorpion", new String[]{
                "POISON LOSS",
                "BITTER COLD",
                "GIANT SCARY BOOTS"
        });

        //---------------------------------------
        bosses.put("WaterCube", new Config(
                Assets.waterCubeAnimation,
                3,
                new String[]{
                        "SPLASH",
                        "DROWN",
                        "DAMPEN",
                        "SUFFOCATE",
                        "MOISTEN"
                },
                new SpellWord.Type[]{
                        SpellWord.Type.QUIET,
                        SpellWord.Type.DARKNESS,
                        SpellWord.Type.SANDSTORM,
                        SpellWord.Type.SEASICKNESS
                }
        ));
        killPhrases.put("WaterCube", new String[]{
                "EVAPORATION",
                "GETTING DRUNK",
                "SUBLIMATION"
        });

        //---------------------------------------
        bosses.put("Beholder", new Config(
                Assets.beholderAnimation,
                3,
                new String[]{
                        "GAZE",
                        "STARE DOWN",
                        "WAGGLE",
                        "FLOAT",
                        "GLARE"
                },
                new SpellWord.Type[]{
                        SpellWord.Type.QUIET,
                        SpellWord.Type.DARKNESS,
                        SpellWord.Type.SANDSTORM,
                        SpellWord.Type.SEASICKNESS,
                        SpellWord.Type.CROSSEYED
                }
        ));
        killPhrases.put("Beholder", new String[]{
                "ASTIGMATISM",
                "CATARACTS",
                "MONOCLES"
        });

        //---------------------------------------
        bosses.put("Chicken", new Config(
                Assets.chickenAnimation,
                3,
                new String[]{
                        "PECK",
                        "STRUT",
                        "PREEN",
                        "CLUCK",
                        "FEATHER"
                },
                new SpellWord.Type[]{
                        SpellWord.Type.QUIET,
                        SpellWord.Type.DARKNESS,
                        SpellWord.Type.SANDSTORM,
                        SpellWord.Type.SEASICKNESS,
                        SpellWord.Type.CROSSEYED,
                        SpellWord.Type.VENTRILOQUISM
                }
        ));
        killPhrases.put("Chicken", new String[]{
                "CHOPPING BLOCK",
                "LOSING FEATHERS",
                "RUFFLED FEATHERS"
        });

        for (String name : bosses.keySet()) {
            bossKeys.add(name);
        }
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

//        String bossName;
//        final int num_enemies = 7;
//        switch (MathUtils.random(0, num_enemies - 1)) {
//            case 0:  bossName = "Librarian"; break;
//            case 1:  bossName = "Ogre";      break;
//            case 2:  bossName = "Bat";       break;
//            case 3:  bossName = "Scorpion";  break;
//            case 4:  bossName = "WaterCube"; break;
//            case 5:  bossName = "Beholder";  break;
//            case 6:  bossName = "Chicken";   break;
//            default: bossName = "Gozer";
//        }

        final String bossName = bossKeys.get(MathUtils.random(bossKeys.size() - 1));
        final String[] phrases = killPhrases.get(bossName);
        return createEnemy(bosses.get(bossName), phrases[MathUtils.random(0, phrases.length - 1)]);
    }
}
