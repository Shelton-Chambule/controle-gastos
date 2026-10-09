package com.chambule.controle_gastos.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
public class StandardError {

    private Instant timeStamp;
    private  Integer status;
    private String error;
    private String message;
    private String path;

}
