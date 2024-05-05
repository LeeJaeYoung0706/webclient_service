package newws.webclient_service.lib;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;


@Getter
public class Response<T> extends ResponseEntity implements Serializable {

    private static final long serialVersionUID = 362498820763181265L;
    private String message;  // 결과 메시지
    private final int code;       // 결과 코드
    private int total;          // 총 응답 데이터 수
    private final T data; // 응답 데이터
    private final HttpStatus status;

    /**
     * 생성자에서 필수적으로 넣어야하는 처리를 해야해서 커스텀
     * @param <T>
     */
    public static class ResponseBuilder<T> {

        private String message;  // 결과 메시지
        private final int code;  // int 형 코드
        private int total;  // 총 응답 데이터 수
        private T data; // 응답 데이터
        private HttpStatus status;

        private void setStatus(int code){
            try {
                status = HttpStatus.valueOf(code);
            } catch (IllegalArgumentException ignored) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            };
        }

        private ResponseBuilder( int code , T data) {
            this.code = code;
            this.data = data;
            setStatus(code);
        }

        public ResponseBuilder<T> message(String value) {
            message = value;
            return this;
        }

        public ResponseBuilder<T> total(int value) {
            total = value;
            return this;
        }

        public Response<T> build() {
            return new Response<>(this);
        }
    }

    public static <T> ResponseBuilder<T> builder(int code, T data ) {
        return new ResponseBuilder<>(code , data);
    }

    private Response(ResponseBuilder<T> responseBuilder){
        super(responseBuilder.data ,responseBuilder.status);
        message = responseBuilder.message;
        status = responseBuilder.status;
        total = responseBuilder.total;
        data = responseBuilder.data;
        code = responseBuilder.code;
    }

}