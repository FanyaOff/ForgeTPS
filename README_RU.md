[RU](https://github.com/FanyaOff/TabTPS/blob/main/README_RU.md) | [EN](https://github.com/FanyaOff/TabTPS/blob/main/README.md)

# ForgeTPS

ForgeTPS - мод для Minecraft, который добавляет информацию о производительности сервера в список игроков

## Плейсхолдеры

Доступные плейсхолдеры для использования:
- **`tabHeader`** — поддерживает только `%playername%`, заменяющийся на имя игрока.
- **`tabFooter`** — поддерживает `%tps%`, `%mspt%`, `%ping%` для отображения показателей производительности сервера и задержки.

## Конфигурация

Конфигурация мода находится в файле `forgetps.json` в папке `config`.

Пример конфигурации по умолчанию:
```json
{
  "tabHeader": "Добро пожаловать, %playername%!",
  "tabFooter": "TPS: %tps% | Ping: %ping% | MSPT: %mspt%",
  "enableNicknameColorChange": true,
  "customDimensions": {
    "minecraft:overworld": "GREEN",
    "minecraft:the_nether": "RED",
    "minecraft:the_end": "LIGHT_PURPLE",
    "undergarden:undergarden": "DARK_GREEN",
    "ae2:spatial_storage": "WHITE"
  }
}
```

### Описание параметров

- **`tabHeader`** — текст заголовка в таб-меню, где можно использовать только `%playername%`.
- **`tabFooter`** — текст подвала таб-меню, поддерживает `%tps%`, `%mspt%`, `%ping%` для отображения TPS, MSPT и задержки.
- **`enableNicknameColorChange`** — если `true`, цвет ника игрока будет изменяться в зависимости от измерения.
- **`customDimensions`** — словарь для настройки цветов ника по измерениям. Ключ — ID измерения, значение — цвет.

### Доступные цвета

Для `customDimensions` доступны следующие цвета:

- `BLACK`, `DARK_BLUE`, `DARK_GREEN`, `DARK_AQUA`, `DARK_RED`, `DARK_PURPLE`, `GOLD`, `GRAY`, `DARK_GRAY`, `BLUE`, `GREEN`, `AQUA`, `RED`, `LIGHT_PURPLE`, `YELLOW`, `WHITE`.

## Установка

1. Установите Forge.
2. Скопируйте мод `TabTPS` в папку `mods`.
3. Настройте конфигурацию в `tabtps.json` (создается автоматически при первом запуске).
4. Перезапустите сервер для применения настроек.
