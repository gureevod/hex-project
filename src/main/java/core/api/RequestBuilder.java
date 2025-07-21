package core.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestBuilder {
    private static final Logger logger = LoggerFactory.getLogger(RequestBuilder.class);

    private final RestClient client;
    private final String method;
    private final String path;
    private RequestSpecification requestSpec;
    private Object body;
    private final Map<String, Object> pathParams = new HashMap<>();
    private final Map<String, Object> queryParams = new HashMap<>();
    private final Map<String, Object> formParams = new HashMap<>();

    RequestBuilder(RestClient client, String method, String path) {
        this.client = client;
        this.method = method;
        this.path = path;
        this.requestSpec = RestAssured.given().spec(client.getBaseSpec());

        // Apply all filters
        client.getFilters().forEach(requestSpec::filter);
    }

    /**
     * Override the current request specification with a custom one
     * while maintaining the client's filters
     */
    public RequestBuilder specification(RequestSpecification customSpec) {
        this.requestSpec = RestAssured.given().spec(customSpec);

        // Re-apply all filters from the client
        client.getFilters().forEach(requestSpec::filter);

        return this;
    }


    public RequestBuilder body(Object body) {
        this.body = body;
        return this;
    }

    public RequestBuilder header(String name, Object value) {
        requestSpec.header(name, value);
        return this;
    }

    public RequestBuilder headers(Map<String, ?> headers) {
        requestSpec.headers(headers);
        return this;
    }

    public RequestBuilder pathParam(String name, Object value) {
        pathParams.put(name, value);
        return this;
    }

    public RequestBuilder queryParam(String name, Object value) {
        queryParams.put(name, value);
        return this;
    }

    public RequestBuilder formParam(String name, Object value) {
        formParams.put(name, value);
        return this;
    }

    public RequestBuilder contentType(String contentType) {
        requestSpec.contentType(contentType);
        return this;
    }

    public Response execute() {
        // Apply all parameters
        requestSpec.pathParams(pathParams);
        queryParams.forEach(requestSpec::queryParam);
        formParams.forEach(requestSpec::formParam);

        if (body != null) {
            requestSpec.body(body);
        }

        // Execute with retry
        Response response = executeWithRetry();

        // Validate response
        if (client.getResponseValidator() != null) {
            client.getResponseValidator().validate(response);
        }

        return response;
    }

    public <T> T execute(Class<T> responseType) {
        Response response = execute();
        return response.as(responseType);
    }

    private Response executeWithRetry() {
        Response response = null;
        Exception lastException = null;

        for (int attempt = 0; attempt <= client.getMaxRetries(); attempt++) {
            try {
                if (attempt > 0) {
                    logger.info("Retry attempt {} of {}", attempt, client.getMaxRetries());
                    TimeUnit.MILLISECONDS.sleep(client.getRetryDelayMillis());
                }

                response = RestAssured.given(requestSpec).request(method, path);

                // Check if we should retry based on response
                if (attempt < client.getMaxRetries() &&
                        client.getRetryStrategy().shouldRetry(response, null, attempt)) {
                    logger.warn("Retrying request due to response: {} {}",
                            response.getStatusCode(), response.getStatusLine());
                    continue;
                }

                return response;

            } catch (Exception e) {
                lastException = e;
                logger.warn("Request failed: {}", e.getMessage());

                if (attempt >= client.getMaxRetries() ||
                        !client.getRetryStrategy().shouldRetry(null, e, attempt)) {
                    break;
                }
            }
        }

        if (response != null) {
            return response;
        } else {
            throw new RestClientException("Request failed after " + client.getMaxRetries() + " retries",
                    lastException, null);
        }
    }
}
