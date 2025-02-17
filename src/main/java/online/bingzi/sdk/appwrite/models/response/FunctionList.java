package online.bingzi.sdk.appwrite.models.response;

import com.google.gson.annotations.SerializedName;
import online.bingzi.sdk.appwrite.models.Function;

import java.util.List;

/**
 * Appwrite 函数列表响应模型
 * <p>
 * 该类表示从 Appwrite API 获取函数列表时的响应结构。
 * 包含总数和函数列表两个主要字段。
 * </p>
 */
public class FunctionList {
    /**
     * 函数总数
     */
    @SerializedName("total")
    private int total;

    /**
     * 函数列表
     */
    @SerializedName("functions")
    private List<Function> functions;

    /**
     * 获取函数总数
     *
     * @return 函数的总数量
     */
    public int getTotal() {
        return total;
    }

    /**
     * 获取函数列表
     *
     * @return 包含函数对象的列表
     */
    public List<Function> getFunctions() {
        return functions;
    }
} 