package cn.csuosa.chatroomcli.common.AsyncEventQueue;

import cn.csuosa.chatroomcli.common.AsyncEventQueue.SocketWaitingReplyEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

public class AsyncEventQueue
{
    private ConcurrentLinkedQueue<SocketWaitingReplyEvent> eventQueue;
    private long serialCount;

    public AsyncEventQueue()
    {
        eventQueue = new ConcurrentLinkedQueue<>();
        serialCount = 0;
    }

    public void newWait(String triggerWord, Object lockItem, boolean consumeEvent, AsyncEventHandler handler)
    {
        eventQueue.add(new SocketWaitingReplyEvent(serialCount, triggerWord, lockItem, consumeEvent, handler));
        serialCount++;
    }

    public void newEvent(Object item, String keyWord)
    {
        for (SocketWaitingReplyEvent it: eventQueue)
        {
            if (it.triggerCheck(keyWord))
            {
                boolean isConsumed = it.awake(item);
                eventQueue.remove(it);
                if (isConsumed) break;
            }
        }
    }

    public void clean()
    {
        eventQueue.clear();
    }
}
