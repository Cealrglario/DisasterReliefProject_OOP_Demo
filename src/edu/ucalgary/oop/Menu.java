package edu.ucalgary.oop;

public abstract class Menu {
    private String[] currentDisplay;
    protected InputHandler inputHandler = new InputHandler();
    protected MenuManager menuManager = MenuManager.INSTANCE;
    protected LanguageManager languageManager = LanguageManager.INSTANCE;
    protected final String[] DEFAULT_OPTIONS;
    protected int intInput;
    protected boolean requiresIntInput = true;
    protected int minIntInput;
    protected int maxIntInput;
    protected boolean stringEmptyAllowed;
    protected boolean stringNumbersAllowed;

    public Menu(String[] defaultOptions) {
        this.DEFAULT_OPTIONS = defaultOptions;
        setCurrentDisplay(getDefaultOptions());
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(getDefaultOptions().length - 1);
    }

    public String[] getCurrentDisplay() {
        return this.currentDisplay;
    }

    public void setCurrentDisplay(String[] newDisplay) {
        this.currentDisplay = newDisplay;
    }

    public String[] getDefaultOptions() {
        return this.DEFAULT_OPTIONS;
    }

    public void handleDisplay() {
        for (int i = 0; i < currentDisplay.length; i++) {
            System.out.println(currentDisplay[i]);
        }
    }

    public boolean requiresIntInput() {
        return true;
    }

    public void handleIntInput() {
        intInput = inputHandler.getIntInput(minIntInput, maxIntInput);
    }

    public String handleStringInput() {
        return inputHandler.getStringInput(stringEmptyAllowed, stringNumbersAllowed);
    }

    public void returnToMainMenu() {
        menuManager.returnToMainMenu();
    }

    public boolean getRequiresIntInput() {
        return requiresIntInput;
    }

    public void setRequiresIntInput(boolean requiresIntInput) {
        this.requiresIntInput = requiresIntInput;
    }

    public int getMinIntInput() {
        return minIntInput;
    }

    public void setMinIntInput(int minIntInput) {
        this.minIntInput = minIntInput;
    }

    public int getMaxIntInput() {
        return maxIntInput;
    }

    public void setMaxIntInput(int maxIntInput) {
        this.maxIntInput = maxIntInput;
    }

    public boolean getStringEmptyAllowed() {
        return stringEmptyAllowed;
    }

    public void setStringEmptyAllowed(boolean stringEmptyAllowed) {
        this.stringEmptyAllowed = stringEmptyAllowed;
    }

    public boolean getStringNumbersAllowed() {
        return stringNumbersAllowed;
    }

    public void setStringNumbersAllowed(boolean stringNumbersAllowed) {
        this.stringNumbersAllowed = stringNumbersAllowed;
    }

    public abstract void processInput();
}
