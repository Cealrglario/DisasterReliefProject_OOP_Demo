package edu.ucalgary.oop;

import java.util.Stack;

public enum MenuManager {
    INSTANCE;

    private static boolean isRunning;
    private static Menu currentMenu;
    private static Stack<Menu> menuHistory;

    public void initializeMenuLanguage() {}

    public void navigateToMenu(Menu menu) {
        menuHistory.push(menu);
        currentMenu = menu;
    }

    public void backtrackMenus() {
        menuHistory.pop();
        currentMenu = menuHistory.peek();
    }

    public void startRunning() {
        isRunning = true;
    }

    public boolean getIsRunning() {
        return isRunning;
    }

    public void stopRunning() {
        isRunning = false;
    }

    public void run() {
        currentMenu.handleDisplay();
        currentMenu.handleInput();
    }

}

