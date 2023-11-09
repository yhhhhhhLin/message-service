package xyz.linyh.messageservice.config;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * 获取建立连接后保存httpsession
 */
public class GetHttpSessionConfig extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        String query = request.getRequestURI().getQuery();
        String[] split = query.split("=");
        if(split.length==2 && "token".equals(split[0]) && "12345".equals(split[1])) {
            sec.getUserProperties().put("token", split[1]);
        }

//        获取httpsession请求 前后端项目没有token这个说法，所以无法获取对应token，ws不会携带请求头，所以也无法获取到请求头信息，需要直接uri传递对应token进行认证
//        Object httpSession = request.getHttpSession();
////        判断是否有session
//        if(httpSession!=null) {
//            sec.getUserProperties().put("httpSession", httpSession);
//        }else {
//            super.modifyHandshake(sec, request, response);
//        }
    }
}
