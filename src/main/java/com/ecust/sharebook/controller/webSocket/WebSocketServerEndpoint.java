package com.ecust.sharebook.controller.webSocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecust.sharebook.controller.RabbitMQtest.RabbitProduct;
import com.ecust.sharebook.pojo.MessageInf;
import com.ecust.sharebook.service.TMessageService;
import com.ecust.sharebook.service.optService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;


/**
 *  WebSocket 服务配置类
 *  定义 userId 为当前连接(在线) WebSocket 的用户
 */
@Slf4j
@Component
@ServerEndpoint(value = "/small/websocket/{userId}")
public class WebSocketServerEndpoint {

    private Session session; //建立连接的会话
    private String userId; //当前连接用户id   路径参数


    static    RabbitProduct rabbitProduct ;

    static TMessageService tMessageService;

    static optService toptService;




    @Autowired
    public void setMessageService(TMessageService tMessageService){
        WebSocketServerEndpoint.tMessageService = tMessageService;
    }


    @Autowired
    public void setToptService(optService toptService){
        WebSocketServerEndpoint.toptService = toptService;
    }



    /**
     * 构造方法注入rabbitTemplate
     */
    @Autowired
    public void setProduct(RabbitProduct rabbitProduct){
        WebSocketServerEndpoint.rabbitProduct = rabbitProduct;
    }

    /**
     * 存放存活的Session集合(map保存)
     */
    private static ConcurrentHashMap<String , WebSocketServerEndpoint> livingSession = new ConcurrentHashMap<>();

    /**
     *  建立连接的回调
     *  session 建立连接的会话
     *  userId 当前连接用户id   路径参数
     */
    @OnOpen
    public void onOpen(Session session,  @PathParam("userId") String userId){
        this.session = session;
        this.userId = userId;
        livingSession.put(userId, this);

       System.out.println("----[ WebSocket ]---- 用户id为 : {} 的用户进入WebSocket连接 !  --------"+userId+livingSession.size());


    }

    /**
     *  关闭连接的回调
     *  移除用户在线状态
     */
    @OnClose
    public void onClose(){
        livingSession.remove(userId);
       System.out.println("----[ WebSocket ]---- 用户id为 : {} 的用户退出WebSocket连接 ! 当前在线人数为 : {} 人 !--------"+userId+livingSession.size()+'\n');
    }

    /**
     * flag opt
     * senderId
     * recevierId
     * dateTime
     *
     *
     * @param message
     * @param session
     * @param userId
     * @throws ParseException
     */
    @OnMessage
    public void onMessage(String message, Session session,  @PathParam("userId") String userId) throws ParseException {
       System.out.println("-----[ WebSocket ]----收到用户id为 : {} 的用户发送的消息 ! 消息内容为 : ------------------"+userId+message);
        //sendMessageToAll(userId + " : " + message);

        JSONObject jsonObject = JSON.parseObject(message);
        System.out.println(jsonObject);
        MessageInf messageInf = new MessageInf();
        String date = jsonObject.getString("dateTime");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = format.parse(date);

        String senderId = jsonObject.getString("senderId");
        String receiverId = jsonObject.getString("receiverId");
        Integer mType = Integer.valueOf(jsonObject.get("mType").toString());
        messageInf.setmType(mType);
        messageInf.setSenderId(Integer.valueOf(senderId));
        messageInf.setReceiverId(Integer.valueOf(receiverId));

        if(mType!=2){

            Integer borrowRes =Integer.valueOf( jsonObject.get("borrowRes").toString());
            Integer messageId = Integer.valueOf(jsonObject.get("messageId").toString());

            messageInf.setBorrowRes(borrowRes);
            messageInf.setMessageId(messageId);
        }else{
            String content = jsonObject.getString("content");
            messageInf.setContent(content);

        }
        messageInf.setDateTime(dateTime);

        //保存到数据库
        try{
            if(mType!=2){
               Boolean   result = toptService.opt(messageInf);
               if(result){
                   messageInf.setFlag(1);
               }else{
                   messageInf.setFlag(0);
               }
            }
            int i= tMessageService.insertSelective(messageInf);
            if(i==1){
               messageInf.setFlag(1);
            }else{
                messageInf.setFlag(0);
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("insertMesage-failed");
            messageInf.setFlag(0);
        }

        rabbitProduct.sendMSG(messageInf);


    }

    @OnError
    public void onError(Session session, Throwable error) {
       System.out.println("----------------WebSocket发生错误----------------"+'\n');
       System.out.println(error.getStackTrace() + "");
    }

    /**
     *  根据userId发送给用户
     * @param userId
     * @param message
     */
    public void sendMessageById(String userId, String message) {
        livingSession.forEach((sessionId, session) -> {
            //发给指定的接收用户
            if (userId.equals(session.userId)) {
                sendMessageBySession(session.session, message);
            }
        });
    }

    /**
     *  根据Session发送消息给用户
     * @param session
     * @param message
     */
    public void sendMessageBySession(Session session, String message) {
        try {

            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
           System.out.println("----[ WebSocket ]------给用户发送消息失败---------\n");
            e.printStackTrace();
        }
    }



    /**
     *  给在线用户发送消息
     * @param message
     */
    public void sendMessageOnline(String message) throws IOException {
       // livingSession.forEach((sessionId, session) -> {
     //       if(session.session.isOpen()){

    //           sendMessageBySession(session.session, message);
               // sendMessageById(toUserId,message);
        //    }
    //    });
      //  sendMessageById(toUserId,message);
        JSONObject jsonObject = JSON.parseObject(message);
        String receiverId = jsonObject.getString("receiverId");
        String senderId = jsonObject.getString("senderId");
        sendMessageTo(message,receiverId,senderId);


    }


    public void sendMessageTo(String message, String TouserId, String userId) throws IOException {
System.out.println(TouserId);
        for (WebSocketServerEndpoint item : livingSession.values()) {
            if (item.userId.equals(TouserId) || item.userId.equals(userId) ) {

                item.session.getAsyncRemote().sendText(message);
                /**
                 *  getBasicRemote()  阻塞式 同步
                 *  getAsyncRemote() 非阻塞式 异步
                 */

                break;

            }
        }

    }
}

