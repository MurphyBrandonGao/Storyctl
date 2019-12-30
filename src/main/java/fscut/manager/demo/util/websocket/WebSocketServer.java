package fscut.manager.demo.util.websocket;

import fscut.manager.demo.service.MessageService;
import fscut.manager.demo.util.token.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket/{token}", encoders = {SocketEncoder.class})
@Component
@Slf4j
public class WebSocketServer {

    public static MessageService messageService;

    public static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    private Session session;

    private String username;

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        this.session = session;
        this.username = JwtUtils.getUsername(token);
        webSocketMap.put(username, this);
        log.info(username + " has login,有新的连接，总数：{}", webSocketMap.size());
    }

    @OnClose
    public void onClose() {
        webSocketMap.remove(username);
        log.info("连接断开，总数：{}", webSocketMap.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("收到客户端发来的消息：{}", message);
    }

    @OnError
    public void onError(Throwable error) {
        log.info("错误信息为：{}", error.getMessage());
    }

    private void sendMessage(Object message) {
        try {
            this.session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            log.info("错误信息为：{}", e.getMessage());
        }
    }

    public static void sendInfo(Object message, String username) {
        System.out.println(username);
        webSocketMap.get(username).sendMessage(message);
        log.info("向{}发送了消息：{}", username, message);
    }

}
