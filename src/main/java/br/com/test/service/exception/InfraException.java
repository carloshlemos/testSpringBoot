package br.com.test.service.exception;

public class InfraException extends RuntimeException {
    private static final long serialVersionUID = -6472789855877680893L;

    public InfraException(String message) {
        super(message);
    }

    public InfraException(String message, Throwable cause) {
        super(message, cause);
    }

    public InfraException(Throwable cause) {
        super(cause);
    }

    public static InfraException raise(String message, Throwable cause) {
        throw new InfraException(message, cause);
    }

    public static InfraException raise(String message) {
        throw new InfraException(message);
    }

    public static InfraException raise(Throwable e) {
        throw new InfraException(e);
    }
}