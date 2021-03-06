package com.city.info.bot.tg_bot_api.handler;

import com.city.info.bot.cache.UserDataCache;
import com.city.info.bot.service.ChatReplyMessageService;
import com.city.info.bot.tg_bot_api.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class BotStartHandler implements InputMessageHandler {

    private final UserDataCache userDataCache;
    private final ChatReplyMessageService messagesService;

    public BotStartHandler(UserDataCache userDataCache,
                           ChatReplyMessageService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.BOT_START_REPLY;
    }

    private SendMessage processUsersInput(Message message) {

        int userId = message.getFrom().getId();
        long chatId = message.getChatId();

        SendMessage replyToUser =
                messagesService.getReplyMessage(chatId, "bot.start.reply");

        userDataCache.setCurrentUserBotState(userId, BotState.GET_INFO_BY_CITY_NAME);

        return replyToUser;
    }
}
