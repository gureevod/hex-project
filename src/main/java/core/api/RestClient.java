package core.api;

import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class RestClient {

    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);
    private static final int DEFAULT_MAX_RETRIES = 2; // Or from Config
    private static final long DEFAULT_RETRY_DELAY_MILLIS = 1000; //Or from config

    // Prevent instantiation
    private RestClient() {}

    public static Response get(String path) {
        return get(path, RestAssured.given(), DEFAULT_MAX_RETRIES);
    }
    public static Response get(String path, RequestSpecification requestSpec) {
        return get(path, requestSpec, DEFAULT_MAX_RETRIES);
    }

    public static Response get(String path, RequestSpecification requestSpec, int maxRetries) {
        return executeWithRetry(() -> sendRequest("GET", path, requestSpec), maxRetries, getConfiguredRetryDelay());
    }

    public static Response post(String path, Object body) {
        return post(path, body,  RestAssured.given(), DEFAULT_MAX_RETRIES);
    }
    public static Response post(String path, Object body, RequestSpecification requestSpec) {
        return post(path, body,  requestSpec, DEFAULT_MAX_RETRIES);
    }

    public static Response post(String path, Object body, RequestSpecification requestSpec, int maxRetries) {
        return executeWithRetry(() -> sendRequest("POST", path, requestSpec.body(body)), maxRetries, getConfiguredRetryDelay());
    }

    public static Response put(String path, Object body) {
        return put(path, body, RestAssured.given(), DEFAULT_MAX_RETRIES);
    }

    public static Response put(String path, Object body, RequestSpecification requestSpec) {
        return put(path, body, requestSpec, DEFAULT_MAX_RETRIES);
    }
    public static Response put(String path, Object body, RequestSpecification requestSpec, int maxRetries) {
        return executeWithRetry(() -> sendRequest("PUT", path, requestSpec.body(body)), maxRetries, getConfiguredRetryDelay());
    }

    public static Response delete(String path) {
        return delete(path, RestAssured.given(), DEFAULT_MAX_RETRIES);
    }

    public static Response delete(String path, RequestSpecification requestSpec) {
        return delete(path, requestSpec, DEFAULT_MAX_RETRIES);
    }
    public static Response delete(String path, RequestSpecification requestSpec, int maxRetries) {
        return executeWithRetry(() -> sendRequest("DELETE", path, requestSpec), maxRetries, getConfiguredRetryDelay());
    }
    public static Response patch(String path, Object body) {
        return patch(path, body, RestAssured.given(), DEFAULT_MAX_RETRIES);
    }
    public static Response patch(String path, Object body, RequestSpecification requestSpec) {
        return patch(path, body, requestSpec, DEFAULT_MAX_RETRIES);
    }
    public static Response patch(String path, Object body, RequestSpecification requestSpec, int maxRetries) {
        return executeWithRetry(() -> sendRequest("PATCH", path, requestSpec.body(body)), maxRetries, getConfiguredRetryDelay());
    }
    private static Response sendRequest(String method, String path, RequestSpecification requestSpec) {
        // Apply Allure filter here, rather than baking it into a base spec
        RequestSpecification finalSpec = requestSpec.filter(new AllureReportingFilter());

        return RestAssured.given()
                .spec(finalSpec)
                .request(method, path);
    }

    private static Response executeWithRetry(RequestSupplier request, int maxRetries, long retryDelayMillis) {
        AtomicReference<Response> response = new AtomicReference<>();
        int attempts = 0;
        while (true) {
            try {
                response.set(request.get());
                return response.get();
            } catch (Exception e) {
                attempts++;
                if (attempts > maxRetries) {
                    throw new RestClientException("Request failed after " + maxRetries + " retries.", e, null);
                }
                logger.warn("Attempt {} failed: {}. Retrying in {}ms", attempts, e.getMessage(), retryDelayMillis);
                try {
                    TimeUnit.MILLISECONDS.sleep(retryDelayMillis);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                    throw new RestClientException("Request interrupted.", ignored, null);
                }
            }
        }
    }

    public static RequestSpecification jsonConfig() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    public static RequestSpecification xmlConfig() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.XML)
                .setAccept(ContentType.XML)
                .build();
    }

    private static class AllureReportingFilter implements Filter {
        @Override
        public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
            String requestBody = requestSpec.getBody() != null ? requestSpec.getBody().toString() : "null";
            //String requestBody = requestSpec.getBody(); // for raw bytes representation

            Allure.addAttachment("Request",
                    "Method: " + requestSpec.getMethod() + "\n" +
                            "URI: " + requestSpec.getURI() + "\n" +
                            "Headers: " + requestSpec.getHeaders() + "\n" +
                            "Body: " + requestBody );

            Response response = ctx.next(requestSpec, responseSpec);

            String responseBody = response.getBody().asString();

            Allure.addAttachment("Response",
                    "Status: " + response.getStatusCode() + "\n" +
                            "Headers: " + response.getHeaders() + "\n" +
                            "Body: " + responseBody);

            return response;
        }
    }
    public static class RestClientException extends RuntimeException {
        private final transient Response response;

        public RestClientException(String message, Throwable cause, Response response) {
            super(message, cause);
            this.response = response;
        }

        public Response getResponse() {
            return response;
        }
    }

    @FunctionalInterface
    private interface RequestSupplier {
        Response get() throws Exception;
    }

    // Placeholder for config retrieval.  Replace with your actual config mechanism.
    private static long getConfiguredRetryDelay() {
        // Example: return ConfigFactory.load().getLong("api.retry.delayMillis");
        // Or, use Owner:  return ConfigProvider.getConfig().getRetryDelayMillis();
        return DEFAULT_RETRY_DELAY_MILLIS; //Fall back to default value
    }
}


