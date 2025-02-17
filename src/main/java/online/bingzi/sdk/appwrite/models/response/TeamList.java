package online.bingzi.sdk.appwrite.models.response;

import com.google.gson.annotations.SerializedName;
import online.bingzi.sdk.appwrite.models.Team;

import java.util.List;

/**
 * Appwrite 团队列表响应模型
 * <p>
 * 该类表示从 Appwrite API 获取团队列表时的响应结构。
 * 包含总数和团队列表两个主要字段。
 * </p>
 */
public class TeamList {
    /**
     * 团队总数
     */
    @SerializedName("total")
    private int total;

    /**
     * 团队列表
     */
    @SerializedName("teams")
    private List<Team> teams;

    /**
     * 获取团队总数
     *
     * @return 团队的总数量
     */
    public int getTotal() {
        return total;
    }

    /**
     * 获取团队列表
     *
     * @return 包含团队对象的列表
     */
    public List<Team> getTeams() {
        return teams;
    }
} 