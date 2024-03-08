# Castle Siege Minigame Plugin README

Welcome to the Castle Siege minigame plugin! Experience thrilling battles and fun gameplay on your Minecraft server with this exciting plugin.

## Introduction

Castle Siege introduces a dynamic minigame where players can choose to join either the defending Franks or the attacking Vikings. The goal is simple: either assassinate the King as a Viking or defend the throne until time's up as a Frank. With a variety of kits, abilities, and features, Castle Siege offers an immersive experience for all players on your server.

## Features

- **2 Teams**: Engage in battle as Franks (Defenders) or Vikings (Attackers).
- **6 Kits**: Choose from 3 kits per team, allowing diverse strategies.
- **Scoreboards**: Configurable scoreboards to track progress.
- **Abilities**: Each Attacker kit has its own unique ability.
- **Kill Rewards**: Obtain potion effects like speed or regeneration upon making a kill.
- **Throwable Axes**: Intense combat experiences with throwable axes.
- **Autostart**: Start the game automatically with customizable settings.

## Getting Started

To set up the Castle Siege minigame, follow these steps:

1. Configure countdown timer, king's health, maximum players per team, autostart timers, and required number of players before launching the server.
2. Use commands with either permission viking.admin or op to set up lobby, team spawns, and king's spawn.
3. Restart the server after setting spawns.

## Commands

- `/setlobby`: Set lobby location.
- `/start`: Start the game.
- `/setspawn [teamName]`: Set team spawn location for either VIKINGS or FRANKS.
- `/setmobspawn`: Set king's spawn location.
- `/endgame`: Force stop the game without a winner.

## Kits

### Defenders Kits (FRANKS)

1. **MARKSMAN**:
   - Stone Sword
   - Crossbow
   - Heal Soup

2. **SPEARMAN**:
   - Stone Sword
   - Spear
   - Heal Soup

3. **KNIGHT**:
   - Iron Sword
   - Heal Soup

### Attackers Kits (VIKINGS)

1. **SKALD**:
   - Stone Sword
   - Bow
   - Heal Soup
   - Skald's Sight

2. **BERSERKER**:
   - Combat Axe
   - Throwable Axe
   - Berserker's Rage
   - Heal Soup

3. **WARRIOR**:
   - Warrior's Sword
   - Ragnarok
   - Heal Soup

## Custom Abilities

- **Healing Stew**: Grants regeneration for 5 seconds to every kit.
- **Vikings Abilities**:
  1. Skald's Sight: Provides jump, haste, and night vision effects.
  2. Berserker's Rage: Grants speed, resistance, and strength for 5 seconds.
  3. Ragnarok: Provides speed and strength for 5 seconds.

## Kill Rewards

### FRANKS (Defenders)

- MARKSMAN: 1 Spectral Arrow
- SPEARMAN: Speed 2 for 5 seconds
- KNIGHT: Speed, Regeneration 2 for 5 seconds

### VIKINGS (Attackers)

- SKALD: Regeneration, Resistance 2 for 5 seconds & Harm Arrow
- BERSERKER: Absorption & Regeneration 2 for 5 seconds
- WARRIOR: Regeneration & Resistance 2 for 5 seconds

## Configuration

Adjust settings in the `config.yml` file to customize your gameplay experience.

## Future Updates

Regular updates are planned, aiming for approximately twice a month. However, the frequency may vary due to other tasks.

## Bug Reports

If you encounter any bugs or have suggestions for features, please reach out via Discord rather than using the reviews section.

## Contact

Discord: cbhud

Your interest and feedback are greatly appreciated in improving the plugin.

