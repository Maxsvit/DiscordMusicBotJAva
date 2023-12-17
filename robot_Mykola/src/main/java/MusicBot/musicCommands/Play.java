package MusicBot.musicCommands;

import MusicBot.ICommand;
import MusicBot.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Play implements ICommand {
    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Will play a song";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING,"name","Name of the song to play"));
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        Member member = event.getMember();
        GuildVoiceState memberGuildVoiceState= member.getVoiceState();

        if (!memberGuildVoiceState.inAudioChannel()){
            event.reply("You need to be in a voice channel").queue();
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if(!selfVoiceState.inAudioChannel()){

            event.getGuild().getAudioManager().openAudioConnection(memberGuildVoiceState.getChannel());
        }
        else {
            if (selfVoiceState.getChannel() != memberGuildVoiceState.getChannel()){
                event.reply("You need to be in the same channel as me").queue();
                return;
            }
        }

        String name =  event.getOption("name").getAsString();
        try {
            new URI(name);
        } catch (URISyntaxException e) {
            name = name + "ytsearch:";
        }

        PlayerManager playerManager = PlayerManager.get();
        playerManager.play(event.getGuild(),name);

    }
}
