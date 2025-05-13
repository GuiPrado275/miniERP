package com.projetominiERP.miniERP.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor //required = viriáveis finais
@JsonInclude(JsonInclude.Include.NON_NULL) //nao inclui mensagens nulas no console
public class ErrorResponse {

    private final int status;
    private final String message;
    private String stackTrace;
    private List<ValidationError> errors;

    @Getter
    @Setter
    @RequiredArgsConstructor
    private static class ValidationError {
        private final String field;
        private final String message;
    }

    public void addValidationError(String field, String message) {
        if (Objects.isNull(errors)) {
            this.errors = new ArrayList<>(); // se o objeto nao é nulo, esse metodo passa o "field" e a
        }                                    // "message" para o erro
        this.errors.add(new ValidationError(field, message));
    }

    public String toJson() { // a mensagem do erro 402 (user write wrong the email or password)
        return "{\"status\": " + getStatus() + ", " +
                "\"message\": \"" + getMessage() + "\"}";
    }
}
