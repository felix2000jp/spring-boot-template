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
@ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest")
public @interface ApiResponseBadRequest {
}
