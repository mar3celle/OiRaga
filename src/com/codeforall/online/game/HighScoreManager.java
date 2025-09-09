package com.codeforall.online.game;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class HighScoreManager {

    // ficheiro pra guardar o score
    private static final String FILE_NAME = System.getProperty("user.home")
            + File.separator + ".agar_offline_highscore.txt";

    public int loadHighScore() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line = br.readLine();
            if (line == null) return 0;
            return Integer.parseInt(line.trim());
        } catch (Exception e) {
            return 0; // ficheiro corrompido → recomeça
        }
    }

    public void saveHighScore(int score) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(FILE_NAME, false), StandardCharsets.UTF_8))) {
            bw.write(Integer.toString(score));
            bw.newLine();
        } catch (IOException ignored) { }
    }

    // Atualiza e grava se o score atual for maior.
    public int updateIfBest(int current, int previousHigh) {
        if (current > previousHigh) {
            saveHighScore(current);
            return current;
        }
        return previousHigh;
    }
}
