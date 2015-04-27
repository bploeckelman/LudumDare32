package lando.systems.ld32.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
        bosses.put("Librarian", new Config(
                Assets.librarianAnimation,
                3,
                Assets.libraryBackgroundRegion,
                new String[]{
                        "SHUSH",
                        "READ",
                        "LEARN",
                        "STUDY",
                        "RESEARCH",
                        "PERUSE",
                        "KNOW",
                        "DISCOVER",
                        "SKIM",
                        "REVIEW"
                },
                new SpellWord.Type[]{
                        SpellWord.Type.SHHH
                }
        ));
        killPhrases.put("Librarian", new String[]{
                "DOG EAR PAGE",
                "THE INTERNET",
                "IGNORANCE",
                "ILLITERACY",
                "INDIFFERENCE",
                "PUBLISHERS"
        });

        //---------------------------------------
        bosses.put("Ogre", new Config(
                Assets.ogreAnimation,
                3,
                Assets.forestBackgroundRegion,
                new String[]{
                        "SMASH",
                        "CRUSH",
                        "ROAR",
                        "SCRATCH",
                        "GROWL",
                        "GRUNT",
                        "BELLOW",
                        "BARK",
                        "GRUMBLE",
                        "BRUISE",
                        "SQUEEZE",
                        "SMUSH",
                        "TRAMPLE",
                        "QUASH",
                        "CRASH",
                        "GRUMBLE"
                },
                new SpellWord.Type[]{
                        SpellWord.Type.QUIET
                }
        ));
        killPhrases.put("Ogre", new String[]{
                "BACON PANCAKES",
                "NO UNDERWEAR",
                "MATTED FUR",
                "A HOT SHOWER",
                "CIVILIZATION",
                "PAVED PARKS",
                "EDUCATION"
        });

        //---------------------------------------
        bosses.put("Bat", new Config(
                Assets.batAnimation,
                3,
                Assets.caveBackgroundRegion,
                new String[]{
                        "FLAP",
                        "FLUTTER",
                        "SWOOP",
                        "SCRATCH",
                        "BLEED",
                        "HANG",
                        "SWING",
                        "THRASH",
                        "FLIT AROUND",
                        "HOVER",
                        "CLAW",
                        "GRAZE",
                        "LACERATE",
                        "LEECH",
                        "CHEW",
                        "CHOMP",
                        "NIBBLE"
                },
                new SpellWord.Type[]{
                        SpellWord.Type.QUIET,
                        SpellWord.Type.DARKNESS
                }
        ));
        killPhrases.put("Bat", new String[]{
                "FANG CAVITIES",
                "BADLY TORN WINGS",
                "LOSS OF HEARING",
                "BRIGHT SUNLIGHT",
                "SUDDEN VAMPIRISM",
                "CAVE EXPLORERS"
        });

        //---------------------------------------
        bosses.put("Scorpion", new Config(
                Assets.scorpionAnimation,
                3,
                Assets.desertBackgroundRegion,
                new String[]{
                        "STAB",
                        "SKITTER",
                        "POISON",
                        "CREEP",
                        "CRAWL",
                        "SCAMPER",
                        "JAB",
                        "WOUND",
                        "HURT",
                        "SLINK",
                        "SNEAK",
                        "WRIGGLE",
                        "PROD",
                        "STICK",
                        "POKE"
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
                "GIANT BOOTS",
                "WICKED OWLS",
                "HUNGRY CAVE BATS",
                "OTHER SCORPIONS"
        });

        //---------------------------------------
        bosses.put("WaterCube", new Config(
                Assets.waterCubeAnimation,
                3,
                Assets.waterBackgroundRegion,
                new String[]{
                        "SPLASH",
                        "DROWN",
                        "DAMPEN",
                        "SUFFOCATE",
                        "MOISTEN",
                        "DRENCH",
                        "RINSE",
                        "SOAK",
                        "DOUSE",
                        "SLOSH",
                        "SPATTER",
                        "SPRAY",
                        "HUMIDIFY",
                        "RINSE",
                        "FLOOD",
                        "IMMERSE",
                        "ASPHYXIATE"
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
                "SUBLIMATION",
                "DEHYDRATION",
                "FREEZING COLD",
                "VAPORIZATION",
                "BOILING HEAT",
                "DISSIPATION",
                "ENDLESS DROUGHT"
        });

        //---------------------------------------
        bosses.put("Beholder", new Config(
                Assets.beholderAnimation,
                3,
                Assets.dungeonBackgroundRegion,
                new String[]{
                        "GAZE",
                        "STARE DOWN",
                        "WAGGLE",
                        "FLOAT",
                        "GLARE",
                        "WATCH",
                        "EVIL EYE",
                        "EXAMINE",
                        "OBSERVE",
                        "SCRUTINIZE",
                        "GLIMPSE",
                        "PEEK",
                        "HOSTILE STARE",
                        "SUFFER",
                        "SCOURGE",
                        "PLAGUE",
                        "TORMENT",
                        "AFFLICT",
                        "GLOWER",
                        "SCOWL"
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
                "MONOCLES",
                "DEPTH PERCEPTION",
                "COLOR BLINDNESS",
                "GLAUCOMA",
                "SHARP OBJECTS"
        });

        //---------------------------------------
        bosses.put("Chicken", new Config(
                Assets.chickenAnimation,
                3,
                Assets.towerBackgroundRegion,
                new String[]{
                        "PECK",
                        "STRUT",
                        "PREEN",
                        "CLUCK",
                        "FEATHER",
                        "SWAGGER",
                        "PUFF UP",
                        "SPREAD WINGS",
                        "HEAD BOB",
                        "LAY EGG",
                        "SCRATCH",
                        "FEED",
                        "BIRD FIGHT",
                        "BROODING",
                        "FLOCK",
                        "WARNING CALL",

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
                "RUFFLED FEATHERS",
                "FACTORY FARMS",
                "POULTRY FARMING",
                "NASTY PARASITES",
                "AVIAN INFLUENZA"
        });

        for (String name : bosses.keySet()) {
            bossKeys.add(name);
        }
    }

    private static class Config {
        Animation animation;
        float attackDelay;
        TextureRegion backgroundRegion;
        String[] attackDictionary;
        SpellWord.Type[] spellDictionary;

        public Config(
            Animation animation,
            float attackDelay,
            TextureRegion backgroundRegion,
            String[] attackDictionary,
            SpellWord.Type[] spellDictionary)
        {
            this.attackDelay = attackDelay;
            this.animation = animation;
            this.backgroundRegion = backgroundRegion;
            this.attackDictionary = attackDictionary;
            this.spellDictionary = spellDictionary;
        }
    }

    private static Enemy createEnemy(Config config, String killPhrase) {
        Enemy enemy = new Enemy(config.animation,
                         config.attackDelay,
                         config.attackDictionary,
                         config.spellDictionary,
                         killPhrase);
        enemy.backgroundRegion = config.backgroundRegion;
        return enemy;
    }

    public static Enemy getBoss(int level) {

        String bossName;
//        final int num_enemies = 7;
//        switch (MathUtils.random(0, num_enemies - 1)) {
        switch (level) {
            case 0:  bossName = "Librarian"; break;
            case 1:  bossName = "Ogre";      break;
            case 2:  bossName = "Bat";       break;
            case 3:  bossName = "Scorpion";  break;
            case 4:  bossName = "WaterCube"; break;
            case 5:  bossName = "Beholder";  break;
            case 6:  bossName = "Chicken";   break;
            default: bossName = "Gozer";
        }

//        final String bossName = bossKeys.get(MathUtils.random(bossKeys.size() - 1));
        final String[] phrases = killPhrases.get(bossName);
        return createEnemy(bosses.get(bossName), phrases[MathUtils.random(0, phrases.length - 1)]);
    }
}
