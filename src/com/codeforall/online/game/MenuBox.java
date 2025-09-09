package com.codeforall.online.game;

import com.codeforall.online.game.buttons.Buttons;
import com.codeforall.online.game.inputs.MouseMenus;
import com.codeforall.simplegraphics.mouse.Mouse;
import com.codeforall.simplegraphics.mouse.MouseEvent;
import com.codeforall.simplegraphics.mouse.MouseEventType;
import com.codeforall.simplegraphics.mouse.MouseHandler;
import com.codeforall.simplegraphics.pictures.Picture;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class MenuBox {

    public static final List<MouseMenus> activeButtons = Collections.synchronizedList(new ArrayList<>());
    private static Mouse mouse;

    public static void init(Game mainGame) {
        if (mouse == null) {
            mouse = new Mouse(new GlobalMouseHandler());
            mouse.addEventListener(MouseEventType.MOUSE_CLICKED);
        }
        drawMainMenu(mainGame);
    }

    public static void drawMainMenu(Game mainGame) {
        synchronized (activeButtons) {
            activeButtons.clear();
        }

        Picture backFull = new Picture(0, 0, "resources/background.png");
        backFull.draw();

        Picture mainTitle = new Picture(544, 158, "resources/main-menu_main-elements/base-elements/oi.Raga_title.png");
        mainTitle.draw();

        Buttons timerButton = new Buttons(300, 370, "resources/main-menu_main-elements/base-elements/timer_button.png");
        Buttons playButton = new Buttons(timerButton.getX() + 350, 370, "resources/main-menu_main-elements/base-elements/start_button.png");
        Buttons babyButton = new Buttons(playButton.getX() + 350, 370, "resources/main-menu_main-elements/base-elements/easy_button.png");
        Buttons instructionsButton = new Buttons(100, 640, "resources/main-menu_main-elements/base-elements/instructions_button.png");
        Buttons quitButton = new Buttons(1370, 640, "resources/main-menu_main-elements/base-elements/quit_button.png");

        Runnable startGame = () -> {
            new Thread(() -> {
                try {
                    backFull.delete();
                    mainTitle.delete();
                    timerButton.delete();
                    playButton.delete();
                    babyButton.delete();
                    instructionsButton.delete();
                    quitButton.delete();
                    synchronized (activeButtons) {
                        activeButtons.clear();
                    }
                    mainGame.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        };

        synchronized (activeButtons) {
            activeButtons.add(new MouseMenus(playButton, startGame));
            activeButtons.add(new MouseMenus(timerButton, startGame));
            activeButtons.add(new MouseMenus(babyButton, startGame));

            activeButtons.add(new MouseMenus(instructionsButton, () -> {
                backFull.delete();
                mainTitle.delete();
                timerButton.delete();
                playButton.delete();
                babyButton.delete();
                instructionsButton.delete();
                quitButton.delete();
                synchronized (activeButtons) {
                    activeButtons.clear();
                }
                showInstructions(mainGame);
            }));

            activeButtons.add(new MouseMenus(quitButton, () -> {
                System.out.println("Quit button clicked. Exiting...");
                System.exit(0);
            }));
        }
    }

    public static void showInstructions(Game mainGame) {
        synchronized (activeButtons) {
            activeButtons.clear();
        }

        final int[] currentIndex = {0};
        final int maxIndex = 5;

        Buttons instructionButton = new Buttons(540, 80, "resources/main-menu_main-elements/explanation-menu/explanation_page_0.png");

        synchronized (activeButtons) {
            activeButtons.add(new MouseMenus(instructionButton, () -> {
                currentIndex[0]++;
                if (currentIndex[0] > maxIndex) {
                    instructionButton.delete();
                    drawMainMenu(mainGame);
                } else {
                    instructionButton.load("resources/main-menu_main-elements/explanation-menu/explanation_page_" + currentIndex[0] + ".png");
                }
            }));
        }
    }

    private static class GlobalMouseHandler implements MouseHandler {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = (int) e.getX();
            int y = (int) e.getY();

            synchronized (activeButtons) {
                for (MouseMenus menu : activeButtons) {
                    if (menu.checkClick(x, y)) break;
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }
}