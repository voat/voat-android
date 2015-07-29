package co.voat.android.api;

import java.util.List;

import co.voat.android.data.Submission;

/**
 * Created by Jawn on 6/11/2015.
 */
public class SubmissionsResponse {
    public boolean success;
    public List<Submission> data;
    public String error;
}
