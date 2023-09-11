package uz.pdp.citymanagement_monolith.exception;

public class AuthFailedException extends RuntimeException{
    public AuthFailedException(String message) {
        super(message);
    }
}
