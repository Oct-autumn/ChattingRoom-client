package cn.csuosa.chatroomcli.common;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;

public class CheckboxCell
{
     CheckBox checkbox = new CheckBox();

    public ObservableValue<CheckBox> getCheckBox()
    {
        return new ObservableValue<>()
        {
            @Override
            public void addListener(ChangeListener<? super CheckBox> listener)
            {

            }

            @Override
            public void removeListener(ChangeListener<? super CheckBox> listener)
            {

            }

            @Override
            public CheckBox getValue()
            {
                return checkbox;
            }

            @Override
            public void addListener(InvalidationListener listener)
            {

            }

            @Override
            public void removeListener(InvalidationListener listener)
            {

            }
        };
    }

    public Boolean isSelected()
    {
        return checkbox.isSelected();
    }

    public void setSelected(boolean b)
    {
        this.checkbox.setSelected(b);
    }

    public CheckboxCell setDisable(boolean b)
    {
        this.checkbox.setDisable(b);
        return this;
    }
}
