package zbs.casclient;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;

import java.util.Collections;
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
    private static final String FILE_PATH_JAVA = "D://aaaaa";
    private static final String FILE_PATH_XML = "D://aaaaa";
    private static final String url_user =
            "jdbc:mysql://10.10.1.30:3307/syhd_user?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai";

    public static void main(String[] args) {
        FastAutoGenerator.create(
                        url_user,
                        "syhd",
                        "syhd!@#321")
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
                .execute();
    }

    public static Consumer<GlobalConfig.Builder> getGlobalConfig() {
        return builder -> builder.author("zbs") // 设置作者
//                .enableSwagger() // 开启 swagger 模式
                .commentDate("yyyy-MM-dd") //注释日期
                .outputDir(FILE_PATH_JAVA); // 指定输出目录
    }

    public static Consumer<PackageConfig.Builder> getPackageConfig() {
        return builder -> builder.parent("com.syhd") // 设置父包名
                .entity("model")
                .service("service")
                .service("service.impl")
                .mapper("mapper")
                .xml("mapper.xml")
                .pathInfo(Collections.singletonMap(OutputFile.xml, FILE_PATH_XML)); // 设置mapperXml生成路径
    }

    public static Consumer<TemplateConfig.Builder> getTemplateConfig() {
        return builder -> builder.xml(TEMPLATE_XML)
                .mapper(TEMPLATE_MAPPER)
                .entity(TEMPLATE_ENTITY)
                ;
    }
}
