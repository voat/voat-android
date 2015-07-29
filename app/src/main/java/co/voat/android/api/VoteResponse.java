package co.voat.android.api;

/**
 * Vote response. Yeah. Nothing clever here.
 * Created by Jawn on 6/15/2015.
 */
public class VoteResponse {
    public boolean success;
    public VoteResponseBody data;

    public class VoteResponseBody {
        public int recordedValue;
        public boolean success;
        public int result = 1;
        public String resultName;
        public String message;
    }
}
