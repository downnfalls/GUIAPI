package me.downn_falls.guiapi.api;

import me.downn_falls.guiapi.utils.GuiUtils;

public class InputResult {
    public static InputResult SUCCESS = new InputResult(null, false);
    public static InputResult ERROR = new InputResult(null, true);
    public static InputResult success(String message) { return new InputResult(message, false); }
    public static InputResult error(String message) { return new InputResult(message, true); }

    private final String message;
    private final boolean error;

    private InputResult(String message, boolean error) {
        this.message = GuiUtils.colorize(message);
        this.error = error;
    }

    public String getMessage() {
        return message;
    }
    public boolean isError() {
        return error;
    }
}
