package exception;

public class BadRequestException extends ApiException {
    public BadRequestException(String msg) {
        super(400, msg);
    }
}