# 🛝 Playground *(Formerly Housing2)*
Originally created by Al3x with the idea of recreating Hypixel's gamemode Housing due to the fact at the time Housing went over 1 year without an update. The goal was to recreate the gamemode as if it were to constantly get new features and updates.

Now the project has a team of a few people who maintain the project as well as the public server you can join right now.

# Public Minecraft Server
### IP: playground.redstone.llc
### Version: 1.21.4+

# The Team:
### Developers:
- [Al3xWarrior](https://github.com/al3xwarrior)
- [Sin_Ender](https://github.com/sinender)
- [VariousCacti](https://github.com/VariousCacti)
- [Wekend](https://github.com/Wekendd)
- [BusterBrown1218](https://github.com/BusterBrown1218)
### Playtesters:
- [pjma](https://github.com/npjma)
- [PixelBedrock](https://github.com/PixelBedrock)

# How to Use on your own:
### Requirements
- Java 21 or newer
- Git

### Compiling from source
```bash
git clone https://github.com/al3xwarrior/Playground.git
cd Playground/

# to build the plugin:
./gradlew :paper:shadowJar

# to build the client mod:
./gradlew :fabric:remapJar
```
You can find the output plugin JAR in the `paper/build/libs` directory and the output mod JAR in the `fabric/build/libs` directory.

### Running on a server
You must use [AdvancedSlimePaper 1.21.4](https://ci.infernalsuite.com/repository/download/AdvancedSlimePaper_Build/2762:id/output/asp-server.jar) as your server software.
Move the output plugin JAR to the server's `plugins` directory, along with these dependencies as a minimum:
- [Citizens](https://ci.citizensnpcs.co/job/citizens2/)
- [NoteBlockAPI](https://modrinth.com/plugin/noteblockapi)
- [packetevents](https://modrinth.com/plugin/packetevents)
- [ProtocolLib](https://github.com/dmulloy2/ProtocolLib/releases)
