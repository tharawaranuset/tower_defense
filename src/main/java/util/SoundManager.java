package util;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private static SoundManager instance;

    private final Map<String, AudioClip> clips = new HashMap<>();
    private final Map<String, MediaPlayer> tracks = new HashMap<>();
    private boolean muted = false;

    private SoundManager() {
        loadAllClips("/sounds/tower");

        loadTrack("bgm", "/sounds/background.mp3");
    }

    private void loadAllClips(String folderPath) {
        try {
            var url = getClass().getResource(folderPath);

            if (url == null) {
                System.out.println("Folder not found: " + folderPath);
                return;
            }

            File folder = new File(url.toURI());

            File[] files = folder.listFiles();
            if (files == null) return;

            for (File file : files) {
                String name = file.getName();

                if (name.endsWith(".mp3")) {
                    String key = name.substring(0, name.lastIndexOf('.'));
                    loadClip(key, folderPath + "/" + name);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SoundManager getInstance() {
        if (instance == null) instance = new SoundManager();
        return instance;
    }

    private void loadClip(String key, String path) {
        try {
            var url = getClass().getResource(path);
            if (url != null) clips.put(key, new AudioClip(url.toString()));
            else System.out.println("Clip not found: " + path);
        } catch (Exception e) {
            System.out.println("Failed to load clip: " + path);
        }
    }

    private void loadTrack(String key, String path) {
        try {
            var url = getClass().getResource(path);
            if (url != null) {
                Media media = new Media(url.toString());
                MediaPlayer player = new MediaPlayer(media);
                player.setCycleCount(MediaPlayer.INDEFINITE);  // loop
                tracks.put(key, player);
            } else {
                System.out.println("Track not found: " + path);
            }
        } catch (Exception e) {
            System.out.println("Failed to load track: " + path);
        }
    }

    // play sound effect
    public void play(Object obj) {
        if (obj == null) return;
        String key = obj.getClass().getSimpleName();

        if (muted) return;
        if (clips.containsKey(key)) {
            clips.get(key).play();
        } else if (tracks.containsKey(key)) {
            tracks.get(key).seek(tracks.get(key).getStartTime());
            tracks.get(key).play();
        }
    }

    // play background sound, loop
    public void playBgm() {
        if (muted || !tracks.containsKey("bgm")) return;
        tracks.get("bgm").play();
    }

    public void stopBgm() {
        if (tracks.containsKey("bgm")) tracks.get("bgm").stop();
    }

    public void pauseBgm() {
        if (tracks.containsKey("bgm")) tracks.get("bgm").pause();
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
        if (muted) stopBgm();
    }
}