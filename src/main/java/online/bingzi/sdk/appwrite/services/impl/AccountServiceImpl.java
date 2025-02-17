package online.bingzi.sdk.appwrite.services.impl;

import online.bingzi.sdk.appwrite.Client;
import online.bingzi.sdk.appwrite.models.User;
import online.bingzi.sdk.appwrite.models.response.SessionList;
import online.bingzi.sdk.appwrite.services.AccountService;
import retrofit2.Call;

import java.util.Map;

/**
 * Appwrite账户服务实现类
 */
public class AccountServiceImpl implements AccountService {
    private final AccountService accountService;

    public AccountServiceImpl(Client client) {
        this.accountService = client.createService(AccountService.class);
    }

    @Override
    public Call<User> create(String email, String password, String name) {
        return accountService.create(email, password, name);
    }

    @Override
    public Call<User> createEmailSession(String email, String password) {
        return accountService.createEmailSession(email, password);
    }

    @Override
    public Call<User> get() {
        return accountService.get();
    }

    @Override
    public Call<Map<String, Object>> getPrefs() {
        return accountService.getPrefs();
    }

    @Override
    public Call<Map<String, Object>> updatePrefs(Map<String, Object> prefs) {
        return accountService.updatePrefs(prefs);
    }

    @Override
    public Call<Void> createRecovery(String email, String url) {
        return accountService.createRecovery(email, url);
    }

    @Override
    public Call<Void> updateRecovery(String userId, String secret, String password, String passwordAgain) {
        return accountService.updateRecovery(userId, secret, password, passwordAgain);
    }

    @Override
    public Call<Void> deleteSession(String sessionId) {
        return accountService.deleteSession(sessionId);
    }

    @Override
    public Call<Void> deleteSessions() {
        return accountService.deleteSessions();
    }

    @Override
    public Call<SessionList> listSessions() {
        return accountService.listSessions();
    }
} 