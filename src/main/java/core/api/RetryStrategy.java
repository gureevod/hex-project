package core.api;

import io.restassured.response.Response;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public interface RetryStrategy {
    boolean shouldRetry(Response response, Exception exception, int attemptNumber);
}

class RetryStrategies {
    public static RetryStrategy onServerErrors() {
        return (response, exception, attemptNumber) ->
                exception != null || (response != null && response.getStatusCode() >= 500);
    }

    public static RetryStrategy onSpecificStatusCodes(int... statusCodes) {
        Set<Integer> codes = Arrays.stream(statusCodes).boxed().collect(Collectors.toSet());
        return (response, exception, attemptNumber) ->
                exception != null || (response != null && codes.contains(response.getStatusCode()));
    }

    public static RetryStrategy onAnyError() {
        return (response, exception, attemptNumber) ->
                exception != null || (response != null && response.getStatusCode() >= 400);
    }

    public static RetryStrategy never() {
        return (response, exception, attemptNumber) -> false;
    }

    public static RetryStrategy always() {
        return (response, exception, attemptNumber) -> true;
    }
}
