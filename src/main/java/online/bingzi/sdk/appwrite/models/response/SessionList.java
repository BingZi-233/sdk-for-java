package online.bingzi.sdk.appwrite.models.response;

import com.google.gson.annotations.SerializedName;
import online.bingzi.sdk.appwrite.models.Session;

import java.util.List;

/**
 * Appwrite 会话列表响应模型
 * <p>
 * 该类表示从 Appwrite API 获取会话列表时的响应结构。
 * 包含总数和会话列表两个主要字段。
 * </p>
 */
public class SessionList {
    /**
     * 会话总数
     */
    @SerializedName("total")
    private int total;

    /**
     * 会话列表
     */
    @SerializedName("sessions")
    private List<Session> sessions;

    /**
     * 获取会话总数
     *
     * @return 会话的总数量
     */
    public int getTotal() {
        return total;
    }

    /**
     * 获取会话列表
     *
     * @return 包含会话对象的列表
     */
    public List<Session> getSessions() {
        return sessions;
    }
} 