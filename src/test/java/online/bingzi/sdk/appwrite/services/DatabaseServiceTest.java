package online.bingzi.sdk.appwrite.services;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import online.bingzi.sdk.appwrite.BaseTest;
import online.bingzi.sdk.appwrite.models.Collection;
import online.bingzi.sdk.appwrite.models.Database;
import online.bingzi.sdk.appwrite.models.Document;
import online.bingzi.sdk.appwrite.models.response.CollectionList;
import online.bingzi.sdk.appwrite.models.response.DatabaseList;
import online.bingzi.sdk.appwrite.models.response.DocumentList;
import online.bingzi.sdk.appwrite.services.impl.DatabaseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据库服务测试类
 */
class DatabaseServiceTest extends BaseTest {
    private DatabaseService databaseService;

    @BeforeEach
    void init() {
        databaseService = new DatabaseServiceImpl(client);
    }

    @Test
    void createDatabase() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(loadJsonFromResource("database"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<Database> response = databaseService.createDatabase(
                "Test Database",
                "test-db"
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("/v1/databases", request.getPath());
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("Test Database"));
        assertTrue(body.contains("test-db"));

        // 验证响应
        assertTrue(response.isSuccessful());
        Database database = response.body();
        assertNotNull(database);
        assertEquals("5e5ea5c16897e", database.getId());
        assertEquals("Test Database", database.getName());
        assertTrue(database.isEnabled());
    }

    @Test
    void listDatabases() throws Exception {
        // 准备模拟响应
        String mockResponse = "{\n" +
                "    \"total\": 1,\n" +
                "    \"databases\": [" + loadJsonFromResource("database") + "]\n" +
                "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<DatabaseList> response = databaseService.listDatabases().execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/databases", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
        DatabaseList databaseList = response.body();
        assertNotNull(databaseList);
        assertEquals(1, databaseList.getTotal());
        List<Database> databases = databaseList.getDatabases();
        assertNotNull(databases);
        assertEquals(1, databases.size());
        Database database = databases.get(0);
        assertEquals("5e5ea5c16897e", database.getId());
        assertEquals("Test Database", database.getName());
    }

    @Test
    void getDatabase() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(loadJsonFromResource("database"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<Database> response = databaseService.getDatabase("test-db").execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/databases/test-db", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
        Database database = response.body();
        assertNotNull(database);
        assertEquals("5e5ea5c16897e", database.getId());
        assertEquals("Test Database", database.getName());
    }

    @Test
    void deleteDatabase() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse().setResponseCode(204));

        // 执行请求
        Response<Void> response = databaseService.deleteDatabase("test-db").execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("DELETE", request.getMethod());
        assertEquals("/v1/databases/test-db", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
    }

    @Test
    void createCollection() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(loadJsonFromResource("collection"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<Collection> response = databaseService.createCollection(
                "test-db",
                "Test Collection",
                "test-collection",
                Arrays.asList("read", "write")
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("/v1/databases/test-db/collections", request.getPath());
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("Test Collection"));
        assertTrue(body.contains("test-collection"));
        assertTrue(body.contains("read"));
        assertTrue(body.contains("write"));

        // 验证响应
        assertTrue(response.isSuccessful());
        Collection collection = response.body();
        assertNotNull(collection);
        assertEquals("5e5ea5c16897e", collection.getId());
        assertEquals("Test Collection", collection.getName());
        assertTrue(collection.isEnabled());
        assertTrue(collection.isDocumentSecurity());
    }

    @Test
    void listCollections() throws Exception {
        // 准备模拟响应
        String mockResponse = "{\n" +
                "    \"total\": 1,\n" +
                "    \"collections\": [" + loadJsonFromResource("collection") + "]\n" +
                "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<CollectionList> response = databaseService.listCollections("test-db").execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/databases/test-db/collections", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
        CollectionList collectionList = response.body();
        assertNotNull(collectionList);
        assertEquals(1, collectionList.getTotal());
        List<Collection> collections = collectionList.getCollections();
        assertNotNull(collections);
        assertEquals(1, collections.size());
        Collection collection = collections.get(0);
        assertEquals("5e5ea5c16897e", collection.getId());
        assertEquals("Test Collection", collection.getName());
    }

    @Test
    void getCollection() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(loadJsonFromResource("collection"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<Collection> response = databaseService.getCollection(
                "test-db",
                "test-collection"
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/databases/test-db/collections/test-collection", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
        Collection collection = response.body();
        assertNotNull(collection);
        assertEquals("5e5ea5c16897e", collection.getId());
        assertEquals("Test Collection", collection.getName());
    }

    @Test
    void deleteCollection() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse().setResponseCode(204));

        // 执行请求
        Response<Void> response = databaseService.deleteCollection(
                "test-db",
                "test-collection"
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("DELETE", request.getMethod());
        assertEquals("/v1/databases/test-db/collections/test-collection", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
    }

    @Test
    void createDocument() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(loadJsonFromResource("document"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Map<String, Object> data = new HashMap<>();
        data.put("name", "John Doe");
        data.put("age", 30);
        data.put("email", "john@example.com");

        Response<Document<Map<String, Object>>> response = databaseService.createDocument(
                "test-db",
                "test-collection",
                "test-doc",
                data,
                Arrays.asList("read", "write")
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("/v1/databases/test-db/collections/test-collection/documents", request.getPath());
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("John Doe"));
        assertTrue(body.contains("30"));
        assertTrue(body.contains("john@example.com"));
        assertTrue(body.contains("read"));
        assertTrue(body.contains("write"));

        // 验证响应
        assertTrue(response.isSuccessful());
        Document<Map<String, Object>> document = response.body();
        assertNotNull(document);
        assertEquals("5e5ea5c16897e", document.getId());
        assertEquals("John Doe", document.getData().get("name"));
        assertEquals(30, ((Number)document.getData().get("age")).intValue());
    }

    @Test
    void listDocuments() throws Exception {
        // 准备模拟响应
        String mockResponse = "{\n" +
                "    \"total\": 1,\n" +
                "    \"documents\": [" + loadJsonFromResource("document") + "]\n" +
                "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<DocumentList<Map<String, Object>>> response = databaseService.listDocuments(
                "test-db",
                "test-collection"
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/databases/test-db/collections/test-collection/documents", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
        DocumentList<Map<String, Object>> documentList = response.body();
        assertNotNull(documentList);
        assertEquals(1, documentList.getTotal());
        List<Document<Map<String, Object>>> documents = documentList.getDocuments();
        assertNotNull(documents);
        assertEquals(1, documents.size());
        Document<Map<String, Object>> document = documents.get(0);
        assertEquals("5e5ea5c16897e", document.getId());
        assertEquals("John Doe", document.getData().get("name"));
    }

    @Test
    void getDocument() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(loadJsonFromResource("document"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<Document<Map<String, Object>>> response = databaseService.getDocument(
                "test-db",
                "test-collection",
                "test-doc"
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/databases/test-db/collections/test-collection/documents/test-doc", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
        Document<Map<String, Object>> document = response.body();
        assertNotNull(document);
        assertEquals("5e5ea5c16897e", document.getId());
        assertEquals("John Doe", document.getData().get("name"));
    }

    @Test
    void updateDocument() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(loadJsonFromResource("document"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Map<String, Object> data = new HashMap<>();
        data.put("name", "Jane Doe");
        data.put("age", 28);

        Response<Document<Map<String, Object>>> response = databaseService.updateDocument(
                "test-db",
                "test-collection",
                "test-doc",
                data,
                Arrays.asList("read", "write")
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("PATCH", request.getMethod());
        assertEquals("/v1/databases/test-db/collections/test-collection/documents/test-doc", request.getPath());
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("Jane Doe"));
        assertTrue(body.contains("28"));
        assertTrue(body.contains("read"));
        assertTrue(body.contains("write"));

        // 验证响应
        assertTrue(response.isSuccessful());
        Document<Map<String, Object>> document = response.body();
        assertNotNull(document);
        assertEquals("5e5ea5c16897e", document.getId());
        assertEquals("John Doe", document.getData().get("name"));
    }

    @Test
    void deleteDocument() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse().setResponseCode(204));

        // 执行请求
        Response<Void> response = databaseService.deleteDocument(
                "test-db",
                "test-collection",
                "test-doc"
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("DELETE", request.getMethod());
        assertEquals("/v1/databases/test-db/collections/test-collection/documents/test-doc", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
    }
} 