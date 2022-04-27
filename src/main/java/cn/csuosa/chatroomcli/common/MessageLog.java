package cn.csuosa.chatroomcli.common;

import cn.csuosa.chatroomcli.model.Message;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.*;

public class MessageLog implements ObservableValue<Vector<Message>>
{
    private final Vector<InvalidationListener> invalidationListeners = new Vector<>();
    private final Vector<ChangeListener<? super Vector<Message>>> changeListeners = new Vector<>();
    private final Vector<Message> messageList = new Vector<>();

    public MessageLog()
    {
        super();
    }

    @Override
    public void addListener(InvalidationListener invalidationListener)
    {
        invalidationListeners.add(invalidationListener);
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener)
    {
        invalidationListeners.remove(invalidationListener);
    }

    @Override
    public void addListener(ChangeListener<? super Vector<Message>> changeListener)
    {
        changeListeners.add(changeListener);
    }

    @Override
    public void removeListener(ChangeListener<? super Vector<Message>> changeListener)
    {
        changeListeners.remove(changeListener);
    }

    @Override
    public Vector<Message> getValue()
    {
        return messageList;
    }

    public void addMessage(Message message)
    {
        Vector<Message> messages_old = new Vector<>(messageList);
        messageList.add(message);
        changeListeners.forEach(changeListener -> changeListener.changed(this, messages_old, messageList));
        invalidationListeners.forEach(invalidationListener -> invalidationListener.invalidated(this));
    }

    public void clear()
    {
        Vector<Message> messages_old = new Vector<>(messageList);
        messageList.clear();
        changeListeners.forEach(changeListener -> changeListener.changed(this, messages_old, messageList));
        invalidationListeners.forEach(invalidationListener -> invalidationListener.invalidated(this));
    }
}

