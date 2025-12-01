package gdg.hongik.mission.common;


import gdg.hongik.mission.common.dto.ErrorResponse;
import gdg.hongik.mission.common.exception.BadRequestException;
import gdg.hongik.mission.common.exception.NotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);


    }



    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

    }

    /*
    유효성 검사 : 아직 추가하지 않은부분
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknownException(Exception e){
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);

    }
    */

}


