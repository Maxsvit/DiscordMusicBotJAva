package MusicBot.musicCommands;

import MusicBot.GuildMusicManager;
import MusicBot.ICommand;
import MusicBot.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Repeat implements ICommand {
    @Override
    public String getName() {
        return "repeat";
    }

    @Override
    public String getDescription() {
        return "Will toggle repeating";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
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

        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        boolean isRepeat = !guildMusicManager.getTrackScheduler().isRepeat();
        guildMusicManager.getTrackScheduler().setRepeat(isRepeat);
        event.reply("Repeat is now " + isRepeat).queue();

    }
}
