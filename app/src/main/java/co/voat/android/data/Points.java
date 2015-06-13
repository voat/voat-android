package co.voat.android.data;

/**
 * Created by Jawn on 6/11/2015.
 */
public class Points {
    int total;
    int sum;
    int upCount;
    int downCount;
    float upRatio;
    float downRatio;
    float bias;

    public int getTotal() {
        return total;
    }

    public int getSum() {
        return sum;
    }

    public int getUpCount() {
        return upCount;
    }

    public int getDownCount() {
        return downCount;
    }

    public float getUpRatio() {
        return upRatio;
    }

    public float getDownRatio() {
        return downRatio;
    }

    public float getBias() {
        return bias;
    }
}
