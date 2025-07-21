package core.api;

import io.restassured.response.Response;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface ResponseValidator {
    void validate(Response response) throws ResponseValidationException;
}

class ResponseValidators {
    public static ResponseValidator statusCode(int... expectedStatusCodes) {
        Set<Integer> validCodes = new HashSet<>(Arrays.stream(expectedStatusCodes)
                .boxed().collect(Collectors.toSet()));

        return response -> {
            if (!validCodes.contains(response.getStatusCode())) {
                throw new ResponseValidationException(
                        "Unexpected status code: " + response.getStatusCode() +
                                ", expected one of: " + validCodes,
                        response
                );
            }
        };
    }

    public static ResponseValidator statusCodeSuccess() {
        return statusCode(200, 201, 202, 204);
    }

    public static ResponseValidator custom(Predicate<Response> validator, String errorMessage) {
        return response -> {
            if (!validator.test(response)) {
                throw new ResponseValidationException(errorMessage, response);
            }
        };
    }

    public static ResponseValidator none() {
        return response -> {};
    }
}
