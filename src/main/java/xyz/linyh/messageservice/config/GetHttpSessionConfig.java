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
//        获取httpsession请求
        Object httpSession = request.getHttpSession();
//        判断是否有session
        if(httpSession!=null) {
            sec.getUserProperties().put("httpSession", httpSession);
        }else {
            super.modifyHandshake(sec, request, response);
        }
    }
}
