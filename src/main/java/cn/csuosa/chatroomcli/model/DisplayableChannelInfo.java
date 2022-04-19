package cn.csuosa.chatroomcli.model;

import cn.csuosa.chatroomcli.common.CheckboxCell;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class DisplayableChannelInfo
{
    private String name = "";
    private String onlineMember = "0";
    private CheckboxCell requireVerify = new CheckboxCell();
    private CheckboxCell joined = new CheckboxCell();

    public DisplayableChannelInfo(String name, int onlineMember, boolean requireVerify, boolean joined)
    {
        this.setName(name);
        this.setOnlineMember(onlineMember);
        this.setRequireVerify(requireVerify);
        this.setJoined(joined);

        this.requireVerify.setDisable(true);
        this.joined.setDisable(true);
    }

    public int getOnlineMemberByInt()
    {
        return Integer.parseInt(onlineMember);
    }

    public String getOnlineMember()
    {
        return onlineMember;
    }

    public void setOnlineMember(int onlineMember)
    {
        this.onlineMember = String.format("%d", onlineMember);
    }

    public Boolean isRequireVerify()
    {
        return requireVerify.isSelected();
    }

    public void setRequireVerify(boolean requireVerify)
    {
        this.requireVerify.setSelected(requireVerify);
    }

    public Boolean isJoined()
    {
        return joined.isSelected();
    }

    public void setJoined(boolean joined)
    {
        this.joined.setSelected(joined);
    }
}
