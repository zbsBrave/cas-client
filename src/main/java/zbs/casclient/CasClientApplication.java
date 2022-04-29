package zbs.casclient;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DateType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author zbs
 * @since 2022/4/28 18:51
 * <p>
 * xml中所需的参数，参考：TableInfo。
 * 表字段：fields
 * 公共字段：commonFields
 */
public class CasClientApplication {
    private static final String TEMPLATE_XML = "template-mybatis/mapper.xml.vm";
    private static final String TEMPLATE_MAPPER = "template-mybatis/mapper.java.vm";
    private static final String TEMPLATE_ENTITY = "template-mybatis/entity.java.vm";
    private static final String FILE_PATH_JAVA = "D://aaa-generator";
    private static final String FILE_PATH_XML = "D://aaa-generator";
    private static final String url_user =
            "jdbc:mysql://10.10.1.30:3307/syhd_user?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai";

    private static final String username = "syhd";
    private static final String pwd = "syhd!@#321";

    public static void main(String[] args) {
        FastAutoGenerator.create(getDataSourceConfig())
                .globalConfig(getGlobalConfig())
                .packageConfig(getPackageConfig())
                .strategyConfig(builder -> {
                    builder
                            .addInclude("app") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_") // 设置过滤表前缀
                            .entityBuilder()
                            .enableLombok().disableSerialVersionUID().fileOverride()
                            .serviceBuilder().fileOverride() //覆盖已有文件
                            .controllerBuilder().fileOverride()
                            .mapperBuilder().fileOverride().enableBaseColumnList().enableBaseResultMap();
                })
                .templateConfig(getTemplateConfig())
                .injectionConfig(getInjectionConfig()) //注入自定义配置
                .execute();
    }

    /**
     * 数据库配置
     */
    public static DataSourceConfig.Builder getDataSourceConfig() {
        return new DataSourceConfig.Builder(url_user, username, pwd)
                .typeConvert(new MySqlTypeConvert());
    }

    /**
     * 全局配置
     */
    public static Consumer<GlobalConfig.Builder> getGlobalConfig() {
        return builder -> builder.author("zbs") // 设置作者
//                .enableSwagger() // 开启 swagger 模式
                .dateType(DateType.ONLY_DATE) //指定日期类型映射为 java.util.Date。默认的是LocalDateTime
                .commentDate("yyyy-MM-dd") //注释日期
                .outputDir(FILE_PATH_JAVA); // 指定输出目录
    }

    /**
     * 包配置
     */
    public static Consumer<PackageConfig.Builder> getPackageConfig() {
        Map<OutputFile, String> map = new HashMap<>();
        map.put(OutputFile.xml,FILE_PATH_XML);
        map.put(OutputFile.other,FILE_PATH_XML);
        return builder -> builder.parent("com.syhd") // 设置父包名
                .entity("model")
                .service("service")
                .service("service.impl")
                .mapper("mapper")
                .xml("mapper.xml")
//                .pathInfo(Collections.singletonMap(OutputFile.xml, FILE_PATH_XML)) // 设置mapperXml生成路径
                .pathInfo(map) // 设置mapperXml生成路径
                ;
    }

    /**
     * 模板配置
     */
    public static Consumer<TemplateConfig.Builder> getTemplateConfig() {
        return builder -> builder.xml(TEMPLATE_XML)
                .mapper(TEMPLATE_MAPPER)
                .entity(TEMPLATE_ENTITY)
                .disable(TemplateType.CONTROLLER) //禁用模板
                ;
    }

    /**
     * 注入自定义配置
     */
    public static Consumer<InjectionConfig.Builder> getInjectionConfig() {
        return builder -> builder.beforeOutputFile((tableInfo, objectMap) -> {
                    System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size());
                })
                //自定义配置 Map 对象。例如以下配置，可以在模板文件中通过key=testKey，获取对应的值=testVal
                .customMap(Collections.singletonMap("testKey", "testVal"))
                //自定义配置模板文件
                .customFile(Collections.singletonMap("other.java", "template-mybatis/other.java.vm"))
                ;
    }
}
