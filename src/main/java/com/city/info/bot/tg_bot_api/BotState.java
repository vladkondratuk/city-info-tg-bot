package com.city.info.bot.tg_bot_api;

public enum BotState {
    BOT_START_REPLY,
    GET_INFO_BY_CITY_NAME,
    CITY_NOT_FOUND,
    ADDING_NEW_CITY,
    ASK_CITY_NAME,
    ASK_CITY_INFO,
    CITY_DATA_ADDED,
    CITY_CRUD_MENU,
    SHOW_LIST_OF_CITIES,
    UPDATING_CITY,
    REMOVE_CITY,
    SHOW_MAIN_MENU,
}
