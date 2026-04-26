package thread;

import controller.WaveController;

public class SpawnThread extends Thread {
    private static final long DEFAULT_INTERVAL_MS = 2000;

    private final WaveController waveController;
    private boolean running = true;
    private long intervalMs;

    public SpawnThread(WaveController waveController) {
        this.waveController = waveController;
        this.intervalMs = DEFAULT_INTERVAL_MS;
    }

    public void setIntervalMs(long intervalMs) {
        this.intervalMs = intervalMs;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(intervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            waveController.spawnNext();
        }
    }

    public void stopSpawn() {
        running = false;
    }
}