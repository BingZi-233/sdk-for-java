package online.bingzi.sdk.appwrite.services;

import online.bingzi.sdk.appwrite.models.Bucket;
import online.bingzi.sdk.appwrite.models.File;
import online.bingzi.sdk.appwrite.models.response.BucketList;
import online.bingzi.sdk.appwrite.models.response.FileList;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Appwrite 存储服务接口
 * <p>
 * 该接口提供了文件存储管理的所有基本功能，包括：
 * - 存储桶（Bucket）的创建和管理
 * - 文件的上传、下载和管理
 * - 文件权限控制
 * - 安全特性（加密、防病毒）
 * </p>
 *
 * <h2>使用示例：</h2>
 * <pre>
 * // 创建存储服务实例
 * StorageService storageService = client.createService(StorageService.class);
 *
 * // 创建存储桶
 * List<String> permissions = Arrays.asList(
 *     "read(\"any\")",           // 允许所有用户读取
 *     "write(\"team:dev\")"      // 允许开发团队写入
 * );
 * List<String> extensions = Arrays.asList("jpg", "png", "pdf");
 *
 * Call<Bucket> createCall = storageService.createBucket(
 *     "user-files",              // 存储桶ID
 *     "用户文件",                // 存储桶名称
 *     permissions,               // 访问权限
 *     10L * 1024 * 1024,        // 最大文件大小（10MB）
 *     extensions,                // 允许的文件类型
 *     true,                      // 启用加密
 *     true                       // 启用防病毒
 * );
 *
 * // 上传文件
 * java.io.File uploadFile = new java.io.File("document.pdf");
 * RequestBody requestFile = RequestBody.create(
 *     MediaType.parse("application/pdf"), 
 *     uploadFile
 * );
 * MultipartBody.Part body = MultipartBody.Part.createFormData(
 *     "file",
 *     uploadFile.getName(),
 *     requestFile
 * );
 *
 * Call<File> uploadCall = storageService.createFile(
 *     "user-files",              // 存储桶ID
 *     body,                      // 文件数据
 *     "unique_file_id",          // 文件ID
 *     permissions                // 文件权限
 * );
 *
 * uploadCall.enqueue(new Callback<File>() {
 *     @Override
 *     public void onResponse(Call<File> call, Response<File> response) {
 *         if (response.isSuccessful()) {
 *             File file = response.body();
 *             System.out.println("文件ID: " + file.getId());
 *             System.out.println("文件名: " + file.getName());
 *             System.out.println("大小: " + file.getSize() + "字节");
 *             System.out.println("MIME类型: " + file.getMimeType());
 *         }
 *     }
 * });
 *
 * // 获取文件列表
 * Call<List<File>> listCall = storageService.listFiles("user-files");
 * listCall.enqueue(new Callback<List<File>>() {
 *     @Override
 *     public void onResponse(Call<List<File>> call, Response<List<File>> response) {
 *         if (response.isSuccessful()) {
 *             List<File> files = response.body();
 *             for (File file : files) {
 *                 System.out.println(file.getName() + " - " + file.getSize() + "字节");
 *             }
 *         }
 *     }
 * });
 * </pre>
 *
 * <h2>注意事项：</h2>
 * <ul>
 *   <li>存储桶ID必须是唯一的，且只能包含小写字母、数字和中划线</li>
 *   <li>文件大小不能超过存储桶设置的限制</li>
 *   <li>文件扩展名必须在允许列表中</li>
 *   <li>启用加密和防病毒功能可能会影响上传性能</li>
 *   <li>文件ID如果不指定会自动生成</li>
 *   <li>大文件上传建议使用分片上传</li>
 * </ul>
 */
public interface StorageService {
    /**
     * 创建存储桶
     * <p>
     * 创建一个新的存储桶用于存储文件。
     * 存储桶可以设置访问权限、文件大小限制和允许的文件类型。
     * </p>
     *
     * @param bucketId             存储桶唯一标识符，只能包含小写字母、数字和中划线
     * @param name                 存储桶名称，用于显示
     * @param permissions          访问权限列表
     * @param maximumFileSize      单个文件的最大大小（字节）
     * @param allowedFileExtensions 允许上传的文件扩展名列表
     * @param encryption           是否启用文件加密
     * @param antivirus           是否启用防病毒扫描
     * @return 包含新创建存储桶信息的Call对象
     * @throws IllegalArgumentException 如果bucketId格式无效或参数不合法
     */
    @FormUrlEncoded
    @POST("storage/buckets")
    Call<Bucket> createBucket(
            @Field("bucketId") String bucketId,
            @Field("name") String name,
            @Field("permissions") List<String> permissions,
            @Field("maximumFileSize") Long maximumFileSize,
            @Field("allowedFileExtensions") List<String> allowedFileExtensions,
            @Field("encryption") Boolean encryption,
            @Field("antivirus") Boolean antivirus
    );

    /**
     * 获取存储桶列表
     * <p>
     * 获取当前项目中的所有存储桶。
     * 返回的列表按创建时间降序排序。
     * </p>
     *
     * @return 包含存储桶列表的Call对象
     */
    @GET("storage/buckets")
    Call<BucketList> listBuckets();

    /**
     * 获取存储桶信息
     * <p>
     * 获取指定存储桶的详细信息，包括配置和统计数据。
     * 如果存储桶不存在，将返回404错误。
     * </p>
     *
     * @param bucketId 要查询的存储桶ID
     * @return 包含存储桶信息的Call对象
     */
    @GET("storage/buckets/{bucketId}")
    Call<Bucket> getBucket(@Path("bucketId") String bucketId);

    /**
     * 删除存储桶
     * <p>
     * 删除指定的存储桶及其所有文件。
     * 此操作不可逆，请谨慎使用。
     * </p>
     *
     * @param bucketId 要删除的存储桶ID
     * @return 空的Call对象
     */
    @DELETE("storage/buckets/{bucketId}")
    Call<Void> deleteBucket(@Path("bucketId") String bucketId);

    /**
     * 上传文件
     * <p>
     * 将文件上传到指定的存储桶。
     * 文件必须符合存储桶的大小和类型限制。
     * 如果启用了防病毒，文件会在上传后进行扫描。
     * </p>
     *
     * @param bucketId    存储桶ID
     * @param file        文件数据，使用MultipartBody.Part格式
     * @param fileId      文件唯一标识符（可选）
     * @param permissions 文件访问权限列表
     * @return 包含上传文件信息的Call对象
     * @throws IllegalArgumentException 如果文件大小超限或类型不允许
     */
    @Multipart
    @POST("storage/buckets/{bucketId}/files")
    Call<File> createFile(
            @Path("bucketId") String bucketId,
            @Part okhttp3.MultipartBody.Part file,
            @Part("fileId") String fileId,
            @Part("permissions") List<String> permissions
    );

    /**
     * 获取文件列表
     * <p>
     * 获取指定存储桶中的所有文件。
     * 返回的列表按上传时间降序排序。
     * </p>
     *
     * @param bucketId 存储桶ID
     * @return 包含文件列表的Call对象
     */
    @GET("storage/buckets/{bucketId}/files")
    Call<FileList> listFiles(@Path("bucketId") String bucketId);

    /**
     * 获取文件信息
     * <p>
     * 获取指定文件的详细信息，包括大小、类型和权限。
     * 如果文件不存在或没有访问权限，将返回404错误。
     * </p>
     *
     * @param bucketId 存储桶ID
     * @param fileId   文件ID
     * @return 包含文件信息的Call对象
     */
    @GET("storage/buckets/{bucketId}/files/{fileId}")
    Call<File> getFile(
            @Path("bucketId") String bucketId,
            @Path("fileId") String fileId
    );

    /**
     * 删除文件
     * <p>
     * 从存储桶中删除指定的文件。
     * 此操作不可逆，需要有文件的删除权限。
     * </p>
     *
     * @param bucketId 存储桶ID
     * @param fileId   要删除的文件ID
     * @return 空的Call对象
     */
    @DELETE("storage/buckets/{bucketId}/files/{fileId}")
    Call<Void> deleteFile(
            @Path("bucketId") String bucketId,
            @Path("fileId") String fileId
    );
} 