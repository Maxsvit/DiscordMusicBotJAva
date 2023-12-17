package org.example;

import MusicBot.CommandManager;
import MusicBot.musicCommands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class BotMykola {
    public static void main(String[] args) {

        JDA jda = JDABuilder.createDefault("MTE4NDk5MjA0Nzg0MTY3MzI3Nw.GEfEQt.PfhaG9gtY77VIAfo8TR7Bi3AO0zW07Ilpo9RPc").build();

        CommandManager commandManager = new CommandManager();
        commandManager.add(new Play());
        commandManager.add(new Skip());
        commandManager.add(new Stop());
        commandManager.add(new NowPlaying());
        commandManager.add(new Queue());
        commandManager.add(new Repeat());
        jda.addEventListener(commandManager);

    }
}