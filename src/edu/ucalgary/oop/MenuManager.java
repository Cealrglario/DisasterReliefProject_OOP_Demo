package edu.ucalgary.oop;

import java.util.Stack;

public enum MenuManager {
    INSTANCE;

    private boolean isRunning = false;
    private Menu currentMenu;
    private Stack<Menu> menuHistory;
    private LanguageManager languageManager = LanguageManager.INSTANCE;

    public Menu getCurrentMenu() {
        return this.currentMenu;
    }

    public void navigateToMenu(Menu menu) {
        menuHistory.push(menu);
        currentMenu = menu;
    }

    public void backtrackMenus() {
        menuHistory.pop();
        currentMenu = menuHistory.peek();
    }

    public void returnToMainMenu() {
        menuHistory.clear();
        navigateToMenu(new MainMenu(languageManager.getMenuTranslation("main_menu_defaults")));
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

        if (currentMenu.requiresIntInput()) {
            currentMenu.handleIntInput();
        } else {
            currentMenu.handleStringInput();
        }

        currentMenu.processInput();
    }


}

