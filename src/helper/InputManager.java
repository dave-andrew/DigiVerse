package helper;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.HashSet;
import java.util.Set;

public class InputManager {

    private static final Set<KeyCode> pressedKeys = new HashSet<>();
    private static final Set<MouseButton> mouseClicks = new HashSet<>();
    private static InputManager inputManager;

    private final Scene scene;
    private final StringBuilder typedChars = new StringBuilder();
    private final int CHARACTER_LIMIT = 10;

    private InputManager(Scene scene) {
        this.scene = scene;
        handlePlayerInput();
    }

    public static synchronized InputManager getInstance(Scene scene) {
        if (inputManager == null) {
            inputManager = new InputManager(scene);
        }
        return inputManager;
    }

    public static Set<KeyCode> getPressedKeys() {
        return pressedKeys;
    }

    public static Set<MouseButton> getMouseClicks() {
        return mouseClicks;
    }

    private void handlePlayerInput() {
        this.scene.setOnKeyPressed(e -> {
            KeyCode keyCode = e.getCode();
            switch (keyCode) {
                case A:
                case S:
                case D:
                case W:
                case UP:
                case DOWN:
                case LEFT:
                case RIGHT:
                case SPACE:
                case ESCAPE:
                    pressedKeys.add(keyCode);
                    break;
            }

            if (keyCode.isLetterKey()) {
                typedChars.append(keyCode.getChar().charAt(0));
                truncateCharacters();
                checkCheats();
            }
        });

        this.scene.setOnKeyReleased(e -> {
            KeyCode keyCode = e.getCode();
            pressedKeys.remove(keyCode);
        });

        this.scene.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            mouseClicks.add(e.getButton());
        });

        this.scene.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            mouseClicks.remove(e.getButton());
        });
    }

    public void checkCheats() {
        if (godCheat()) {
            System.out.println("GOD MODE ACTIVATED");
        }

        if (dropRateCheat()) {
            System.out.println("DROP RATE CHEAT ACTIVATED");
        }
    }

    public boolean godCheat() {
        String cheat = "GOD";
        return checkCheat(cheat);
    }

    public boolean dropRateCheat() {
        String cheat = "DROP";
        return checkCheat(cheat);
    }

    private void truncateCharacters() {
        if (typedChars.length() > CHARACTER_LIMIT) {
            typedChars.setLength(CHARACTER_LIMIT);
        }
    }

    private boolean checkCheat(String cheat) {
        int typedSize = typedChars.length();

        int startIndex = Math.max(0, typedSize - cheat.length());
        StringBuilder lastTypedChars = new StringBuilder(
                typedChars.substring(startIndex, typedSize)
        );

        if (lastTypedChars.length() > CHARACTER_LIMIT) {
            lastTypedChars.setLength(CHARACTER_LIMIT);
        }

        return lastTypedChars.toString().equals(cheat);
    }

}
