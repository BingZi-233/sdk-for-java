package online.bingzi.sdk.appwrite.services.impl;

import online.bingzi.sdk.appwrite.Client;
import online.bingzi.sdk.appwrite.models.Execution;
import online.bingzi.sdk.appwrite.models.Function;
import online.bingzi.sdk.appwrite.models.response.ExecutionList;
import online.bingzi.sdk.appwrite.models.response.FunctionList;
import online.bingzi.sdk.appwrite.services.FunctionService;
import retrofit2.Call;

import java.util.List;
import java.util.Map;

/**
 * 函数服务实现类
 */
public class FunctionServiceImpl implements FunctionService {
    private final FunctionService functionService;

    public FunctionServiceImpl(Client client) {
        this.functionService = client.createService(FunctionService.class);
    }

    @Override
    public Call<Function> createFunction(String functionId, String name, String runtime,
                                       List<String> execute, List<String> events, String schedule,
                                       Integer timeout, Boolean enabled) {
        return functionService.createFunction(functionId, name, runtime, execute, events,
                schedule, timeout, enabled);
    }

    @Override
    public Call<FunctionList> listFunctions() {
        return functionService.listFunctions();
    }

    @Override
    public Call<Function> getFunction(String functionId) {
        return functionService.getFunction(functionId);
    }

    @Override
    public Call<Function> updateFunction(String functionId, String name, List<String> execute,
                                         List<String> events, String schedule, Integer timeout,
                                         Boolean enabled) {
        return functionService.updateFunction(functionId, name, execute, events,
                schedule, timeout, enabled);
    }

    @Override
    public Call<Void> deleteFunction(String functionId) {
        return functionService.deleteFunction(functionId);
    }

    @Override
    public Call<Execution> createExecution(String functionId, String data) {
        return functionService.createExecution(functionId, data);
    }

    @Override
    public Call<ExecutionList> listExecutions(String functionId) {
        return functionService.listExecutions(functionId);
    }

    @Override
    public Call<Execution> getExecution(String functionId, String executionId) {
        return functionService.getExecution(functionId, executionId);
    }

    @Override
    public Call<Function> updateVariables(String functionId, Map<String, String> variables) {
        return functionService.updateVariables(functionId, variables);
    }
} 