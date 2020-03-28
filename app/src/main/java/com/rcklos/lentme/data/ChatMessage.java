package com.rcklos.lentme.data;

public class ChatMessage {
    private String token = "";//如果能永久聊的话，可以用这个token一直聊下去
    private String userName = "";//用户名
    private String content = "";//消息内容
    private Boolean isFromToken = false;//是否token发过来的信息
//    private String sendTime;
//    private String recvTime;

    public ChatMessage(String token, String userName, String content,  Boolean isFromToken ) {
        this.token = token;
        this.userName = userName;
        this.content = content;
        this.isFromToken = isFromToken;
    }

    public ChatMessage(){}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getFromToken() {
        return isFromToken;
    }

    public void setFromToken(Boolean fromToken) {
        isFromToken = fromToken;
    }

}
