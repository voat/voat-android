package co.voat.android.api;

import com.google.gson.annotations.SerializedName;

/**
 * Lets get you logged in...
 * Created by Jawn on 6/12/2015.
 */
public class AuthResponse {
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("token_type")
    public String tokenType;
    @SerializedName("expires_in")
    public int expiresIn;
    public String userName;
    @SerializedName(".issued")
    public String issued;
    @SerializedName(".expires")
    public String expires;
}
