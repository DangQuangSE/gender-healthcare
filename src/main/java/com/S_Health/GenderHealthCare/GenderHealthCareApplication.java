package com.S_Health.GenderHealthCare;

import com.S_Health.GenderHealthCare.common.message.ApiDocumentationMessages;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = ApiDocumentationMessages.API_TITLE,
        version = ApiDocumentationMessages.API_VERSION,
        description = ApiDocumentationMessages.API_DESCRIPTION))
@SecurityScheme(name = "api", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@EnableScheduling
@EnableJpaAuditing
public class GenderHealthCareApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenderHealthCareApplication.class, args);
	}

}
