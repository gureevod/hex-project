package core.api;

import io.qameta.allure.Allure;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class AllureReportingFilter implements Filter {
    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        String requestBody = requestSpec.getBody() != null ? requestSpec.getBody().toString() : "null";

        Allure.addAttachment("Request",
                "Method: " + requestSpec.getMethod() + "\n" +
                        "URI: " + requestSpec.getURI() + "\n" +
                        "Headers: " + requestSpec.getHeaders() + "\n" +
                        "Body: " + requestBody);

        Response response = ctx.next(requestSpec, responseSpec);

        String responseBody = response.getBody().asString();

        Allure.addAttachment("Response",
                "Status: " + response.getStatusCode() + "\n" +
                        "Headers: " + response.getHeaders() + "\n" +
                        "Body: " + responseBody);

        return response;
    }
}
