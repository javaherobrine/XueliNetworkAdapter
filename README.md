# Introduction
It's an extension for <a href="https://github.com/javaherobrine/craftgame-tcp-library">CraftGame TCP Library</a>.

# Motivation
From my compilation result, `io.github.javaherobrine.net.EventContent` shares the same meaning with `xueli.game2.network.Packet`, with different specifications. Initially, they are used for multiplayer. So, I'm here to fully support LovelyZeeiam's network implementation specifications with CraftGame TCP Library.

# Goals
To create a new protocol that is capable for his or her implementation specifications(Both server & client). That means, with this extension, the program can interact with LovelyZeeiam's server correctly. Also, LovelyZeeiam's client can connect to your server created by CraftGame TCP Library.

# Non goals
It's not my goal to make this extension as an alternative dependency of LovelyZeeiam's to fully provider his or her APIs. That means, it's only used for CraftGame TCP Library, as its extension.

# References

<a href="https://github.com/javaherobrine/craftgame-tcp-library">CraftGame TCP Library</a> <br />

<a href="https://github.com/javaherobrine/CraftGame_from_lovelyzeeiam/tree/minecraft-classic-remake/src/main/java/xueli/game2/network">LovelyZeeiam's implementation specifications</a>

# Special thanks to CraftGame Studio!