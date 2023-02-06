package com.example.mingchuapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: zhangwentao
 * @CreateTime: 2023/2/6
 * @Description: TODO
 * @Version: 1.0
 */


@Configuration
@EnableSwagger2
public class Swagger2 {

    /**
     * 配置swagger2核心配置
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {
        // 指定api类型为swagger2
        return new Docket(DocumentationType.SWAGGER_2)
                // 定义api文档汇总信息
                .apiInfo(apiInfo())
                // 指定需要提供文档的Controller类所在的包
                .select().apis(RequestHandlerSelectors.basePackage("com.example.mingchuapi.controller"))
                // 需要生成文档的接口路径
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("明厨亮灶监管平台 B01-B04 API")
                .contact(new Contact(
                        "zhangwentao",
                        "zhangwentao@js.chinamobile.com",
                        "zhangwentao@js.chinamobile.com"))
                .description("明厨亮灶监管平台 - 接口标准（B01-B04）")
                .version("1.0.1")
                .termsOfServiceUrl("zhangwentao@js.chinamobile.com")
                .build();
    }


}