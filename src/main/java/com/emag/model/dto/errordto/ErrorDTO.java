package com.emag.model.dto.errordto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorDTO {

    private String msg;
    private int status;
    private LocalDateTime time;
    private String exceptionType;
}
