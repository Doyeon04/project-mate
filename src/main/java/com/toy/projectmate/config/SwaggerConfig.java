package com.toy.projectmate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)// 기본 응답 코드
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.toy.projectmate")) // 스캔할 패키지 범위
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() { // swagger ui로 노출할 정보
        return new ApiInfoBuilder()
                .title("project-mate Swagger")
                .description("project-mate 프로젝트 Swagger입니다.")
                .version("3.0")
                .build();
    }
}
