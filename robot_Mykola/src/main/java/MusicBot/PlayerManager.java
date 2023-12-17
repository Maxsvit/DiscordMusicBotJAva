package MusicBot;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private Map<Long,GuildMusicManager> guildMusicManagerMap = new HashMap<>();
    private AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

    public PlayerManager(){

        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public static PlayerManager get(){

        if (INSTANCE == null){
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }

    public GuildMusicManager getGuildMusicManager(Guild guild){

        return guildMusicManagerMap.computeIfAbsent(guild.getIdLong(), (guildId) ->{
            GuildMusicManager musicManager = new GuildMusicManager(audioPlayerManager);

            guild.getAudioManager().setSendingHandler(musicManager.getAudioForwarder());

            return musicManager;
        });

    }

    public void play(Guild guild, String trackUrl){

        GuildMusicManager guildMusicManager = getGuildMusicManager(guild);

        audioPlayerManager.loadItemOrdered(guildMusicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                guildMusicManager.getTrackScheduler().queue(audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {

                guildMusicManager.getTrackScheduler().queue(audioPlaylist.getTracks().get(0));

            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });

    }
}
