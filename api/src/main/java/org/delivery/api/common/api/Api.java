package org.delivery.api.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.delivery.api.common.error.ErrorCodeIfs;

import javax.validation.Valid;

@Data // Lombok을 사용하여 getter, setter, toString, equals, hashCode 메서드를 자동 생성합니다.
@NoArgsConstructor // Lombok을 사용하여 매개변수가 없는 기본 생성자를 자동 생성합니다.
@AllArgsConstructor // Lombok을 사용하여 모든 필드를 매개변수로 받는 생성자를 자동 생성합니다.
public class Api<T> {

    private Result result; // API 호출의 결과를 나타내는 필드입니다.

    @Valid // 이 필드는 유효성 검사를 받아야 함을 나타냅니다.
    private T body; // API의 응답 본문을 나타내는 제네릭 타입 필드입니다.

    // 성공적인 응답을 생성하는 정적 메서드입니다.
    public static <T> Api<T> OK(T data) {
        var api = new Api<T>(); // 새로운 Api 인스턴스를 생성합니다.
        api.result = Result.OK(); // 결과를 성공으로 설정합니다.
        api.body = data; // 응답 본문을 설정합니다.
        return api; // Api 인스턴스를 반환합니다.
    }

    // 에러 응답을 생성하는 정적 메서드입니다. (Result 객체를 직접 인자로 받습니다.)
    public static Api<Object> ERROR(Result result) {
        var api = new Api<Object>(); // 새로운 Api 인스턴스를 생성합니다.
        api.result = result; // 결과를 에러로 설정합니다.
        return api; // Api 인스턴스를 반환합니다.
    }

    // 에러 응답을 생성하는 정적 메서드입니다. (ErrorCodeIfs 인터페이스를 인자로 받습니다.)
    public static Api<Object> ERROR(ErrorCodeIfs errorCodeIfs) {
        var api = new Api<Object>(); // 새로운 Api 인스턴스를 생성합니다.
        api.result = Result.ERROR(errorCodeIfs); // 에러 코드로 결과를 설정합니다.
        return api; // Api 인스턴스를 반환합니다.
    }

    // 에러 응답을 생성하는 정적 메서드입니다. (ErrorCodeIfs와 Throwable 객체를 인자로 받습니다.)
    public static Api<Object> ERROR(ErrorCodeIfs errorCodeIfs, Throwable tx) {
        var api = new Api<Object>(); // 새로운 Api 인스턴스를 생성합니다.
        api.result = Result.ERROR(errorCodeIfs, tx); // 에러 코드와 예외로 결과를 설정합니다.
        return api; // Api 인스턴스를 반환합니다.
    }

    // 에러 응답을 생성하는 정적 메서드입니다. (ErrorCodeIfs와 설명 문자열을 인자로 받습니다.)
    public static Api<Object> ERROR(ErrorCodeIfs errorCodeIfs, String description) {
        var api = new Api<Object>(); // 새로운 Api 인스턴스를 생성합니다.
        api.result = Result.ERROR(errorCodeIfs, description); // 에러 코드와 설명으로 결과를 설정합니다.
        return api; // Api 인스턴스를 반환합니다.
    }
}
