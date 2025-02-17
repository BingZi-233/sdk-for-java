package online.bingzi.sdk.appwrite.models.response;

import com.google.gson.annotations.SerializedName;
import online.bingzi.sdk.appwrite.models.Execution;

import java.util.List;

/**
 * Appwrite 函数执行记录列表响应模型
 * <p>
 * 该类表示从 Appwrite API 获取函数执行记录列表时的响应结构。
 * 包含总数和执行记录列表两个主要字段。
 * </p>
 */
public class ExecutionList {
    /**
     * 执行记录总数
     */
    @SerializedName("total")
    private int total;

    /**
     * 执行记录列表
     */
    @SerializedName("executions")
    private List<Execution> executions;

    /**
     * 获取执行记录总数
     *
     * @return 执行记录的总数量
     */
    public int getTotal() {
        return total;
    }

    /**
     * 获取执行记录列表
     *
     * @return 包含执行记录对象的列表
     */
    public List<Execution> getExecutions() {
        return executions;
    }
} 