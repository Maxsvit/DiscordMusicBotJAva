package MusicBot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class AudioForwarder implements AudioSendHandler {

    private final AudioPlayer audioPlayer;
    private final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    private final MutableAudioFrame frame = new MutableAudioFrame();

    public AudioForwarder(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        frame.setBuffer(byteBuffer);
    }

    @Override
    public boolean canProvide() {
        return audioPlayer.provide(frame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return byteBuffer.flip();
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
