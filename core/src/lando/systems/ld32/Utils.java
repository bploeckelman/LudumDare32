package lando.systems.ld32;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

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

    /**
     * Doug's screen shaker class
     */
    public static class Shake {

        private static final int default_frequency = 35;
        private static final int default_amplitude = 10;

        float[] samples;
        float  internalTimer = 0;
        float  shakeDuration = 0;

        int     duration  = 5; // In seconds, make longer if you want more variation
        int     frequency = default_frequency; // hertz

        public float   amplitude = default_amplitude; // how much you want to shake
        public boolean falloff   = true; // if the shake should decay as it expires

        int sampleCount;

        public Shake() {
            this(default_frequency, default_amplitude);
        }

        public Shake(int frequency, int amplitude) {
            this.frequency = frequency;
            this.amplitude = amplitude;

            sampleCount = duration * frequency;
            samples = new float[sampleCount];
            for (int i = 0; i < sampleCount; i++) {
                samples[i] = MathUtils.random(-1f, 1f);
            }
        }

        /**
         * Called every frame will shake the camera if it has a shake duration
         *
         * @param dt     Gdx.graphics.getDeltaTime() or your dt in seconds
         * @param camera your camera
         * @param center Where the camera should stay centered on
         */
        public void update(float dt, Camera camera, Vector2 center) {
            update(dt, camera, center.x, center.y);
        }

        public void update(float dt, Camera camera, float centerx, float centery) {
            internalTimer += dt;
            if (internalTimer > duration) internalTimer -= duration;
            if (shakeDuration > 0) {
                shakeDuration -= dt;
                float shakeTime = (internalTimer * frequency);
                int first = (int) shakeTime;
                int second = (first + 1) % sampleCount;
                int third = (first + 2) % sampleCount;
                float deltaT = shakeTime - (int) shakeTime;
                float deltaX = samples[first] * deltaT + samples[second] * (1f - deltaT);
                float deltaY = samples[second] * deltaT + samples[third] * (1f - deltaT);

                camera.position.x = centerx + deltaX * amplitude * (falloff ? Math.min(shakeDuration, 1f) : 1f);
                camera.position.y = centery + deltaY * amplitude * (falloff ? Math.min(shakeDuration, 1f) : 1f);
                camera.update();
            }
        }

        /**
         * Will make the camera shake for the duration passed in in seconds
         *
         * @param d duration of the shake in seconds
         */
        public void shake(float d) {
            shakeDuration = d;
        }
    }

    public interface Callback {
        public void run();
    }

}
