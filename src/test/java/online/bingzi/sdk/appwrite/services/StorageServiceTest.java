package online.bingzi.sdk.appwrite.services;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import online.bingzi.sdk.appwrite.models.Bucket;
import online.bingzi.sdk.appwrite.models.File;
import online.bingzi.sdk.appwrite.models.response.BucketList;
import online.bingzi.sdk.appwrite.models.response.FileList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageServiceTest {
    private MockWebServer mockWebServer;
    private StorageService storageService;

    @BeforeEach
    void setUp() {
        mockWebServer = new MockWebServer();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/v1/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        storageService = retrofit.create(StorageService.class);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    private String loadJsonFromResource(String filename) throws IOException {
        return new String(Files.readAllBytes(
                Paths.get("src/test/resources/json/" + filename + ".json")));
    }

    @Test
    void createBucket() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(loadJsonFromResource("storage_bucket"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<Bucket> response = storageService.createBucket(
                "test-bucket",
                "Test Bucket",
                Arrays.asList("read", "write"),
                30000000L,
                Arrays.asList("jpg", "png", "pdf"),
                true,
                true
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("/v1/storage/buckets", request.getPath());
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("test-bucket"));
        assertTrue(body.contains("Test Bucket"));
        assertTrue(body.contains("read"));
        assertTrue(body.contains("write"));
        assertTrue(body.contains("30000000"));
        assertTrue(body.contains("jpg"));
        assertTrue(body.contains("true"));

        // 验证响应
        assertTrue(response.isSuccessful());
        Bucket bucket = response.body();
        assertNotNull(bucket);
        assertEquals("default", bucket.getId());
        assertEquals("Default", bucket.getName());
        assertTrue(bucket.isEnabled());
    }

    @Test
    void listBuckets() throws Exception {
        // 准备模拟响应
        String mockResponse = "{\n" +
                "    \"total\": 1,\n" +
                "    \"buckets\": [" + loadJsonFromResource("storage_bucket") + "]\n" +
                "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<BucketList> response = storageService.listBuckets().execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/storage/buckets", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
        BucketList bucketList = response.body();
        assertNotNull(bucketList);
        assertEquals(1, bucketList.getTotal());
        List<Bucket> buckets = bucketList.getBuckets();
        assertNotNull(buckets);
        assertEquals(1, buckets.size());
        Bucket bucket = buckets.get(0);
        assertEquals("default", bucket.getId());
        assertEquals("Default", bucket.getName());
    }

    @Test
    void getBucket() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(loadJsonFromResource("storage_bucket"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<Bucket> response = storageService.getBucket("test-bucket").execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/storage/buckets/test-bucket", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
        Bucket bucket = response.body();
        assertNotNull(bucket);
        assertEquals("default", bucket.getId());
        assertEquals("Default", bucket.getName());
    }

    @Test
    void deleteBucket() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse().setResponseCode(204));

        // 执行请求
        Response<Void> response = storageService.deleteBucket("test-bucket").execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("DELETE", request.getMethod());
        assertEquals("/v1/storage/buckets/test-bucket", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
    }

    @Test
    void createFile() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(loadJsonFromResource("storage_file"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        java.io.File testFile = new java.io.File("src/test/resources/test.txt");
        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(
                okhttp3.MediaType.parse("text/plain"),
                testFile
        );
        okhttp3.MultipartBody.Part filePart = okhttp3.MultipartBody.Part.createFormData(
                "file",
                testFile.getName(),
                requestBody
        );

        Response<File> response = storageService.createFile(
                "test-bucket",
                filePart,
                "test-file",
                Arrays.asList("read", "write")
        ).execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("/v1/storage/buckets/test-bucket/files", request.getPath());
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("test-file"));
        assertTrue(body.contains("read"));
        assertTrue(body.contains("write"));

        // 验证响应
        assertTrue(response.isSuccessful());
        File file = response.body();
        assertNotNull(file);
        assertEquals("5e5ea5c16897e", file.getId());
        assertEquals("example.jpg", file.getName());
    }

    @Test
    void listFiles() throws Exception {
        // 准备模拟响应
        String mockResponse = "{\n" +
                "    \"total\": 1,\n" +
                "    \"files\": [" + loadJsonFromResource("storage_file") + "]\n" +
                "}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<FileList> response = storageService.listFiles("test-bucket").execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/storage/buckets/test-bucket/files", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
        FileList fileList = response.body();
        assertNotNull(fileList);
        assertEquals(1, fileList.getTotal());
        List<File> files = fileList.getFiles();
        assertNotNull(files);
        assertEquals(1, files.size());
        File file = files.get(0);
        assertEquals("5e5ea5c16897e", file.getId());
        assertEquals("example.jpg", file.getName());
    }

    @Test
    void getFile() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(loadJsonFromResource("storage_file"))
                .addHeader("Content-Type", "application/json"));

        // 执行请求
        Response<File> response = storageService.getFile("test-bucket", "test-file").execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/storage/buckets/test-bucket/files/test-file", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
        File file = response.body();
        assertNotNull(file);
        assertEquals("5e5ea5c16897e", file.getId());
        assertEquals("example.jpg", file.getName());
    }

    @Test
    void deleteFile() throws Exception {
        // 准备模拟响应
        mockWebServer.enqueue(new MockResponse().setResponseCode(204));

        // 执行请求
        Response<Void> response = storageService.deleteFile("test-bucket", "test-file").execute();

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("DELETE", request.getMethod());
        assertEquals("/v1/storage/buckets/test-bucket/files/test-file", request.getPath());

        // 验证响应
        assertTrue(response.isSuccessful());
    }
} 