package online.bingzi.sdk.appwrite.services;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import online.bingzi.sdk.appwrite.BaseTest;
import online.bingzi.sdk.appwrite.models.User;
import online.bingzi.sdk.appwrite.models.response.SessionList;
import online.bingzi.sdk.appwrite.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 账户服务测试类
 */
class AccountServiceTest extends BaseTest {
    private AccountService accountService;

    @BeforeEach
    void init() {
        accountService = new AccountServiceImpl(client);
    }

    @Test
    void create() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(loadJsonFromResource("user"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<User> response = accountService.create(
                "john@example.com",
                "password123",
                "John Doe"
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("/v1/account", request.getPath());
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("john@example.com"));
        assertTrue(body.contains("password123"));
        assertTrue(body.contains("John Doe"));

        // 验证响应
        assertTrue(response.isSuccessful());
        User user = response.body();
        assertNotNull(user);
        assertEquals("5e5ea5c16897e", user.getId());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("John Doe", user.getName());
        assertTrue(user.getEmailVerification());
    }

    @Test
    void createEmailSession() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(loadJsonFromResource("user"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<User> response = accountService.createEmailSession(
                "john@example.com",
                "password123"
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("/v1/account/sessions/email", request.getPath());
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("john@example.com"));
        assertTrue(body.contains("password123"));

        // 验证响应
        assertTrue(response.isSuccessful());
        User user = response.body();
        assertNotNull(user);
        assertEquals("5e5ea5c16897e", user.getId());
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void get() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(loadJsonFromResource("user"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<User> response = accountService.get().execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/account", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
        User user = response.body();
        assertNotNull(user);
        assertEquals("5e5ea5c16897e", user.getId());
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void getPrefs() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"theme\":\"dark\",\"notifications\":true}")
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<Map<String, Object>> response = accountService.getPrefs().execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/account/prefs", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
        Map<String, Object> prefs = response.body();
        assertNotNull(prefs);
        assertEquals("dark", prefs.get("theme"));
        assertEquals(true, prefs.get("notifications"));
    }

    @Test
    void updatePrefs() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"theme\":\"light\",\"notifications\":false}")
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("theme", "light");
        prefs.put("notifications", false);
        Response<Map<String, Object>> response = accountService.updatePrefs(prefs).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("PATCH", request.getMethod());
        assertEquals("/v1/account/prefs", request.getPath());
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("light"));
        assertTrue(body.contains("false"));

        // 验证响应
        assertTrue(response.isSuccessful());
        Map<String, Object> updatedPrefs = response.body();
        assertNotNull(updatedPrefs);
        assertEquals("light", updatedPrefs.get("theme"));
        assertEquals(false, updatedPrefs.get("notifications"));
    }

    @Test
    void createRecovery() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody("{\"success\":true}")
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<Void> response = accountService.createRecovery(
                "john@example.com",
                "https://example.com/recovery"
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("/v1/account/recovery", request.getPath());
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("john@example.com"));
        assertTrue(body.contains("https://example.com/recovery"));

        // 验证响应
        assertTrue(response.isSuccessful());
    }

    @Test
    void updateRecovery() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"success\":true}")
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<Void> response = accountService.updateRecovery(
                "user-id",
                "recovery-secret",
                "newpassword123",
                "newpassword123"
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("PUT", request.getMethod());
        assertEquals("/v1/account/recovery", request.getPath());
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("user-id"));
        assertTrue(body.contains("recovery-secret"));
        assertTrue(body.contains("newpassword123"));

        // 验证响应
        assertTrue(response.isSuccessful());
    }

    @Test
    void deleteSession() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse().setResponseCode(204));

        // 执行请求
        Response<Void> response = accountService.deleteSession("current").execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("DELETE", request.getMethod());
        assertEquals("/v1/account/sessions/current", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
    }

    @Test
    void deleteSessions() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse().setResponseCode(204));

        // 执行请求
        Response<Void> response = accountService.deleteSessions().execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("DELETE", request.getMethod());
        assertEquals("/v1/account/sessions", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
    }

    @Test
    void listSessions() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(loadJsonFromResource("session_list"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<SessionList> response = accountService.listSessions().execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/account/sessions", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
        SessionList sessionList = response.body();
        assertNotNull(sessionList);
        assertTrue(sessionList.getTotal() > 0);
        assertNotNull(sessionList.getSessions());
        assertFalse(sessionList.getSessions().isEmpty());
    }
} 