package helper;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputManager {

    private static InputManager gameInput;
    private static final ArrayList<KeyCode> pressedKeys = new ArrayList<>();
    private static final ArrayList<MouseButton> mouseClicks = new ArrayList<>();

    private InputManager(Scene scene) {
        handlePlayerInput(scene);
    }

    public static InputManager getInstance(Scene scene) {
        if (gameInput == null) {
            gameInput = new InputManager(scene);
        }
        return gameInput;
    }

    public static ArrayList<KeyCode> getPressedKeys() {
        return pressedKeys;
    }

    public static ArrayList<MouseButton> getMouseClicks() {
        return mouseClicks;
    }

    private void handlePlayerInput(Scene scene) {
        KeyCode[] allowedKeys = { KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.W, KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT,
                KeyCode.RIGHT };
        List<KeyCode> allowedKeysList = Arrays.asList(allowedKeys);

        scene.setOnKeyPressed(e -> {
            KeyCode keyCode = e.getCode();
            System.out.println(keyCode);
            if (!pressedKeys.contains(keyCode) && allowedKeysList.contains(keyCode)) {
                pressedKeys.add(keyCode);
            }

            if (keyCode == KeyCode.Z) {

            }
        });

        scene.setOnKeyReleased(e -> {
            KeyCode keyCode = e.getCode();
            pressedKeys.remove(keyCode);
        });

        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            mouseClicks.add(e.getButton());
        });

        scene.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            mouseClicks.remove(e.getButton());
        });
    }

}
