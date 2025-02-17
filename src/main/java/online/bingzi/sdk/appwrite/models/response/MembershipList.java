package online.bingzi.sdk.appwrite.models.response;

import com.google.gson.annotations.SerializedName;
import online.bingzi.sdk.appwrite.models.Membership;

import java.util.List;

/**
 * Appwrite 团队成员列表响应模型
 * <p>
 * 该类表示从 Appwrite API 获取团队成员列表时的响应结构。
 * 包含总数和成员列表两个主要字段。
 * </p>
 */
public class MembershipList {
    /**
     * 成员总数
     */
    @SerializedName("total")
    private int total;

    /**
     * 成员列表
     */
    @SerializedName("memberships")
    private List<Membership> memberships;

    /**
     * 获取成员总数
     *
     * @return 成员的总数量
     */
    public int getTotal() {
        return total;
    }

    /**
     * 获取成员列表
     *
     * @return 包含成员对象的列表
     */
    public List<Membership> getMemberships() {
        return memberships;
    }
} 