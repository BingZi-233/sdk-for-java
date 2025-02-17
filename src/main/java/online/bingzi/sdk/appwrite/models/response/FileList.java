package online.bingzi.sdk.appwrite.models.response;

import com.google.gson.annotations.SerializedName;
import online.bingzi.sdk.appwrite.models.File;

import java.util.List;

/**
 * Appwrite 文件列表响应模型
 * <p>
 * 该类表示从 Appwrite API 获取文件列表时的响应结构。
 * 包含总数和文件列表两个主要字段。
 * </p>
 */
public class FileList {
    /**
     * 文件总数
     */
    @SerializedName("total")
    private int total;

    /**
     * 文件列表
     */
    @SerializedName("files")
    private List<File> files;

    /**
     * 获取文件总数
     *
     * @return 文件的总数量
     */
    public int getTotal() {
        return total;
    }

    /**
     * 获取文件列表
     *
     * @return 包含文件对象的列表
     */
    public List<File> getFiles() {
        return files;
    }
} 