package br.com.accmd.easyFlix.domain.exception;

public class DuplicatedTupleException extends RuntimeException {
    public DuplicatedTupleException(String message) {
        super(message);
    }
}