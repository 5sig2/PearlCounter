# PearlCounter

A mod that just tracks how many ender pearls each player has thrown.

Derivative Work of [TotemCounter](https://github.com/uku3lig/totemcounter) and has a integration to allow easy use of both of them.

## Features

- Tracks pearl throws from nearby players
- Adds pearl counts and an icon after player nametags
- Show on TAB
- Configurable Position
- Display your remaining pearls or your own throws
- Default or Resource Pack pearl icon
- Reset when player dies configuration option
- TotemCounter integration

## Counter Colors

| Pearls thrown | Color |
|---:|---|
| 1–7 | Light green |
| 8–15 | Dark green |
| 16–23 | Yellow |
| 24–31 | Gold |
| 32+ | Red |

## TotemCounter Integration

When a compatible version of TotemCounter is installed, PearlCounter adds a dedicated integration tab.

The shared HUD display can:

- Swap Pearl/Totem Display
- Use a configurable 1–30 second swap time
- Skip counters that are currently zero

PearlCounter's standard features work without TotemCounter, but still require ukulib

## Configuration

Press **F9** in game to open the configuration screen.

Use `/resetpearls` to clear all pearl counts for the current session.

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/) for Minecraft 1.21.11.
2. Install [Fabric API](https://modrinth.com/mod/fabric-api).
3. Install [Ukulib](https://modrinth.com/mod/ukulib).
4. Place the PearlCounter JAR in your `mods` folder.
5. Optionally install [TotemCounter](https://modrinth.com/mod/totemcounter) for integration features.

## Requirements

| Dependency | Version |
|---|---|
| Minecraft | 1.21.11 |
| Java | 21 or newer |
| Fabric Loader | 0.17 or newer |
| Fabric API | Compatible 1.21.11 version |
| Ukulib | 1.10.0 or newer |
| TotemCounter | 1.11.2 or newer — optional |


## Source and License

PearlCounter is open source under the [MIT License](https://github.com/5sig2/PearlCounter/blob/main/LICENSE).

This project is a fork and adaptation of [TotemCounter](https://github.com/uku3lig/totemcounter). The original copyright and license notice are retained.

- [Source code](https://github.com/5sig2/PearlCounter)
- [Releases](https://github.com/5sig2/PearlCounter/releases)
