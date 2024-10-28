[RU](https://github.com/FanyaOff/TabTPS/blob/main/README_RU.md) | [EN](https://github.com/FanyaOff/TabTPS/blob/main/README.md)

# ForgeTPS

ForgeTPS is a Minecraft mod that adds server performance information to the player list (Tab menu) header and footer.

## Placeholders

Available placeholders for customization:
- **`tabHeader`** — supports only `%playername%` to display the player's name.
- **`tabFooter`** — supports `%tps%`, `%mspt%`, `%ping%` to show server performance stats.

## Configuration

The mod configuration is located in the `forgetps.json` file in the `config` folder.

Default configuration example:
```json
{
  "tabHeader": "Welcome, %playername%!",
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

### Configuration Parameters

- **`tabHeader`** — header text in the tab menu; only `%playername%` is allowed here.
- **`tabFooter`** — footer text in the tab menu; supports `%tps%`, `%mspt%`, `%ping%` for performance stats and ping.
- **`enableNicknameColorChange`** — if `true`, changes player nickname color based on dimension.
- **`customDimensions`** — dictionary for setting nickname colors per dimension. Key — dimension ID, value — color.

### Available Colors

For `customDimensions`, the following colors are available:

- `BLACK`, `DARK_BLUE`, `DARK_GREEN`, `DARK_AQUA`, `DARK_RED`, `DARK_PURPLE`, `GOLD`, `GRAY`, `DARK_GRAY`, `BLUE`, `GREEN`, `AQUA`, `RED`, `LIGHT_PURPLE`, `YELLOW`, `WHITE`.

## Installation

1. Install Forge.
2. Copy the `TabTPS` mod into the `mods` folder.
3. Configure settings in `tabtps.json` (auto-generated on first launch).
4. Restart the server to apply the settings.
