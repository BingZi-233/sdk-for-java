package online.bingzi.sdk.appwrite.services;

import online.bingzi.sdk.appwrite.models.Collection;
import online.bingzi.sdk.appwrite.models.Database;
import online.bingzi.sdk.appwrite.models.Document;
import online.bingzi.sdk.appwrite.models.response.CollectionList;
import online.bingzi.sdk.appwrite.models.response.DatabaseList;
import online.bingzi.sdk.appwrite.models.response.DocumentList;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

/**
 * Appwrite 数据库服务接口
 * <p>
 * 该接口提供了数据库管理的所有基本功能，包括：
 * - 数据库的创建、查询和删除
 * - 集合（Collection）的管理
 * - 文档（Document）的CRUD操作
 * - 权限管理
 * </p>
 *
 * <h2>使用示例：</h2>
 * <pre>
 * // 创建数据库服务实例
 * DatabaseService databaseService = client.createService(DatabaseService.class);
 *
 * // 创建新数据库
 * Call<Database> createDbCall = databaseService.createDatabase(
 *     "我的数据库",           // 数据库名称
 *     "unique_db_id"        // 数据库ID
 * );
 *
 * // 创建集合
 * List<String> permissions = Arrays.asList(
 *     "read(\"any\")",      // 允许所有用户读取
 *     "write(\"team:123\")" // 允许特定团队写入
 * );
 *
 * Call<Collection> createCollCall = databaseService.createCollection(
 *     "unique_db_id",       // 数据库ID
 *     "用户配置",           // 集合名称
 *     "user_settings",      // 集合ID
 *     permissions           // 访问权限
 * );
 *
 * // 创建文档
 * Map<String, Object> data = new HashMap<>();
 * data.put("theme", "dark");
 * data.put("language", "zh-CN");
 *
 * Call<Document<Map<String, Object>>> createDocCall = databaseService.createDocument(
 *     "unique_db_id",       // 数据库ID
 *     "user_settings",      // 集合ID
 *     "unique_doc_id",      // 文档ID
 *     data,                 // 文档数据
 *     permissions           // 访问权限
 * );
 *
 * // 查询文档
 * Call<Document<Map<String, Object>>> getDocCall = databaseService.getDocument(
 *     "unique_db_id",       // 数据库ID
 *     "user_settings",      // 集合ID
 *     "unique_doc_id"       // 文档ID
 * );
 *
 * getDocCall.enqueue(new Callback<Document<Map<String, Object>>>() {
 *     @Override
 *     public void onResponse(Call<Document<Map<String, Object>>> call,
 *                          Response<Document<Map<String, Object>>> response) {
 *         if (response.isSuccessful()) {
 *             Document<Map<String, Object>> doc = response.body();
 *             Map<String, Object> data = doc.getData();
 *             System.out.println("主题: " + data.get("theme"));
 *             System.out.println("语言: " + data.get("language"));
 *         }
 *     }
 * });
 * </pre>
 *
 * <h2>注意事项：</h2>
 * <ul>
 *   <li>数据库ID和集合ID必须是唯一的</li>
 *   <li>ID只能包含小写字母、数字和下划线</li>
 *   <li>权限格式必须符合Appwrite的权限语法</li>
 *   <li>文档数据必须符合集合的结构定义</li>
 *   <li>批量操作时要注意API调用限制</li>
 *   <li>大型文档建议使用分页查询</li>
 * </ul>
 */
public interface DatabaseService {
    /**
     * 创建数据库
     * <p>
     * 创建一个新的数据库。数据库是集合的容器，用于组织和隔离数据。
     * 每个项目可以创建多个数据库，每个数据库可以包含多个集合。
     * </p>
     *
     * @param name       数据库名称，用于显示
     * @param databaseId 数据库唯一标识符，只能包含小写字母、数字和下划线
     * @return 包含新创建数据库信息的Call对象
     * @throws IllegalArgumentException 如果databaseId格式无效
     */
    @FormUrlEncoded
    @POST("databases")
    Call<Database> createDatabase(
            @Field("name") String name,
            @Field("databaseId") String databaseId
    );

    /**
     * 获取数据库列表
     * <p>
     * 获取当前项目中的所有数据库。
     * 返回的列表按创建时间降序排序。
     * </p>
     *
     * @return 包含数据库列表的Call对象
     */
    @GET("databases")
    Call<DatabaseList> listDatabases();

    /**
     * 获取数据库信息
     * <p>
     * 获取指定数据库的详细信息。
     * 如果数据库不存在，将返回404错误。
     * </p>
     *
     * @param databaseId 要查询的数据库ID
     * @return 包含数据库信息的Call对象
     */
    @GET("databases/{databaseId}")
    Call<Database> getDatabase(@Path("databaseId") String databaseId);

    /**
     * 删除数据库
     * <p>
     * 删除指定的数据库及其所有集合和文档。
     * 此操作不可逆，请谨慎使用。
     * </p>
     *
     * @param databaseId 要删除的数据库ID
     * @return 空的Call对象
     */
    @DELETE("databases/{databaseId}")
    Call<Void> deleteDatabase(@Path("databaseId") String databaseId);

    /**
     * 创建集合
     * <p>
     * 在指定数据库中创建新的集合。集合用于存储相同结构的文档。
     * 创建后可以通过属性API定义集合的结构。
     * </p>
     *
     * @param databaseId   数据库ID
     * @param name         集合名称，用于显示
     * @param collectionId 集合唯一标识符，只能包含小写字母、数字和下划线
     * @param permissions  访问权限列表，使用Appwrite权限语法
     * @return 包含新创建集合信息的Call对象
     * @throws IllegalArgumentException 如果collectionId格式无效或权限语法错误
     */
    @FormUrlEncoded
    @POST("databases/{databaseId}/collections")
    Call<Collection> createCollection(
            @Path("databaseId") String databaseId,
            @Field("name") String name,
            @Field("collectionId") String collectionId,
            @Field("permissions") List<String> permissions
    );

    /**
     * 获取集合列表
     * <p>
     * 获取指定数据库中的所有集合。
     * 返回的列表按创建时间降序排序。
     * </p>
     *
     * @param databaseId 数据库ID
     * @return 包含集合列表的Call对象
     */
    @GET("databases/{databaseId}/collections")
    Call<CollectionList> listCollections(@Path("databaseId") String databaseId);

    /**
     * 获取集合信息
     * <p>
     * 获取指定集合的详细信息，包括结构定义和权限设置。
     * 如果集合不存在，将返回404错误。
     * </p>
     *
     * @param databaseId   数据库ID
     * @param collectionId 集合ID
     * @return 包含集合信息的Call对象
     */
    @GET("databases/{databaseId}/collections/{collectionId}")
    Call<Collection> getCollection(
            @Path("databaseId") String databaseId,
            @Path("collectionId") String collectionId
    );

    /**
     * 删除集合
     * <p>
     * 删除指定的集合及其所有文档。
     * 此操作不可逆，请谨慎使用。
     * </p>
     *
     * @param databaseId   数据库ID
     * @param collectionId 要删除的集合ID
     * @return 空的Call对象
     */
    @DELETE("databases/{databaseId}/collections/{collectionId}")
    Call<Void> deleteCollection(
            @Path("databaseId") String databaseId,
            @Path("collectionId") String collectionId
    );

    /**
     * 创建文档
     * <p>
     * 在指定集合中创建新的文档。文档数据必须符合集合的结构定义。
     * 如果指定了documentId，则使用该ID；否则系统会自动生成一个唯一ID。
     * </p>
     *
     * @param databaseId   数据库ID
     * @param collectionId 集合ID
     * @param documentId   文档唯一标识符（可选）
     * @param data         文档数据，键值对形式
     * @param permissions  访问权限列表
     * @return 包含新创建文档信息的Call对象
     * @throws IllegalArgumentException 如果数据不符合集合结构或权限语法错误
     */
    @FormUrlEncoded
    @POST("databases/{databaseId}/collections/{collectionId}/documents")
    Call<Document<Map<String, Object>>> createDocument(
            @Path("databaseId") String databaseId,
            @Path("collectionId") String collectionId,
            @Field("documentId") String documentId,
            @Field("data") Map<String, Object> data,
            @Field("permissions") List<String> permissions
    );

    /**
     * 获取文档列表
     * <p>
     * 获取指定集合中的所有文档。
     * 返回的列表按创建时间降序排序。
     * 建议使用分页参数限制返回数量。
     * </p>
     *
     * @param databaseId   数据库ID
     * @param collectionId 集合ID
     * @return 包含文档列表的Call对象
     */
    @GET("databases/{databaseId}/collections/{collectionId}/documents")
    Call<DocumentList<Map<String, Object>>> listDocuments(
            @Path("databaseId") String databaseId,
            @Path("collectionId") String collectionId
    );

    /**
     * 获取文档信息
     * <p>
     * 获取指定文档的详细信息。
     * 如果文档不存在或没有访问权限，将返回404错误。
     * </p>
     *
     * @param databaseId   数据库ID
     * @param collectionId 集合ID
     * @param documentId   文档ID
     * @return 包含文档信息的Call对象
     */
    @GET("databases/{databaseId}/collections/{collectionId}/documents/{documentId}")
    Call<Document<Map<String, Object>>> getDocument(
            @Path("databaseId") String databaseId,
            @Path("collectionId") String collectionId,
            @Path("documentId") String documentId
    );

    /**
     * 更新文档
     * <p>
     * 更新指定文档的数据和权限。
     * 只更新提供的字段，未提供的字段保持不变。
     * 数据必须符合集合的结构定义。
     * </p>
     *
     * @param databaseId   数据库ID
     * @param collectionId 集合ID
     * @param documentId   文档ID
     * @param data         要更新的数据
     * @param permissions  新的访问权限列表
     * @return 包含更新后文档信息的Call对象
     * @throws IllegalArgumentException 如果数据不符合集合结构或权限语法错误
     */
    @FormUrlEncoded
    @PATCH("databases/{databaseId}/collections/{collectionId}/documents/{documentId}")
    Call<Document<Map<String, Object>>> updateDocument(
            @Path("databaseId") String databaseId,
            @Path("collectionId") String collectionId,
            @Path("documentId") String documentId,
            @Field("data") Map<String, Object> data,
            @Field("permissions") List<String> permissions
    );

    /**
     * 删除文档
     * <p>
     * 删除指定的文档。
     * 此操作不可逆，请谨慎使用。
     * 需要有文档的删除权限。
     * </p>
     *
     * @param databaseId   数据库ID
     * @param collectionId 集合ID
     * @param documentId   要删除的文档ID
     * @return 空的Call对象
     */
    @DELETE("databases/{databaseId}/collections/{collectionId}/documents/{documentId}")
    Call<Void> deleteDocument(
            @Path("databaseId") String databaseId,
            @Path("collectionId") String collectionId,
            @Path("documentId") String documentId
    );
} 