package online.bingzi.sdk.appwrite.models.response;

import com.google.gson.annotations.SerializedName;
import online.bingzi.sdk.appwrite.models.Bucket;

import java.util.List;

/**
 * Appwrite 存储桶列表响应模型
 * <p>
 * 该类表示从 Appwrite API 获取存储桶列表时的响应结构。
 * 包含总数和存储桶列表两个主要字段。
 * </p>
 */
public class BucketList {
    /**
     * 存储桶总数
     */
    @SerializedName("total")
    private int total;

    /**
     * 存储桶列表
     */
    @SerializedName("buckets")
    private List<Bucket> buckets;

    /**
     * 获取存储桶总数
     *
     * @return 存储桶的总数量
     */
    public int getTotal() {
        return total;
    }

    /**
     * 获取存储桶列表
     *
     * @return 包含存储桶对象的列表
     */
    public List<Bucket> getBuckets() {
        return buckets;
    }
} 