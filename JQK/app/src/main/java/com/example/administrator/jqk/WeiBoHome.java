package com.example.administrator.jqk;

/**
 * Created by Administrator on 2016/11/18.
 */
public class WeiBoHome {
    int friendFocus;//好友最近关注
    String userName;//当前登录用户昵称
    int radar;//雷达
    int focusImg;//关注人头像
    String focusName;//关注人姓名
    int level;//关注人等级
    boolean watch;//是否关注该用户
    String time;//微博发布的时间
    String original;//来源

    public int getFriendFocus() {
        return friendFocus;
    }

    public void setFriendFocus(int friendFocus) {
        this.friendFocus = friendFocus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRadar() {
        return radar;
    }

    public void setRadar(int radar) {
        this.radar = radar;
    }

    public int getFocusImg() {
        return focusImg;
    }

    public void setFocusImg(int focusImg) {
        this.focusImg = focusImg;
    }

    public String getFocusName() {
        return focusName;
    }

    public void setFocusName(String focusName) {
        this.focusName = focusName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isWatch() {
        return watch;
    }

    public void setWatch(boolean watch) {
        this.watch = watch;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTransfer() {
        return transfer;
    }

    public void setTransfer(int transfer) {
        this.transfer = transfer;
    }

    public int getCommentNums() {
        return commentNums;
    }

    public void setCommentNums(int commentNums) {
        this.commentNums = commentNums;
    }

    public int getLikeNums() {
        return likeNums;
    }

    public void setLikeNums(int likeNums) {
        this.likeNums = likeNums;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    String comment;//评论
    String content;//内容
    int transfer;//转发数
    int commentNums;//评论数
    int likeNums;//点赞数
    String topic;//话题

}
