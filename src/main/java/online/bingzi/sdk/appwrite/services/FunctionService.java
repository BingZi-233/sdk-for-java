package online.bingzi.sdk.appwrite.services;

import online.bingzi.sdk.appwrite.models.Execution;
import online.bingzi.sdk.appwrite.models.Function;
import online.bingzi.sdk.appwrite.models.response.ExecutionList;
import online.bingzi.sdk.appwrite.models.response.FunctionList;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

/**
 * Appwrite 函数服务接口
 * <p>
 * 该接口提供了云函数（Functions）管理的所有基本功能，包括：
 * - 函数的创建、更新和删除
 * - 函数执行管理
 * - 函数变量配置
 * - 执行记录查询
 * </p>
 *
 * <h2>使用示例：</h2>
 * <pre>
 * // 创建函数服务实例
 * FunctionService functionService = client.createService(FunctionService.class);
 *
 * // 创建新函数
 * List<String> execute = Arrays.asList("users");
 * List<String> events = Arrays.asList("users.*.create");
 *
 * Call<Function> createCall = functionService.createFunction(
 *     "user_welcome",      // 函数ID
 *     "用户欢迎邮件",      // 函数名称
 *     "node-16.0",         // 运行时环境
 *     execute,             // 执行权限
 *     events,              // 触发事件
 *     "0 8 * * *",        // 定时计划（每天早8点）
 *     30,                  // 超时时间（秒）
 *     true                 // 是否启用
 * );
 *
 * // 设置函数环境变量
 * Map<String, String> variables = new HashMap<>();
 * variables.put("SMTP_HOST", "smtp.example.com");
 * variables.put("SMTP_PORT", "587");
 *
 * Call<Function> updateVarsCall = functionService.updateVariables(
 *     "user_welcome",      // 函数ID
 *     variables            // 环境变量
 * );
 *
 * // 执行函数
 * String data = "{\"userId\": \"123\", \"email\": \"user@example.com\"}";
 * Call<Execution> execCall = functionService.createExecution(
 *     "user_welcome",      // 函数ID
 *     data                 // 执行参数
 * );
 *
 * execCall.enqueue(new Callback<Execution>() {
 *     @Override
 *     public void onResponse(Call<Execution> call, Response<Execution> response) {
 *         if (response.isSuccessful()) {
 *             Execution execution = response.body();
 *             System.out.println("执行ID: " + execution.getId());
 *             System.out.println("状态: " + execution.getStatus());
 *             System.out.println("输出: " + execution.getStdout());
 *
 *             if (execution.getStatusCode() != 200) {
 *                 System.out.println("错误: " + execution.getStderr());
 *             }
 *         }
 *     }
 * });
 * </pre>
 *
 * <h2>注意事项：</h2>
 * <ul>
 *   <li>函数ID必须是唯一的，且只能包含小写字母、数字和下划线</li>
 *   <li>运行时环境必须是平台支持的版本</li>
 *   <li>定时计划使用Cron表达式格式</li>
 *   <li>超时时间不能超过平台限制（通常是120秒）</li>
 *   <li>环境变量的值会被加密存储</li>
 *   <li>函数执行可能是异步的，需要通过执行ID查询结果</li>
 * </ul>
 */
public interface FunctionService {
    /**
     * 创建函数
     * <p>
     * 创建一个新的云函数。函数创建后需要部署代码才能执行。
     * 函数可以通过HTTP调用、事件触发或定时任务执行。
     * </p>
     *
     * @param functionId 函数唯一标识符，只能包含小写字母、数字和下划线
     * @param name      函数名称，用于显示
     * @param runtime   运行时环境，如 "node-16.0", "python-3.9"
     * @param execute   执行权限列表，定义哪些角色可以执行此函数
     * @param events    触发事件列表，定义哪些系统事件会触发此函数
     * @param schedule  定时计划，使用Cron表达式格式
     * @param timeout   执行超时时间（秒），默认为15秒
     * @param enabled   是否启用函数
     * @return 包含新创建函数信息的Call对象
     * @throws IllegalArgumentException 如果functionId格式无效或runtime不支持
     */
    @FormUrlEncoded
    @POST("functions")
    Call<Function> createFunction(
            @Field("functionId") String functionId,
            @Field("name") String name,
            @Field("runtime") String runtime,
            @Field("execute") List<String> execute,
            @Field("events") List<String> events,
            @Field("schedule") String schedule,
            @Field("timeout") Integer timeout,
            @Field("enabled") Boolean enabled
    );

    /**
     * 获取函数列表
     * <p>
     * 获取当前项目中的所有函数。
     * 返回的列表按创建时间降序排序。
     * </p>
     *
     * @return 包含函数列表的Call对象
     */
    @GET("functions")
    Call<FunctionList> listFunctions();

    /**
     * 获取函数信息
     * <p>
     * 获取指定函数的详细信息，包括配置、状态和统计数据。
     * 如果函数不存在，将返回404错误。
     * </p>
     *
     * @param functionId 要查询的函数ID
     * @return 包含函数信息的Call对象
     */
    @GET("functions/{functionId}")
    Call<Function> getFunction(@Path("functionId") String functionId);

    /**
     * 更新函数信息
     * <p>
     * 更新指定函数的配置信息。
     * 只更新提供的字段，未提供的字段保持不变。
     * </p>
     *
     * @param functionId 函数ID
     * @param name      新的函数名称
     * @param execute   新的执行权限列表
     * @param events    新的触发事件列表
     * @param schedule  新的定时计划
     * @param timeout   新的超时时间（秒）
     * @param enabled   是否启用函数
     * @return 包含更新后函数信息的Call对象
     */
    @FormUrlEncoded
    @PUT("functions/{functionId}")
    Call<Function> updateFunction(
            @Path("functionId") String functionId,
            @Field("name") String name,
            @Field("execute") List<String> execute,
            @Field("events") List<String> events,
            @Field("schedule") String schedule,
            @Field("timeout") Integer timeout,
            @Field("enabled") Boolean enabled
    );

    /**
     * 删除函数
     * <p>
     * 删除指定的函数及其所有执行记录和变量。
     * 此操作不可逆，请谨慎使用。
     * </p>
     *
     * @param functionId 要删除的函数ID
     * @return 空的Call对象
     */
    @DELETE("functions/{functionId}")
    Call<Void> deleteFunction(@Path("functionId") String functionId);

    /**
     * 创建函数执行
     * <p>
     * 触发函数执行。可以通过data参数传递执行参数。
     * 执行可能是异步的，需要通过执行ID查询结果。
     * </p>
     *
     * @param functionId 要执行的函数ID
     * @param data      执行参数，JSON格式的字符串
     * @return 包含执行信息的Call对象
     * @throws IllegalArgumentException 如果data不是有效的JSON
     */
    @FormUrlEncoded
    @POST("functions/{functionId}/executions")
    Call<Execution> createExecution(
            @Path("functionId") String functionId,
            @Field("data") String data
    );

    /**
     * 获取函数执行列表
     * <p>
     * 获取指定函数的所有执行记录。
     * 返回的列表按执行时间降序排序。
     * </p>
     *
     * @param functionId 函数ID
     * @return 包含执行记录列表的Call对象
     */
    @GET("functions/{functionId}/executions")
    Call<ExecutionList> listExecutions(@Path("functionId") String functionId);

    /**
     * 获取函数执行信息
     * <p>
     * 获取指定执行记录的详细信息，包括执行状态、输出和错误信息。
     * 如果执行记录不存在，将返回404错误。
     * </p>
     *
     * @param functionId  函数ID
     * @param executionId 执行记录ID
     * @return 包含执行记录信息的Call对象
     */
    @GET("functions/{functionId}/executions/{executionId}")
    Call<Execution> getExecution(
            @Path("functionId") String functionId,
            @Path("executionId") String executionId
    );

    /**
     * 更新函数变量
     * <p>
     * 更新函数的环境变量。新的变量集合会完全替换现有变量。
     * 变量值会被加密存储，只能在函数执行时访问。
     * </p>
     *
     * @param functionId 函数ID
     * @param variables 新的环境变量集合，键值对形式
     * @return 包含更新后函数信息的Call对象
     */
    @FormUrlEncoded
    @PUT("functions/{functionId}/variables")
    Call<Function> updateVariables(
            @Path("functionId") String functionId,
            @FieldMap Map<String, String> variables
    );
} 