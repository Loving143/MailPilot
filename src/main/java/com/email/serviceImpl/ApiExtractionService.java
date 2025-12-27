package com.email.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.email.handler.ApiInfo;

@Service
public class ApiExtractionService {

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    public List<ApiInfo> extractAllApis() {

        List<ApiInfo> apis = new ArrayList<>();

        handlerMapping.getHandlerMethods().forEach((mapping, handlerMethod) -> {

            String controllerName =
                    handlerMethod.getBeanType().getSimpleName();

            String httpMethod =
                    mapping.getMethodsCondition().toString();

            // ✅ FIXED URL EXTRACTION (Spring Boot 3 safe)
            String url;
            if (mapping.getPathPatternsCondition() != null) {
                url = mapping.getPathPatternsCondition()
                             .getPatternValues()
                             .toString();
            } else if (mapping.getPatternsCondition() != null) {
                url = mapping.getPatternsCondition()
                             .getPatterns()
                             .toString();
            } else {
                url = "N/A";
            }

            String requestBody =
                    Arrays.stream(handlerMethod.getMethodParameters())
                            .filter(p -> p.hasParameterAnnotation(RequestBody.class))
                            .map(p -> p.getParameterType().getSimpleName())
                            .findFirst()
                            .orElse("N/A");

            apis.add(new ApiInfo(
                    controllerName,
                    httpMethod,
                    url,
                    requestBody
            ));
        });

        return apis;
    }
}
