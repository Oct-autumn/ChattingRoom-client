package cn.csuosa.chatroomcli.model;

import com.google.protobuf.ByteString;
import lombok.*;

import java.util.Vector;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Channel
{
    private DisplayableChannelInfo info;

    //以下为加入频道后才会有对应实例的量
    private Vector<ByteString> msgRecord = null;
    private Vector<String> memberList = null;
}
