package newws.webclient_service.lib;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNKNOWN(500)                // 알 수 없는 오류
    , REQUIRED_PARAM(501)       // 필수 파라메터 필요
    , WRONG_REQUEST(502)        // 잘못된 요청
    , MAXIMUM_FILE_SIZE(503)    // 파일 크기 오류
    , CANNOT_DELETE(504)        // 삭제 불가(외래키)
    , PRECONDITION_FAILED(412)  // 전제조건 실패


    // 토큰 관련 오류
    , INVALID_TOKEN(300)        // 유효하지 않는 토큰
    , EXPIRED_TOKEN(301)        // 만료된 토큰

    // 인증 관련 오류
    , NOT_FOUND_USER(400)       // 사용자를 찾을 수 없음
    , UNAUTHORIZED(401)         // 미인증
    , FORBIDDEN(403)            // 권한 없음
    , BINDING(405)     // 잘못된 형식의 파라미터
    , ARGUMENT(406) // argument 문제
    , STATE(505); // state 문제

    private final int value;

    ErrorCode(int value) {
        this.value = value;
    }

    public static ErrorCode from(int code) {
        for(ErrorCode err : values()) {
            if(err.getValue() == code) return err;
        }

        return null;
    }

}
