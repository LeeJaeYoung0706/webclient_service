package newws.webclient_service.lib;


import lombok.Getter;


@Getter
public class RestException extends RuntimeException{

    public RestException(String message ) {
        super(message);
    }
}
