# Samurai Dragons (Forge 1.20.4)

Minecraft 1.20.4, Forge 49.x (works with any 49.x build, including the one Aternos offers).

## Get the mod jar
**Option A - build in the cloud (no installs):**
1. Create a free GitHub account and a new repository.
2. Upload ALL files of this folder to it (including the hidden `.github` folder; if it is skipped, create the file
   `.github/workflows/build.yml` by hand with "Add file > Create new file" and paste the contents from this project).
3. Open the **Actions** tab > "Build mod" > wait for the green check > open the run > download **samurai-dragons-jar**.
4. Unzip it: inside is `samurai-dragons-1.0.0.jar`.

**Option B - build on your computer:** install JDK 17 and Gradle 8.5+, then in this folder run
`gradle wrapper --gradle-version 8.5` and `./gradlew build`. The jar is in `build/libs/`.

## Install
- Single player / your client: put the jar in `.minecraft/mods` with Forge 1.20.4.
- Aternos: Software > Forge 1.20.4, then Files > `mods` > Upload the jar. Everyone who joins needs the same jar.

## Content
- **Dragon Katana**: fire on hit, right click = dragon fireball (5s cooldown).
- **Oni Mask / Ronin Scroll**: right click to summon the boss.
- **Bosses**: Oni Shogun (300 HP: slam / fireballs / lightning), Shadow Ronin (200 HP: teleport strikes / sweep slashes). 3 phases, boss bar. Drop Dragon Scales and Dragon Leather.
- **Samurai armor**: iron ingots + red wool. Full set = Speed.
- **Dragon Hide armor**: Dragon Leather (+1 Dragon Scale in chest/leggings), fireproof. Full set = Fire Resistance.
- **Rideable Dragon**: Dragon Whistle (3 Dragon Scale + Ender Pearl + Stick, pattern " S ","SPS"," T ") or spawn egg. Right click to mount; first rider becomes owner.
  W = fly forward, A/D = strafe, look up/down = climb/dive, release W = hover, Shift = dismount. No fall damage; cooked beef heals it.
- Dragon texture is a placeholder on the vanilla phantom model (scaled up).
