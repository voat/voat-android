package co.voat.android.utils;

import java.util.Random;

import co.voat.android.R;

/**
 * Created by Jawn on 6/14/2015.
 */
public class CommonDrawables {

    private static final int[] HEADER_IDS = new int[]{
            R.drawable.header_image_1, R.drawable.header_image_2, R.drawable.header_image_3
    };

    private static Random random;
    public static int getRandomHeader() {
        if (random == null) {
            random = new Random(System.currentTimeMillis());
        }
        return HEADER_IDS[random.nextInt(HEADER_IDS.length)];
    }
}
