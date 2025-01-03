package online.bingzi.sdk.appwrite.services.impl;

import online.bingzi.sdk.appwrite.Client;
import online.bingzi.sdk.appwrite.models.Collection;
import online.bingzi.sdk.appwrite.models.Database;
import online.bingzi.sdk.appwrite.models.Document;
import online.bingzi.sdk.appwrite.services.DatabaseService;
import retrofit2.Call;

import java.util.List;
import java.util.Map;

/**
 * 数据库服务实现类
 */
public class DatabaseServiceImpl implements DatabaseService {
    private final DatabaseService databaseService;

    public DatabaseServiceImpl(Client client) {
        this.databaseService = client.createService(DatabaseService.class);
    }

    @Override
    public Call<Database> createDatabase(String name, String id) {
        return databaseService.createDatabase(name, id);
    }

    @Override
    public Call<List<Database>> listDatabases() {
        return databaseService.listDatabases();
    }

    @Override
    public Call<Database> getDatabase(String databaseId) {
        return databaseService.getDatabase(databaseId);
    }

    @Override
    public Call<Void> deleteDatabase(String databaseId) {
        return databaseService.deleteDatabase(databaseId);
    }

    @Override
    public Call<Collection> createCollection(String databaseId, String name, String id) {
        return databaseService.createCollection(databaseId, name, id);
    }

    @Override
    public Call<List<Collection>> listCollections(String databaseId) {
        return databaseService.listCollections(databaseId);
    }

    @Override
    public Call<Collection> getCollection(String databaseId, String collectionId) {
        return databaseService.getCollection(databaseId, collectionId);
    }

    @Override
    public Call<Void> deleteCollection(String databaseId, String collectionId) {
        return databaseService.deleteCollection(databaseId, collectionId);
    }

    @Override
    public Call<Document<Map<String, Object>>> createDocument(String databaseId, String collectionId, Map<String, Object> data, String id) {
        return databaseService.createDocument(databaseId, collectionId, data, id);
    }

    @Override
    public Call<List<Document<Map<String, Object>>>> listDocuments(String databaseId, String collectionId) {
        return databaseService.listDocuments(databaseId, collectionId);
    }

    @Override
    public Call<Document<Map<String, Object>>> getDocument(String databaseId, String collectionId, String documentId) {
        return databaseService.getDocument(databaseId, collectionId, documentId);
    }

    @Override
    public Call<Document<Map<String, Object>>> updateDocument(String databaseId, String collectionId, String documentId, Map<String, Object> data) {
        return databaseService.updateDocument(databaseId, collectionId, documentId, data);
    }

    @Override
    public Call<Void> deleteDocument(String databaseId, String collectionId, String documentId) {
        return databaseService.deleteDocument(databaseId, collectionId, documentId);
    }
} 