package cn.com.datu.springboot.arcsoft.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
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
 * @author: chenyda
 * @date: 2019/11/15 16:00
 * @description: swagger配置
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    @Value("${swagger.show:true}")
    public boolean swaggerShow;

    @Bean
    public Docket buildDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerShow)
                .apiInfo(buildApiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title("北向接口微服务")
                .description("")
                .contact(new Contact("tianyao", "www.datu.com", "tianyao@dragoninfo.com.cn"))
                .version("1")
                .build();
    }

}
