package org.delivery.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode implements ErrorCodeIfs {

    // 성공
    OK(200, 200, "성공"),

    // 클라이언트 오류 (4xx)
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), 400, "잘못된 요청"),
    INVALID_INPUT(HttpStatus.BAD_REQUEST.value(), 401, "유효하지 않은 입력"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), 401, "인증 실패"),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), 403, "접근 권한 없음"),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), 404, "리소스를 찾을 수 없음"),

    // 서버 오류 (5xx)
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "내부 서버 오류"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE.value(), 503, "서비스 사용 불가"),

    // 커스텀 오류
    NULL_POINT_ERROR(520, 520, "Null Pointer 오류"),
    TOKEN_EXPIRE(521, 521, "토큰 만료"),
    INVALID_TOKEN(522, 522, "유효하지 않은 토큰"),

    // 기타 오류
    UNKNOWN_ERROR(599, 599, "알 수 없는 오류");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}