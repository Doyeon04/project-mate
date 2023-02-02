//package com.toy.projectmate.exception;
//
//import com.toy.projectmate.web.dto.ErrorResponseDto;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice // 예외 발생시 json형태로 반환할 때 사용
//public class BadResponseHandler {
//
//    @ExceptionHandler(JsonException.class) // JsonException 예외 발생시 json으로 에러 메세지 전달
//    public ErrorResponseDto jsonException(JsonException ex){
//        return ErrorResponseDto.of(ex.getMessage());/**/
//    }
//}
