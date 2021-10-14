package ru.dpoz.socinetw.rmq.processors;

import org.springframework.stereotype.Service;
import ru.dpoz.socinetw.rmq.RmqEventMessage;

@Service
public abstract class RmqProcessor
{
    public abstract void processMessage(RmqEventMessage message);
}
