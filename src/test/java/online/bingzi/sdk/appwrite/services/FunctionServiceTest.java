package online.bingzi.sdk.appwrite.services;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import online.bingzi.sdk.appwrite.BaseTest;
import online.bingzi.sdk.appwrite.models.Execution;
import online.bingzi.sdk.appwrite.models.Function;
import online.bingzi.sdk.appwrite.models.response.ExecutionList;
import online.bingzi.sdk.appwrite.models.response.FunctionList;
import online.bingzi.sdk.appwrite.services.impl.FunctionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FunctionServiceTest extends BaseTest {
    private FunctionService functionService;

    @BeforeEach
    void init() {
        functionService = new FunctionServiceImpl(client);
    }

    @Test
    void createFunction() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(loadJsonFromResource("function"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<Function> response = functionService.createFunction(
                "test-function",
                "Test Function",
                "node-14.5",
                Arrays.asList("users"),
                Arrays.asList("users.*.create"),
                "0 0 * * *",
                15,
                true
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("/v1/functions", request.getPath());
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("test-function"));
        assertTrue(body.contains("Test Function"));
        assertTrue(body.contains("node-14.5"));
        assertTrue(body.contains("users"));
        assertTrue(body.contains("users.*.create"));
        assertTrue(body.contains("0 0 * * *"));
        assertTrue(body.contains("15"));
        assertTrue(body.contains("true"));

        // 验证响应
        assertTrue(response.isSuccessful());
        Function function = response.body();
        assertNotNull(function);
        assertEquals("5e5ea5c16897e", function.getId());
        assertEquals("hello-world", function.getName());
    }

    @Test
    void listFunctions() throws Exception {
        // 准备模拟响应
        String mockResponse = "{\n" +
                "    \"total\": 1,\n" +
                "    \"functions\": [" + loadJsonFromResource("function") + "]\n" +
                "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<FunctionList> response = functionService.listFunctions().execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/functions", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
        FunctionList functionList = response.body();
        assertNotNull(functionList);
        assertEquals(1, functionList.getTotal());
        List<Function> functions = functionList.getFunctions();
        assertNotNull(functions);
        assertEquals(1, functions.size());
        Function function = functions.get(0);
        assertEquals("5e5ea5c16897e", function.getId());
        assertEquals("Test Function", function.getName());
    }

    @Test
    void getFunction() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(loadJsonFromResource("function"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<Function> response = functionService.getFunction("test-function").execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/functions/test-function", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
        Function function = response.body();
        assertNotNull(function);
        assertEquals("5e5ea5c16897e", function.getId());
        assertEquals("hello-world", function.getName());
    }

    @Test
    void updateFunction() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(loadJsonFromResource("function"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<Function> response = functionService.updateFunction(
                "test-function",
                "Updated Function",
                Arrays.asList("users"),
                Arrays.asList("users.*.update"),
                "0 0 * * *",
                30,
                true
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("PUT", request.getMethod());
        assertEquals("/v1/functions/test-function", request.getPath());
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("Updated Function"));
        assertTrue(body.contains("users"));
        assertTrue(body.contains("users.*.update"));
        assertTrue(body.contains("0 0 * * *"));
        assertTrue(body.contains("30"));
        assertTrue(body.contains("true"));

        // 验证响应
        assertTrue(response.isSuccessful());
        Function function = response.body();
        assertNotNull(function);
        assertEquals("5e5ea5c16897e", function.getId());
    }

    @Test
    void deleteFunction() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse().setResponseCode(204));

        // 执行请求
        Response<Void> response = functionService.deleteFunction("test-function").execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("DELETE", request.getMethod());
        assertEquals("/v1/functions/test-function", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
    }

    @Test
    void createExecution() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(loadJsonFromResource("execution"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        String data = "{\"key\": \"value\"}";
        Response<Execution> response = functionService.createExecution(
                "test-function",
                data
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("/v1/functions/test-function/executions", request.getPath());
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("data=" + data));

        // 验证响应
        assertTrue(response.isSuccessful());
        Execution execution = response.body();
        assertNotNull(execution);
        assertEquals("5e5ea5c16897f", execution.getId());
        assertEquals("completed", execution.getStatus());
    }

    @Test
    void listExecutions() throws Exception {
        // 准备模拟响应
        String mockResponse = "{\n" +
                "    \"total\": 1,\n" +
                "    \"executions\": [" + loadJsonFromResource("execution") + "]\n" +
                "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<ExecutionList> response = functionService.listExecutions("test-function").execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/functions/test-function/executions", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
        ExecutionList executionList = response.body();
        assertNotNull(executionList);
        assertEquals(1, executionList.getTotal());
        List<Execution> executions = executionList.getExecutions();
        assertNotNull(executions);
        assertEquals(1, executions.size());
        Execution execution = executions.get(0);
        assertEquals("5e5ea5c16897f", execution.getId());
        assertEquals("completed", execution.getStatus());
    }

    @Test
    void getExecution() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(loadJsonFromResource("execution"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<Execution> response = functionService.getExecution(
                "test-function",
                "test-execution"
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/functions/test-function/executions/test-execution", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
        Execution execution = response.body();
        assertNotNull(execution);
        assertEquals("5e5ea5c16897f", execution.getId());
        assertEquals("completed", execution.getStatus());
    }

    @Test
    void updateVariables() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(loadJsonFromResource("function"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Map<String, String> variables = new HashMap<>();
        variables.put("API_KEY", "123456");
        variables.put("DB_NAME", "test");
        Response<Function> response = functionService.updateVariables(
                "test-function",
                variables
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("PUT", request.getMethod());
        assertEquals("/v1/functions/test-function/variables", request.getPath());
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("API_KEY=123456"));
        assertTrue(body.contains("DB_NAME=test"));

        // 验证响应
        assertTrue(response.isSuccessful());
        Function function = response.body();
        assertNotNull(function);
        assertEquals("5e5ea5c16897e", function.getId());
    }
} 