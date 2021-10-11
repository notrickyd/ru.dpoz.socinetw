package ru.dpoz.socinetw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

public interface NewsFeedFriends
{
    String getMessage();
    Timestamp getTimestampx();
    String getName();
}
