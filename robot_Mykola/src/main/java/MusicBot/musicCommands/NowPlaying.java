package MusicBot.musicCommands;

import MusicBot.GuildMusicManager;
import MusicBot.ICommand;
import MusicBot.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class NowPlaying implements ICommand {
    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public String getDescription() {
        return "Will display the current playing song";
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

        if (guildMusicManager.getTrackScheduler().getAudioPlayer().getPlayingTrack() == null){
            event.reply("I am not playing anything").queue();
            return;
        }
        AudioTrackInfo audioTrackInfo = guildMusicManager.getTrackScheduler().getAudioPlayer().getPlayingTrack().getInfo();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Currently Playing");
        embedBuilder.setDescription("**Name:** '" + audioTrackInfo.title + "'");
        embedBuilder.appendDescription("\n**Author:** '" + audioTrackInfo.author + "'");
        embedBuilder.appendDescription("\n**URL:** '" + audioTrackInfo.uri + "'");
        event.replyEmbeds(embedBuilder.build()).queue();



    }
}
