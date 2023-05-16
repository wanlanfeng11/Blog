package com.wansir.config;


import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/11 14:00
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket webApiConfig(){
        //配置Swagger的文档生成器
        return new Docket(DocumentationType.SWAGGER_2)
                //指定了API文档的分组名称，这在生成多个API文档时很有用。
                .groupName("webApi")
                //调用apiInfo方法,创建一个ApiInfo实例,里面是展示在文档页面信息内容
                .apiInfo(webApiInfo())
                //定义哪些API接口应该被包含在生成的文档中
                .select()
                //排除所有以"/admin/"开头的路径，这些路径可能是一些管理后台的接口，不希望在API文档中显示
                .paths(Predicates.not(PathSelectors.regex("/admin/.*")))
                //排除所有以"/error"开头的路径，这些路径通常是处理错误的接口，也不希望在API文档中显
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();

    }


    // api文档的详细信息
    private ApiInfo webApiInfo(){

        return new ApiInfoBuilder()
                .title("Blog接口文档")    //文档标题
                .description("本文档描述接口测试用例")  //文档描述
                .version("1.0")  //版本
                .contact(new Contact("java", "http://baidu.com", "123@qq.com"))//联系方式
                .build();
    }
}
