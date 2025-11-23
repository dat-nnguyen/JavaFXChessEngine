package core;

import entities.Alliance;

public class GameConfiguration {
    public enum GameMode {
        HUMAN_VS_HUMAN,
        HUMAN_VS_AI
    }

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    private final GameMode gameMode;
    private final Difficulty aiDifficulty;
    private final int timeControlMinutes;
    private final Alliance playerColor;

    public GameConfiguration(final GameMode gameMode,
                             final Difficulty aiDifficulty,
                             final int timeControlMinutes,
                             final Alliance playerColor) {
        this.gameMode = gameMode;
        this.aiDifficulty = aiDifficulty;
        this.timeControlMinutes = timeControlMinutes;
        this.playerColor = playerColor;
    }

    // --- GETTERS ---

    public GameMode getGameMode() {
        return this.gameMode;
    }

    public Difficulty getAiDifficulty() {
        return this.aiDifficulty;
    }

    public int getTimeControlMinutes() {
        return this.timeControlMinutes;
    }

    public Alliance getPlayerColor() {
        return this.playerColor;
    }
}
