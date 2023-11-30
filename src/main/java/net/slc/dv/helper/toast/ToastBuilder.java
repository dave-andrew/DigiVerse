package net.slc.dv.helper.toast;


import net.slc.dv.enums.ToastType;

public class ToastBuilder {
    public static Toast buildNormal(ToastType type) {
        return new Toast();
    }

    public static ButtonToast buildButton(ToastType type) {
        return new ButtonToast();
    }
}
