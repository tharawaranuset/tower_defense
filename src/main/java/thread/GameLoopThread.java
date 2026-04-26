package thread;

import controller.GameController;
import main.Main;

public class GameLoopThread extends Thread {

    private static final long TARGET_FPS_MS = 16;

    private final GameController controller;
    private final Main screen;
    private boolean running = true;

    public GameLoopThread(GameController controller, Main screen) {
        this.controller = controller;
        this.screen = screen;
    }

    @Override
    public void run() {
        while (running) {
            long start = System.currentTimeMillis();

            controller.update();
            screen.render();

            long elapsed = System.currentTimeMillis() - start;
            // 16 ms approximately to 60 FPS
            long sleep = TARGET_FPS_MS - elapsed;

            if (sleep > 0) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void stopLoop() {
        running = false;
    }
}