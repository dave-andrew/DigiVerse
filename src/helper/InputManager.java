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
    private static final ArrayList<Character> typedChars = new ArrayList<>();

    private Scene scene;

    private InputManager(Scene scene) {
        this.scene = scene;
        handlePlayerInput();
    }

    public static synchronized InputManager getInstance(Scene scene) {
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

    private void handlePlayerInput() {
        KeyCode[] allowedKeys = {KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.W, KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT,
                KeyCode.RIGHT, KeyCode.SPACE, KeyCode.ESCAPE};
        List<KeyCode> allowedKeysList = Arrays.asList(allowedKeys);

        this.scene.setOnKeyPressed(e -> {
            KeyCode keyCode = e.getCode();
            if (!pressedKeys.contains(keyCode) && allowedKeysList.contains(keyCode)) {
                pressedKeys.add(keyCode);
            }

            if (keyCode.isLetterKey()) {
                typedChars.add(keyCode.getChar().charAt(0));
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
            typedChars.clear();
            System.out.println("GOD MODE ACTIVATED");
        }

        if (dropRateCheat()) {
            typedChars.clear();
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

    private boolean checkCheat(String cheat) {
        int typedSize = typedChars.size();

        StringBuilder lastTypedChars = new StringBuilder();
        for (int i = Math.max(0, typedSize - cheat.length()); i < typedSize; i++) {
            lastTypedChars.append(typedChars.get(i));
        }

        return lastTypedChars.toString().equals(cheat);
    }


}
