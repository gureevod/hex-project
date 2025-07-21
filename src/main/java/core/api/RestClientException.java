package core.api;

import io.restassured.response.Response;

public class RestClientException extends RuntimeException {
    private final transient Response response;

    public RestClientException(String message, Throwable cause, Response response) {
        super(message, cause);
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}

class ResponseValidationException extends RestClientException {
    public ResponseValidationException(String message, Response response) {
        super(message, null, response);
    }
}
