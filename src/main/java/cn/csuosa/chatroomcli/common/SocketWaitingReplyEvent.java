package cn.csuosa.chatroomcli.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SocketWaitingReplyEvent
{
    public static long serialCount;

    public long serialNumber;
    private String waitingLabel;
    private Object lockItem;

    public SocketWaitingReplyEvent(String waitingLabel, Object lockItem)
    {
        this.serialNumber = serialCount;
        this.waitingLabel = waitingLabel;
        this.lockItem = lockItem;
    }
}
