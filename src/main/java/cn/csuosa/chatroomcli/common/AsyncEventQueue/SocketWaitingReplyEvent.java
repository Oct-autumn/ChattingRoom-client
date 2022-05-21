package cn.csuosa.chatroomcli.common.AsyncEventQueue;

import cn.csuosa.chatroomcli.common.AsyncEventQueue.AsyncEventHandler;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SocketWaitingReplyEvent
{
    public long serialNumber;
    private Object lockItem;
    private String triggerWord;
    private AsyncEventHandler handler;
    private boolean consumeEvent;

    public SocketWaitingReplyEvent(long serialNumber, String triggerWord, Object lockItem, boolean consumeEvent, AsyncEventHandler handler)
    {
        this.serialNumber = serialNumber;
        this.triggerWord = triggerWord;
        this.lockItem = lockItem;
        this.consumeEvent = consumeEvent;
        this.handler = handler;
    }

    /**
     * 检查事件触发词
     *
     * @param keyWord 事件触发词
     * @return 是否被触发
     */
    boolean triggerCheck(String keyWord)
    {
        return keyWord.equals(this.triggerWord);
    }

    public boolean awake(Object eventItem)
    {
        new Thread(()-> handler.handle(lockItem, eventItem)).start();
        return consumeEvent;
    }
}
