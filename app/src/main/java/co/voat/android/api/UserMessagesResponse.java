package co.voat.android.api;

import java.util.List;

import co.voat.android.data.Message;

/**
 * Can I see your messages? Oh, you don't have any? Well...
 * Created by Jawn on 6/11/2015.
 */
public class UserMessagesResponse {
    public boolean success;
    public List<Message> data;
    public String error;
}
