package cn.csuosa.chatroomcli.model;

import com.google.protobuf.ByteString;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message
{
    private long timestamp = 0;
    private String channel = "";
    private String fromNick = "";
    private MessageType type = MessageType.PLAIN_TEXT;
    private ByteString content = ByteString.EMPTY;

    public enum MessageType
    {
        PLAIN_TEXT,
        PICTURE,
        RICH_TEXT,
        HTML,
        FILE
    }
}
