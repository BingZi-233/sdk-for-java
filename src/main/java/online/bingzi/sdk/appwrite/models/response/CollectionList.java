package online.bingzi.sdk.appwrite.models.response;

import com.google.gson.annotations.SerializedName;
import online.bingzi.sdk.appwrite.models.Collection;

import java.util.List;

/**
 * Appwrite 集合列表响应模型
 * <p>
 * 该类表示从 Appwrite API 获取集合列表时的响应结构。
 * 包含总数和集合列表两个主要字段。
 * </p>
 */
public class CollectionList {
    /**
     * 集合总数
     */
    @SerializedName("total")
    private int total;

    /**
     * 集合列表
     */
    @SerializedName("collections")
    private List<Collection> collections;

    /**
     * 获取集合总数
     *
     * @return 集合的总数量
     */
    public int getTotal() {
        return total;
    }

    /**
     * 获取集合列表
     *
     * @return 包含集合对象的列表
     */
    public List<Collection> getCollections() {
        return collections;
    }
} 