package online.bingzi.sdk.appwrite.models;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class User {
    @SerializedName("$id")
    private String id;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("emailVerification")
    private boolean emailVerification;
    
    @SerializedName("status")
    private boolean status;
    
    @SerializedName("prefs")
    private Map<String, Object> preferences;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmailVerification() {
        return emailVerification;
    }

    public boolean isStatus() {
        return status;
    }

    public Map<String, Object> getPreferences() {
        return preferences;
    }
} 