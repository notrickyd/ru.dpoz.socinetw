package ru.dpoz.socinetw.rmq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * Класс описывает сообщения отправляемые и поступающие в/из очереди
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RmqEventMessage implements Serializable
{
    EventMessageType type;
    UUID userId;
    Object object;

    @Override
    public String toString()
    {
        return this.type.toString() + ", " + this.userId.toString() + ",  " + object;
    }
}
