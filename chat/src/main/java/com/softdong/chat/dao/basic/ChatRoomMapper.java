package com.softdong.chat.dao.basic;

import java.util.List;

import com.softdong.chat.entity.basic.ChatRoom;

public interface ChatRoomMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ChatRoom record);

    int insertSelective(ChatRoom record);

    ChatRoom selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChatRoom record);

    int updateByPrimaryKey(ChatRoom record);
    
    List<ChatRoom> selectAll();
}