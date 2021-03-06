package com.city.info.bot.tg_bot_api.handler.crud;

import com.city.info.bot.cache.UserDataCache;
import com.city.info.bot.model.City;
import com.city.info.bot.service.ChatReplyMessageService;
import com.city.info.bot.service.CityService;
import com.city.info.bot.tg_bot_api.BotState;
import com.city.info.bot.tg_bot_api.handler.InputMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class GetInfoByCityNameHandler implements InputMessageHandler {

    private final UserDataCache userDataCache;
    private final ChatReplyMessageService messagesService;
    private final CityService cityService;

    public GetInfoByCityNameHandler(UserDataCache userDataCache,
                                    ChatReplyMessageService messagesService,
                                    CityService cityService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.cityService = cityService;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.GET_INFO_BY_CITY_NAME;
    }

    @Override
    public SendMessage handle(Message message) {

        int userId = message.getFrom().getId();
        long chatId = message.getChatId();
        String cityName = message.getText();

        SendMessage replyToUser;

        Optional<City> cityByName = cityService.getCityByName(cityName);

        if (cityService.getCityByName(cityName).isPresent()) {

            String cityInfo = cityByName.get().getInfo();
            replyToUser = new SendMessage(chatId, cityInfo);

            log.info("chatId {}, city name{}, city info{}", chatId, cityName, cityInfo);

            userDataCache.setCurrentUserBotState(userId, BotState.GET_INFO_BY_CITY_NAME);

        } else {
            replyToUser = messagesService.getReplyMessage(chatId, "bot.city.not.found");
            replyToUser.setReplyMarkup(getYesNoButtons());
        }

        return replyToUser;
    }

    private InlineKeyboardMarkup getYesNoButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton yesButton = new InlineKeyboardButton().setText("Да");
        InlineKeyboardButton noButton = new InlineKeyboardButton().setText("Нет");

        yesButton.setCallbackData("yesButton");
        noButton.setCallbackData("noButton");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(yesButton);
        keyboardButtonsRow1.add(noButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
