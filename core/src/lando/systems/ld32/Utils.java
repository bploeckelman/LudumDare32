package lando.systems.ld32;

public class Utils {

    /**
     * @param stringArray The array to join
     * @param startIndex The first index to use for the join (inclusive)
     * @param endIndex The last index for the join (exclusive)
     * @param glue The string to glue the pieces together
     * @return A string
     */
    public static String joinStringArray(String[] stringArray, int startIndex, int endIndex, String glue) {
        String joinedString = "";
        for (int i = startIndex; i < endIndex && i < stringArray.length; i++) {
            if (i > startIndex) {
                joinedString += glue;
            }
            joinedString += stringArray[i];
        }
        return joinedString;
    }

}
