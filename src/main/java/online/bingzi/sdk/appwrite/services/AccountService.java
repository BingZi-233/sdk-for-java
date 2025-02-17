package online.bingzi.sdk.appwrite.services;

import online.bingzi.sdk.appwrite.models.User;
import online.bingzi.sdk.appwrite.models.response.SessionList;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * Appwrite 账户服务接口
 * <p>
 * 该接口提供了用户账户管理的所有基本功能，包括：
 * - 用户注册和认证
 * - 会话管理
 * - 账户信息管理
 * - 密码重置
 * - 偏好设置管理
 * </p>
 *
 * <h2>使用示例：</h2>
 * <pre>
 * // 创建账户服务实例
 * AccountService accountService = client.createService(AccountService.class);
 *
 * // 创建新用户
 * Call<User> createCall = accountService.create(
 *     "user@example.com",     // 邮箱
 *     "strong-password",      // 密码
 *     "John Doe"              // 用户名
 * );
 *
 * createCall.enqueue(new Callback<User>() {
 *     @Override
 *     public void onResponse(Call<User> call, Response<User> response) {
 *         if (response.isSuccessful()) {
 *             User user = response.body();
 *             System.out.println("用户创建成功: " + user.getId());
 *         }
 *     }
 * });
 *
 * // 创建登录会话
 * Call<User> loginCall = accountService.createEmailSession(
 *     "user@example.com",     // 邮箱
 *     "strong-password"       // 密码
 * );
 *
 * // 获取当前用户信息
 * Call<User> getCall = accountService.get();
 *
 * // 更新用户偏好设置
 * Map<String, Object> prefs = new HashMap<>();
 * prefs.put("theme", "dark");
 * Call<Map<String, Object>> updateCall = accountService.updatePrefs(prefs);
 * </pre>
 *
 * <h2>注意事项：</h2>
 * <ul>
 *   <li>所有密码相关的操作都需要使用HTTPS</li>
 *   <li>密码必须符合安全要求（最少8位，包含大小写字母和数字）</li>
 *   <li>邮箱地址必须是有效的格式</li>
 *   <li>用户名不能包含特殊字符</li>
 *   <li>会话有效期默认为2周</li>
 * </ul>
 */
public interface AccountService {
    /**
     * 创建新用户
     * <p>
     * 通过邮箱和密码注册新用户。新用户创建后会自动生成唯一ID。
     * 如果邮箱已被使用，将返回409错误。
     * </p>
     *
     * @param email    用户邮箱地址，必须是有效的邮箱格式
     * @param password 用户密码，必须符合安全要求
     * @param name     用户显示名称，可选
     * @return 包含新创建用户信息的Call对象
     * @throws IllegalArgumentException 如果邮箱格式无效或密码不符合要求
     */
    @FormUrlEncoded
    @POST("account")
    Call<User> create(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String name
    );

    /**
     * 创建邮箱登录会话
     * <p>
     * 使用邮箱和密码创建新的登录会话。成功后会设置会话cookie。
     * 如果认证失败，将返回401错误。
     * </p>
     *
     * @param email    登录邮箱地址
     * @param password 登录密码
     * @return 包含登录用户信息的Call对象
     * @throws IllegalArgumentException 如果邮箱或密码为空
     */
    @FormUrlEncoded
    @POST("account/sessions/email")
    Call<User> createEmailSession(
            @Field("email") String email,
            @Field("password") String password
    );

    /**
     * 获取当前账户信息
     * <p>
     * 获取当前登录用户的详细信息。需要有效的会话。
     * 如果未登录，将返回401错误。
     * </p>
     *
     * @return 包含当前用户信息的Call对象
     */
    @GET("account")
    Call<User> get();

    /**
     * 获取账户偏好设置
     * <p>
     * 获取当前用户的所有偏好设置。需要有效的会话。
     * 偏好设置是一个键值对集合，可以存储用户的自定义配置。
     * </p>
     *
     * @return 包含偏好设置的Call对象
     */
    @GET("account/prefs")
    Call<Map<String, Object>> getPrefs();

    /**
     * 更新账户偏好设置
     * <p>
     * 更新当前用户的偏好设置。需要有效的会话。
     * 新的设置会完全替换现有设置。
     * </p>
     *
     * @param prefs 新的偏好设置，键值对形式
     * @return 包含更新后偏好设置的Call对象
     */
    @PATCH("account/prefs")
    Call<Map<String, Object>> updatePrefs(@Body Map<String, Object> prefs);

    /**
     * 创建密码重置请求
     * <p>
     * 发送密码重置邮件到指定邮箱。
     * 邮件中包含重置链接，链接有效期为1小时。
     * </p>
     *
     * @param email 需要重置密码的邮箱地址
     * @param url   重置密码完成后的跳转地址
     * @return 空的Call对象
     * @throws IllegalArgumentException 如果邮箱格式无效
     */
    @FormUrlEncoded
    @POST("account/recovery")
    Call<Void> createRecovery(
            @Field("email") String email,
            @Field("url") String url
    );

    /**
     * 确认密码重置
     * <p>
     * 使用重置令牌和新密码完成密码重置。
     * 如果令牌无效或过期，将返回401错误。
     * </p>
     *
     * @param userId        用户ID
     * @param secret        重置令牌
     * @param password      新密码
     * @param passwordAgain 确认新密码
     * @return 空的Call对象
     * @throws IllegalArgumentException 如果密码不匹配或不符合要求
     */
    @FormUrlEncoded
    @PUT("account/recovery")
    Call<Void> updateRecovery(
            @Field("userId") String userId,
            @Field("secret") String secret,
            @Field("password") String password,
            @Field("passwordAgain") String passwordAgain
    );

    /**
     * 删除指定会话
     * <p>
     * 使当前用户的指定会话失效。需要有效的会话。
     * 如果删除当前会话，用户将被登出。
     * </p>
     *
     * @param sessionId 要删除的会话ID
     * @return 空的Call对象
     */
    @DELETE("account/sessions/{sessionId}")
    Call<Void> deleteSession(@Path("sessionId") String sessionId);

    /**
     * 删除所有会话
     * <p>
     * 使当前用户的所有会话失效。需要有效的会话。
     * 调用此方法后，用户将被登出所有设备。
     * </p>
     *
     * @return 空的Call对象
     */
    @DELETE("account/sessions")
    Call<Void> deleteSessions();

    /**
     * 获取会话列表
     * <p>
     * 获取当前用户的所有活跃会话。
     * 返回的列表包含所有设备的会话信息。
     * </p>
     *
     * @return 包含会话列表的Call对象
     */
    @GET("account/sessions")
    Call<SessionList> listSessions();
} 