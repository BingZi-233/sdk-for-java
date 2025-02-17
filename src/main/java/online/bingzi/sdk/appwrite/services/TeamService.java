package online.bingzi.sdk.appwrite.services;

import online.bingzi.sdk.appwrite.models.Membership;
import online.bingzi.sdk.appwrite.models.Team;
import online.bingzi.sdk.appwrite.models.response.MembershipList;
import online.bingzi.sdk.appwrite.models.response.TeamList;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

/**
 * Appwrite 团队服务接口
 * <p>
 * 该接口提供了团队和团队成员管理的所有基本功能，包括：
 * - 团队的创建、更新和删除
 * - 团队成员管理
 * - 成员角色管理
 * - 团队偏好设置
 * </p>
 *
 * <h2>使用示例：</h2>
 * <pre>
 * // 创建团队服务实例
 * TeamService teamService = client.createService(TeamService.class);
 *
 * // 创建新团队
 * List<String> roles = Arrays.asList("admin", "developer");
 * Call<Team> createCall = teamService.createTeam(
 *     "dev-team",          // 团队ID
 *     "开发团队",          // 团队名称
 *     roles                // 可用角色
 * );
 *
 * createCall.enqueue(new Callback<Team>() {
 *     @Override
 *     public void onResponse(Call<Team> call, Response<Team> response) {
 *         if (response.isSuccessful()) {
 *             Team team = response.body();
 *             System.out.println("团队创建成功: " + team.getId());
 *
 *             // 邀请成员加入团队
 *             Call<Membership> inviteCall = teamService.createMembership(
 *                 team.getId(),
 *                 "developer@example.com",
 *                 Arrays.asList("developer"),
 *                 "https://example.com/accept-invite"
 *             );
 *         }
 *     }
 * });
 *
 * // 获取团队成员列表
 * Call<List<Membership>> listCall = teamService.listMemberships("dev-team");
 * listCall.enqueue(new Callback<List<Membership>>() {
 *     @Override
 *     public void onResponse(Call<List<Membership>> call,
 *                          Response<List<Membership>> response) {
 *         if (response.isSuccessful()) {
 *             List<Membership> members = response.body();
 *             for (Membership member : members) {
 *                 System.out.println("成员: " + member.getUserName());
 *                 System.out.println("角色: " + member.getRoles());
 *             }
 *         }
 *     }
 * });
 *
 * // 更新团队偏好设置
 * Map<String, Object> prefs = new HashMap<>();
 * prefs.put("notifications", true);
 * prefs.put("language", "zh-CN");
 * Call<Map<String, Object>> updatePrefsCall = teamService.updatePrefs(
 *     "dev-team",
 *     prefs
 * );
 * </pre>
 *
 * <h2>注意事项：</h2>
 * <ul>
 *   <li>团队ID必须是唯一的，且只能包含小写字母、数字和中划线</li>
 *   <li>团队名称不能为空，且长度不能超过128个字符</li>
 *   <li>角色名称只能包含小写字母、数字和中划线</li>
 *   <li>邀请链接必须是有效的HTTPS URL</li>
 *   <li>成员邮箱必须是有效的邮箱格式</li>
 *   <li>团队成员数量可能有上限限制</li>
 * </ul>
 */
public interface TeamService {
    /**
     * 创建团队
     * <p>
     * 创建一个新的团队。团队创建后可以邀请成员加入。
     * 创建团队的用户自动成为团队的所有者。
     * </p>
     *
     * @param teamId 团队唯一标识符，只能包含小写字母、数字和中划线
     * @param name   团队名称，用于显示
     * @param roles  团队可用的角色列表
     * @return 包含新创建团队信息的Call对象
     * @throws IllegalArgumentException 如果teamId格式无效或name为空
     */
    @FormUrlEncoded
    @POST("teams")
    Call<Team> createTeam(
            @Field("teamId") String teamId,
            @Field("name") String name,
            @Field("roles") List<String> roles
    );

    /**
     * 获取团队列表
     * <p>
     * 获取当前用户所属的所有团队。
     * 返回的列表按创建时间降序排序。
     * </p>
     *
     * @return 包含团队列表的Call对象
     */
    @GET("teams")
    Call<TeamList> listTeams();

    /**
     * 获取团队信息
     * <p>
     * 获取指定团队的详细信息。
     * 需要是团队成员才能访问。
     * </p>
     *
     * @param teamId 要查询的团队ID
     * @return 包含团队信息的Call对象
     */
    @GET("teams/{teamId}")
    Call<Team> getTeam(@Path("teamId") String teamId);

    /**
     * 更新团队信息
     * <p>
     * 更新指定团队的基本信息。
     * 需要是团队所有者或管理员。
     * </p>
     *
     * @param teamId 团队ID
     * @param name   新的团队名称
     * @return 包含更新后团队信息的Call对象
     */
    @FormUrlEncoded
    @PUT("teams/{teamId}")
    Call<Team> updateTeam(
            @Path("teamId") String teamId,
            @Field("name") String name
    );

    /**
     * 删除团队
     * <p>
     * 删除指定的团队及其所有成员关系。
     * 此操作不可逆，需要是团队所有者。
     * </p>
     *
     * @param teamId 要删除的团队ID
     * @return 空的Call对象
     */
    @DELETE("teams/{teamId}")
    Call<Void> deleteTeam(@Path("teamId") String teamId);

    /**
     * 获取团队成员列表
     * <p>
     * 获取指定团队的所有成员。
     * 返回的列表包含成员的详细信息和角色。
     * </p>
     *
     * @param teamId 团队ID
     * @return 包含成员列表的Call对象
     */
    @GET("teams/{teamId}/memberships")
    Call<MembershipList> listMemberships(@Path("teamId") String teamId);

    /**
     * 创建团队成员
     * <p>
     * 邀请新成员加入团队。系统会向指定邮箱发送邀请邮件。
     * 新成员需要通过邀请链接确认加入。
     * </p>
     *
     * @param teamId 团队ID
     * @param email  成员邮箱地址
     * @param roles  分配给成员的角色列表
     * @param url    邀请确认后的跳转地址
     * @return 包含成员关系信息的Call对象
     * @throws IllegalArgumentException 如果email格式无效或url不是HTTPS
     */
    @FormUrlEncoded
    @POST("teams/{teamId}/memberships")
    Call<Membership> createMembership(
            @Path("teamId") String teamId,
            @Field("email") String email,
            @Field("roles") List<String> roles,
            @Field("url") String url
    );

    /**
     * 获取团队成员信息
     * <p>
     * 获取指定成员的详细信息，包括角色和状态。
     * 需要是团队成员才能访问。
     * </p>
     *
     * @param teamId       团队ID
     * @param membershipId 成员关系ID
     * @return 包含成员信息的Call对象
     */
    @GET("teams/{teamId}/memberships/{membershipId}")
    Call<Membership> getMembership(
            @Path("teamId") String teamId,
            @Path("membershipId") String membershipId
    );

    /**
     * 更新团队成员信息
     * <p>
     * 更新指定成员的角色。
     * 需要是团队所有者或管理员。
     * </p>
     *
     * @param teamId       团队ID
     * @param membershipId 成员关系ID
     * @param roles        新的角色列表
     * @return 包含更新后成员信息的Call对象
     */
    @FormUrlEncoded
    @PATCH("teams/{teamId}/memberships/{membershipId}")
    Call<Membership> updateMembership(
            @Path("teamId") String teamId,
            @Path("membershipId") String membershipId,
            @Field("roles") List<String> roles
    );

    /**
     * 删除团队成员
     * <p>
     * 将成员从团队中移除。
     * 此操作不可逆，需要是团队所有者或管理员。
     * </p>
     *
     * @param teamId       团队ID
     * @param membershipId 要删除的成员关系ID
     * @return 空的Call对象
     */
    @DELETE("teams/{teamId}/memberships/{membershipId}")
    Call<Void> deleteMembership(
            @Path("teamId") String teamId,
            @Path("membershipId") String membershipId
    );

    /**
     * 更新团队偏好设置
     * <p>
     * 更新团队的自定义配置。新的设置会完全替换现有设置。
     * 需要是团队所有者或管理员。
     * </p>
     *
     * @param teamId 团队ID
     * @param prefs  新的偏好设置，键值对形式
     * @return 包含更新后偏好设置的Call对象
     */
    @FormUrlEncoded
    @PUT("teams/{teamId}/prefs")
    Call<Map<String, Object>> updatePrefs(
            @Path("teamId") String teamId,
            @FieldMap Map<String, Object> prefs
    );
} 