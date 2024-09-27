Castle Siege Plugin Documentation
Overview

The Castle Siege plugin is a multiplayer PvP game where players form teams to defend or attack a castle. Each team has unique roles, abilities, and strategies. The plugin includes features such as customizable kits, map regeneration, and a reward system based on coins and stats.
Features

    King Role: One player is designated as the King, and the team's objective is to protect or eliminate them.
    Teams: Multiple teams with customizable sizes.
    Kits & Abilities: Players can choose from various kits, each with its own abilities.
    Coins: Earned during gameplay and used to unlock kits, abilities, and cosmetics.
    Stats: Track player performance with built-in stats and leaderboards.
    Map Regeneration: Automatically regenerates the battlefield after each round.

Installation

    Download the Castle Siege plugin from SpigotMC.
    Place the .jar file into your server's plugins folder.
    Restart the server to generate the configuration files.

Configuration

    Open the config.yml located in the plugins/CastleSiege/ folder.
    Modify the settings:
        Teams: Define team names, colors, and sizes.
        Kits: Configure available kits and abilities.
        Map Settings: Set map regeneration rules and timers.

Commands

    /castlesiege start – Starts a new game.
    /castlesiege stop – Stops the current game.
    /castlesiege reload – Reloads the plugin configuration.
    /castlesiege team [name] – Assign players to teams.

Permissions

    castlesiege.admin: Full access to plugin commands.
    castlesiege.play: Allows players to join and play Castle Siege.

Gameplay Mechanics

    Players are divided into teams, with one player designated as the King.
    The objective is to eliminate the opposing team's King while protecting your own.
    Players can use coins earned in-game to unlock better kits and abilities.
    After each round, the map regenerates, resetting the battlefield.

Coin System

    Players earn coins by completing objectives such as kills, assists, and victories.
    Coins can be spent on cosmetic upgrades, better kits, and abilities in future matches.

Map Regeneration

    After each round, the plugin automatically regenerates the map to its original state.
    Customize the map regeneration timer and rules in the configuration files.

Frequently Asked Questions

Q: How do I set up custom kits?
A: Kits can be defined in the kits.yml file, where you specify the items, abilities, and permissions for each kit.

Q: How do I enable map regeneration?
A: Map regeneration is enabled by default but can be customized or disabled in the config.yml file.
Support

For further assistance, visit the SpigotMC resource page.