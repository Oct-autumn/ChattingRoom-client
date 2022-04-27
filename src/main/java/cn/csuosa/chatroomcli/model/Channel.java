package cn.csuosa.chatroomcli.model;

import cn.csuosa.chatroomcli.common.MessageLog;
import lombok.*;

import java.util.Vector;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Channel
{
    @Setter
    private String name = "";
    @Setter
    private int onlineMember = 0;
    @Setter
    private boolean publicChannel = false;

    private boolean joined = false;

    //以下为加入频道后才会有对应实例的量
    private MessageLog messageLog = null;    //消息记录
    private Vector<String> memberList = null;    //成员列表

    public void setJoined(boolean b)
    {
        joined = b;

        if (b)
        {
            messageLog = new MessageLog();
            memberList = new Vector<>();
        }
        else
        {
            messageLog = null;
            memberList = null;
        }
    }

}
