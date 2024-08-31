package org.delivery.api.common.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.error.ErrorCodeIfs;

// Lombok을 사용하여 기본 생성자, 모든 필드를 인자로 받는 생성자, getter/setter, 빌더 패턴 메서드를 자동 생성합니다.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {

    // 결과 코드 (애플리케이션 에러 코드)
    private Integer resultCode;

    // 결과 메시지 (에러 설명)
    private String resultMessage;

    // 결과 설명 (상세 설명)
    private String resultDescription;

    // 성공적인 결과를 생성하는 정적 메서드
    public static Result OK() {
        return Result.builder()
                .resultCode(ErrorCode.OK.getErrorCode()) // 성공 코드 설정
                .resultMessage(ErrorCode.OK.getDescription()) // 성공 메시지 설정
                .resultDescription("성공") // 상세 설명 설정
                .build();
    }

    // 에러 코드를 기반으로 에러 결과를 생성하는 정적 메서드
    public static Result ERROR(ErrorCodeIfs errorCodeIfs) {
        return Result.builder()
                .resultCode(errorCodeIfs.getErrorCode()) // 에러 코드 설정
                .resultMessage(errorCodeIfs.getDescription()) // 에러 메시지 설정
                .resultDescription("에러발생") // 상세 설명 설정
                .build();
    }

    // 에러 코드와 예외를 기반으로 에러 결과를 생성하는 정적 메서드
    public static Result ERROR(ErrorCodeIfs errorCodeIfs, Throwable tx) {
        return Result.builder()
                .resultCode(errorCodeIfs.getErrorCode()) // 에러 코드 설정
                .resultMessage(errorCodeIfs.getDescription()) // 에러 메시지 설정
                .resultDescription(tx.getLocalizedMessage()) // 예외의 상세 설명 설정
                .build();
    }

    // 에러 코드와 추가 설명을 기반으로 에러 결과를 생성하는 정적 메서드
    public static Result ERROR(ErrorCodeIfs errorCodeIfs, String description) {
        return Result.builder()
                .resultCode(errorCodeIfs.getErrorCode()) // 에러 코드 설정
                .resultMessage(errorCodeIfs.getDescription()) // 에러 메시지 설정
                .resultDescription(description) // 추가 설명 설정
                .build();
    }
}
