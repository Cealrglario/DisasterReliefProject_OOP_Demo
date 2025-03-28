package edu.ucalgary.oop;

public abstract class Menu {
    private String[] currentDisplay;
    protected final String[] DEFAULT_OPTIONS;

    public Menu(String[] defaultOptions) {
        this.DEFAULT_OPTIONS = defaultOptions;
    }

    public String[] getCurrentDisplay() {
        return this.currentDisplay;
    }

    public void setCurrentDisplay(String[] newDisplay) {}

    public String[] getDefaultOptions() {
        return this.DEFAULT_OPTIONS;
    }

    public void handleDisplay() {}

    public void handleInput() {}

    public void returnToMainMenu() {}
}
