package edu.ucalgary.oop;

import java.util.Stack;

public enum MenuManager {
    INSTANCE;

    private static boolean isRunning;
    private static Stack<Menu> menuHistory;

    public void initializeMenuLanguage() {}

    public void navigateToMenu(Menu menu) {}

    public void backtrackMenus() {}

    public boolean getIsRunning() {
        return isRunning;
    }

    public void exit() {}

    public void run() {}

}

