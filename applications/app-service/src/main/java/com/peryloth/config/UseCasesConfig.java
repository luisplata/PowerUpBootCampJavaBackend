package com.peryloth.config;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "com.peryloth.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {
}
