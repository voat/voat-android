package co.voat.android.api;

import java.util.List;

import co.voat.android.data.Subscription;

/**
 * Created by Jawn on 6/11/2015.
 */
public class SubscriptionsResponse {
    public boolean success;
    public List<Subscription> data;
    public String error;
}
