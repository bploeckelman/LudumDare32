package lando.systems.ld32;

public class Statistics {

    private static int[] statNums;

    public static int numLettersTyped     = 0;
    public static int numAttackWordsFired = 0;
    public static int numWordsDefended    = 0;
    public static int numWordsHit         = 0;
    public static int numSpellsCast       = 0;
    public static int numSpellsCountered  = 0;
    public static int numFearsUncovered   = 0;
    public static int numStaggers         = 0;

    public static float playTime = 0;
    public static String playTimeStr = "Play Time:";

    public static final String numLettersTypedStr     = "Letters Filled:";
    public static final String numAttackWordsFiredStr = "Words Encountered:";
    public static final String numWordsDefendedStr    = "Words Defended:";
    public static final String numWordsHitStr         = "Words Hit:";
    public static final String numSpellsCastStr       = "Spells Cast:";
    public static final String numSpellsCounteredStr  = "Spells Countered:";
    public static final String numFearsUncoveredStr   = "Fears Uncovered:";
    public static final String numStaggersStr         = "Staggers:";

    public static String[] getStatStrings() {
        return new String[]{
            numLettersTypedStr
            ,numAttackWordsFiredStr
            ,numWordsDefendedStr
            ,numWordsHitStr
            ,numSpellsCastStr
            ,numSpellsCounteredStr
            ,numFearsUncoveredStr
            ,numStaggersStr
        };
    }

    public static int[] getStatNums() {
        if(statNums == null) {
            statNums = new int[]{
                numLettersTyped
                , numAttackWordsFired
                , numWordsDefended
                , numWordsHit
                , numSpellsCast
                , numSpellsCountered
                , numFearsUncovered
                , numStaggers
            };
        }

        return statNums;
    }
}
