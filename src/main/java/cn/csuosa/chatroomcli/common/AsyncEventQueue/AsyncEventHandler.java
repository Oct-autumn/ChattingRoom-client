package cn.csuosa.chatroomcli.common.AsyncEventQueue;

public interface AsyncEventHandler
{
    /**
     * 处理器
     * @param lockItem
     */
    void handle(Object lockItem, Object eventItem);
}
