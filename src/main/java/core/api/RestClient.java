package core.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.List;

public class RestClient {
    private final RequestSpecification baseSpec;
    private final int maxRetries;
    private final long retryDelayMillis;
    private final List<Filter> filters;
    private final RetryStrategy retryStrategy;
    private final ResponseValidator responseValidator;

    private RestClient(Builder builder) {
        this.baseSpec = builder.baseSpec;
        this.maxRetries = builder.maxRetries;
        this.retryDelayMillis = builder.retryDelayMillis;
        this.filters = new ArrayList<>(builder.filters);
        this.retryStrategy = builder.retryStrategy;
        this.responseValidator = builder.responseValidator;
    }

    public RequestBuilder get(String path) {
        return new RequestBuilder(this, "GET", path);
    }

    public RequestBuilder post(String path) {
        return new RequestBuilder(this, "POST", path);
    }

    public RequestBuilder put(String path) {
        return new RequestBuilder(this, "PUT", path);
    }

    public RequestBuilder delete(String path) {
        return new RequestBuilder(this, "DELETE", path);
    }

    public RequestBuilder patch(String path) {
        return new RequestBuilder(this, "PATCH", path);
    }

    /**
     * Create a request with a custom specification for this particular request
     */
    public RequestBuilder get(String path, RequestSpecification customSpec) {
        return new RequestBuilder(this, "GET", path).specification(customSpec);
    }

    public RequestBuilder post(String path, RequestSpecification customSpec) {
        return new RequestBuilder(this, "POST", path).specification(customSpec);
    }

    public RequestBuilder put(String path, RequestSpecification customSpec) {
        return new RequestBuilder(this, "PUT", path).specification(customSpec);
    }

    public RequestBuilder delete(String path, RequestSpecification customSpec) {
        return new RequestBuilder(this, "DELETE", path).specification(customSpec);
    }

    public RequestBuilder patch(String path, RequestSpecification customSpec) {
        return new RequestBuilder(this, "PATCH", path).specification(customSpec);
    }

    /**
     * Generic method for creating a request with any HTTP method and custom specification
     */
    public RequestBuilder request(String method, String path, RequestSpecification customSpec) {
        return new RequestBuilder(this, method, path).specification(customSpec);
    }

    public static Builder builder() {
        return new Builder();
    }

    // Factory methods for common configurations
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

    public static RequestSpecification formConfig() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.URLENC)
                .build();
    }

    public static RequestSpecification multipartConfig() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.MULTIPART)
                .build();
    }

    // Getters for internal use
    RequestSpecification getBaseSpec() {
        return baseSpec;
    }

    int getMaxRetries() {
        return maxRetries;
    }

    long getRetryDelayMillis() {
        return retryDelayMillis;
    }

    List<Filter> getFilters() {
        return filters;
    }

    RetryStrategy getRetryStrategy() {
        return retryStrategy;
    }

    ResponseValidator getResponseValidator() {
        return responseValidator;
    }

    public static class Builder {
        private RequestSpecification baseSpec = RestAssured.given();
        private int maxRetries = 2;
        private long retryDelayMillis = 1000;
        private List<Filter> filters = new ArrayList<>();
        private RetryStrategy retryStrategy = RetryStrategies.onServerErrors();
        private ResponseValidator responseValidator = ResponseValidators.statusCodeSuccess();

        public Builder baseSpec(RequestSpecification baseSpec) {
            this.baseSpec = baseSpec;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder retryDelayMillis(long retryDelayMillis) {
            this.retryDelayMillis = retryDelayMillis;
            return this;
        }

        public Builder addFilter(Filter filter) {
            this.filters.add(filter);
            return this;
        }

        public Builder withAllureReporting(boolean enabled) {
            if (enabled) {
                this.filters.add(new AllureReportingFilter());
            }
            return this;
        }

        public Builder retryStrategy(RetryStrategy retryStrategy) {
            this.retryStrategy = retryStrategy;
            return this;
        }

        public Builder responseValidator(ResponseValidator responseValidator) {
            this.responseValidator = responseValidator;
            return this;
        }

        public RestClient build() {
            return new RestClient(this);
        }
    }
}
