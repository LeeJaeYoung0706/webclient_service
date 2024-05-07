//package newws.webclient_service.router;
//
//import newws.webclient_service.controller.MemberController;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.server.HandlerFunction;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.RouterFunctions;
//import org.springframework.web.reactive.function.server.ServerResponse;
//
//@Configuration
//public class ServiceRouter {
//    @Bean
//    RouterFunction<ServerResponse> route(MemberController memberController) {
//        return RouterFunctions.route()
//                .GET("/example" , (HandlerFunction<ServerResponse>) memberController.getExample())
//                .build();
//    }
//}
