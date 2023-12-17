package MusicBot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {


    private final AudioPlayer audioPlayer;
    private BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();
    private boolean isRepeat = false;


    public TrackScheduler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (isRepeat){
            player.startTrack(track.makeClone(), false);

        }
        else {
            audioPlayer.startTrack(queue.poll(), false);
        }
    }

    public void queue(AudioTrack audioTrack){
        if (!audioPlayer.startTrack(audioTrack,true)){
            queue.offer(audioTrack);
        }
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }
}
