package net.slc.dv.helper.toast;

public class ToastBuilder {

    public static Toast buildNormal() {
        return new Toast();
    }

    public static ButtonToast buildButton() {
        return new ButtonToast();
    }

}
