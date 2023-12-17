package MusicBot.musicCommands;

import MusicBot.GuildMusicManager;
import MusicBot.ICommand;
import MusicBot.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class Queue implements ICommand {
    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getDescription() {
        return "Will display the current queue";
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
        List<AudioTrack> queue = new ArrayList<>(guildMusicManager.getTrackScheduler().getQueue());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Current Queue");

        if (queue.isEmpty()){
            embedBuilder.setDescription("Queue is Empty");
        }
        for (int i = 0; i < queue.size(); i++){
            AudioTrackInfo info = queue.get(i).getInfo();
            embedBuilder.addField(i+1 + " ",info.title,false);
        }

        event.replyEmbeds(embedBuilder.build()).queue();


    }
}
