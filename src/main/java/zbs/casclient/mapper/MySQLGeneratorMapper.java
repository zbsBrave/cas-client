package zbs.casclient.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zbs
 * @since 2022/4/28 11:28
 */
public interface MySQLGeneratorMapper {
    List<Map<String, Object>> queryList(@Param("tableName") String tableName);

    Map<String, String> queryTable(String tableName);

    List<Map<String, String>> queryColumns(String tableName);
}
