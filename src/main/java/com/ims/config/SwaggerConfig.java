package com.ims.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * 类名:com.toonan.config.SwaggerConfig
 * 描述:Swagger2接口配置文件
 * 编写者:陈骑元
 * 创建时间:2018年12月17日 上午1:10:40
 * 修改说明:
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	// 定义分隔符
    private static final String splitor = ";";

    /**
     * 创建API应用
     * api() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     *
     * @return
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
 .apis(basePackage("com.toonan.system.controller"+splitor+"com.toonan.mj.controller"+splitor+"com.toonan.appApi.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    /**
     * 
     * 简要说明：构建多个api说明
     * 编写者：陈骑元
     * 创建时间：2018年12月17日 上午1:33:31
     * @param 说明
     * @return 说明
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("webplus接口列表")
                .description("接口")
                .contact(new Contact("广州市图南软件科技有限公司", "http://www.toonan.com/", "toonan_sales@toonan.com"))
                .version("1.0.0")
                .build();
    }

    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage)     {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(splitor)) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }


    



    
}
