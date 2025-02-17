package online.bingzi.sdk.appwrite.models.response;

import com.google.gson.annotations.SerializedName;
import online.bingzi.sdk.appwrite.models.Database;

import java.util.List;

/**
 * Appwrite 数据库列表响应模型
 * <p>
 * 该类表示从 Appwrite API 获取数据库列表时的响应结构。
 * 包含总数和数据库列表两个主要字段。
 * </p>
 */
public class DatabaseList {
    /**
     * 数据库总数
     */
    @SerializedName("total")
    private int total;

    /**
     * 数据库列表
     */
    @SerializedName("databases")
    private List<Database> databases;

    /**
     * 获取数据库总数
     *
     * @return 数据库的总数量
     */
    public int getTotal() {
        return total;
    }

    /**
     * 获取数据库列表
     *
     * @return 包含数据库对象的列表
     */
    public List<Database> getDatabases() {
        return databases;
    }
} 