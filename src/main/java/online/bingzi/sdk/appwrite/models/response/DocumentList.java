package online.bingzi.sdk.appwrite.models.response;

import com.google.gson.annotations.SerializedName;
import online.bingzi.sdk.appwrite.models.Document;

import java.util.List;

/**
 * Appwrite 文档列表响应模型
 * <p>
 * 该类表示从 Appwrite API 获取文档列表时的响应结构。
 * 包含总数和文档列表两个主要字段。
 * </p>
 *
 * @param <T> 文档数据的类型参数
 */
public class DocumentList<T> {
    /**
     * 文档总数
     */
    @SerializedName("total")
    private int total;

    /**
     * 文档列表
     */
    @SerializedName("documents")
    private List<Document<T>> documents;

    /**
     * 获取文档总数
     *
     * @return 文档的总数量
     */
    public int getTotal() {
        return total;
    }

    /**
     * 获取文档列表
     *
     * @return 包含文档对象的列表
     */
    public List<Document<T>> getDocuments() {
        return documents;
    }
} 