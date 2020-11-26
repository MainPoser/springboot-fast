package cn.com.datu.springboot.fast.common.plug;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
@Slf4j
public class MybatisSqlInterceptorConfig {

    private static List<String> sqlIds;

    private static String saveFilePath;

    private static String file_prefix;

    private static String sqlId_prefix;

    private static boolean plugin_enable;

    private static final String TRUE = "true";

    @PostConstruct
    public void init(){
        try {
            //todo 从配置文件读取配置
            /*plugin_enable = TRUE.equals(PropertyPlaceholderUtil.getContextProperty("sqlInterceptor.elsEnable").toString());
            sqlId_prefix = PropertyPlaceholderUtil.getContextProperty("sqlInterceptor.listPrefix").toString();
            sqlIds = Arrays.stream(PropertyPlaceholderUtil.getContextProperty("sqlInterceptor.list").toString().split(",")).map(o -> sqlId_prefix + o).collect(Collectors.toList());
            saveFilePath = PropertyPlaceholderUtil.getContextProperty("sqlInterceptor.saveFilePath").toString();
            file_prefix = PropertyPlaceholderUtil.getContextProperty("sqlInterceptor.filePrefix").toString();*/
        }catch (Exception e){
            log.error("init mybatis ELS failure");
        }
    }

    public static List<String> getSqlIds() {
        return sqlIds;
    }

    public static void setSqlIds(List<String> sqlIds) {
        MybatisSqlInterceptorConfig.sqlIds = sqlIds;
    }

    public static String getSaveFilePath() {
        return saveFilePath;
    }

    public static void setSaveFilePath(String saveFilePath) {
        MybatisSqlInterceptorConfig.saveFilePath = saveFilePath;
    }

    public static String getFile_prefix() {
        return file_prefix;
    }

    public static void setFile_prefix(String file_prefix) {
        MybatisSqlInterceptorConfig.file_prefix = file_prefix;
    }

    public static String getSqlId_prefix() {
        return sqlId_prefix;
    }

    public static void setSqlId_prefix(String sqlId_prefix) {
        MybatisSqlInterceptorConfig.sqlId_prefix = sqlId_prefix;
    }

    public static boolean isPlugin_enable() {
        return plugin_enable;
    }

    public static void setPlugin_enable(boolean plugin_enable) {
        MybatisSqlInterceptorConfig.plugin_enable = plugin_enable;
    }
}
