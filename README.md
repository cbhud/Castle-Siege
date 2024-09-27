
# Castle Siege Plugin Documentation

## Introduction

**Castle Siege** is a thrilling Minecraft minigame plugin inspired by Mineplex. Engage in intense battles as Attackers or Defenders, where your objective is either to protect the King or assassinate him. With multiple kits, abilities, and strategic elements, this game is packed with fun for all players.

----------

## Features Overview

-   **Teams**: Two sides – Defenders and Attackers.
-   **8 Kits**: Four unique kits per team, each with custom abilities.
-   **Modes**: Normal (respawn allowed) or Hardcore (no respawns).
-   **Abilities**: Special items for kits with various effects.
-   **Coins**: Earned through gameplay to unlock new kits.
-   **Kill Rewards**: Get boosts like Speed or Regeneration on kills.
-   **Throwable Weapons**: Berserker’s throwable axes and Bombardier’s TNT to damage players or destroy obstacles.
-   **Map Regeneration**: Automatically rebuilds destroyed fences after each game.
-   **Statistics Tracking**: Tracks kills, deaths, and wins for each player.
-   **Fully Configurable**: Modify teams, kits, coins, messages, and more.
-   **Autostart**: Game can automatically begin when enough players join.
-   **Free Map**: Pre-made map available for download and use.

----------

## Installation Guide

### Requirements:

-   Minecraft server running **Spigot** or **Paper** (version 1.17+).
-   Java **17** or later.
-   **Maven** for compiling the plugin (optional for development).

### Steps:

1.  Download the latest Castle Siege JAR from the [releases](https://www.spigotmc.org/resources/castle-siege.115123/) page.
2.  Place the JAR file into your server’s `plugins/` folder.
3.  Restart the server to enable the plugin.
4.  Configure the plugin using the provided configuration files.

----------

## Configuration

### Main Configuration File (`config.yml`)

Adjust key settings such as team names, game duration, kit prices, king health, and coin rewards.

-   **maxPlayersPerTeam**: Maximum players per team (default: 16).
-   **auto-start-players**: Minimum players required to start the game automatically.
-   **king-health**: Health points for the King (default: 80.0).
-   **coins-on-kill / coins-on-win**: Set coin rewards for kills and victories.

### Kit Configuration (`kits.yml`)

The **kits.yml** file allows you to customize kits, item prices, and abilities. For now, it's recommended to only adjust kit items and prices without altering kit names or teams.

### Map Regeneration

Attackers can destroy **Oak Fences**, and Defenders can rebuild them. Fences automatically regenerate after the game.

----------

## Commands

### Permissions:
-   `cs.admin`: Full access to admin commands.
### Admin Commands:

-   `/cs setlobby`: Set the lobby location.
-   `/cs setspawn <team>`: Set spawn locations for Attackers or Defenders.
-   `/cs setmobspawn`: Set the King's spawn point.
-   `/cs start`: Start the game manually.
-   `/cs endgame`: End the game without a winner.
-   `/cs type`: Toggle between Normal and Hardcore modes.

-   `/coins <set | add | remove> <player> <amount>`: Manage player coins.

### Player Commands:

-   `USE SELECTOR`:  Join a specific team or select a kit.
-   `/stats [player]`: View player statistics.

----------

## Game Modes

### Normal Mode:

Players respawn 5 seconds after death. It’s suitable for more casual gameplay where players can keep battling after death.

### Hardcore Mode:

Players do not respawn after death, raising the stakes for each move.

Use `/cs type` to switch between **Normal** and **Hardcore**.

----------

## Kits and Abilities

### Attackers:

1.  **Skald**:
    
    -   Weapons: Stone Sword, Bow (32 arrows), Healing Stew (2x)
    -   Ability: **Skald Sight** (Jump Boost, Speed, Night Vision)
    -   Kill Effects: Harm Arrow, Regeneration, Absorption
2.  **Bombardier**:
    
    -   Weapons: Stone Sword, Throwable TNT, Chain Armor, Healing Stew
    -   Ability: **Throwable TNT** (destroys fences)
    -   Kill Effects: Speed, Jump Boost
3.  **Berserker**:
    
    -   Weapons: Iron Axe, Throwable Axe, Chain Armor, Healing Stew
    -   Ability: **Rage** (Speed, Resistance, Strength)
    -   Kill Effects: Absorption, Regeneration
4.  **Warrior**:
    
    -   Weapons: Iron Sword, Iron Armor, Healing Stew
    -   Ability: **Ragnarok** (Strength 1, Speed 1)
    -   Kill Effects: Resistance

### Defenders:

1.  **Marksman**:
    
    -   Weapons: Stone Sword, Crossbow (32 arrows), Chain Armor, Healing Stew
    -   Kill Effects: Spectral Arrow, Speed
2.  **Spearman**:
    
    -   Weapons: Iron Sword, Loyalty Spear, Chain Armor, Healing Stew
    -   Kill Effects: Speed
3.  **Wizard**:
    
    -   Weapons: Mystic Sword (chance to cast poison), Attack Wand (cast poison, slowness, or blindness), Support Wand (boost teammates)
    -   Kill Effects: Blast enemies within 10 blocks for 3 hearts of damage
4.  **Knight**:
    
    -   Weapons: Iron Sword, Iron Armor, Healing Stew
    -   Kill Effects: Speed, Resistance

----------

## Coin System

Players earn coins for kills and wins, which can be used to purchase and unlock kits. The number of coins rewarded can be configured in `config.yml`:

-   **coins-on-kill**: Coins earned per kill.
-   **coins-on-win**: Coins earned for winning the game.

----------

## Map Regeneration System

Attackers can destroy **Oak Fences**, while Defenders can rebuild them during the game. After the game ends, all destroyed or placed fences are automatically regenerated.

----------

## Free Map

A pre-built map is available for download:

-   [Map Download Link](https://www.mediafire.com/file/7b348d4d9f2ugxo/world.rar/file)
-   Coordinates: `-751, 116, 607`

----------

## Future Updates

The plugin will receive monthly updates with bug fixes, new features, and balance improvements. For feedback or bug reports, contact me via Discord: cbhud.
