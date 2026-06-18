package dev.felix2000jp.springboottemplate.system.openapi;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.modulith.NamedInterface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NamedInterface
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "404", ref = "#/components/responses/NotFound")
public @interface ApiResponseNotFound {
}
