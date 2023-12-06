package net.slc.dv.resources;

import net.slc.dv.enums.Theme;

public enum Icon {
	HOME("file:resources/icons/light/home.png", "file:resources/icons/dark/home.png"),
	CALENDAR("file:resources/icons/light/calendar.png", "file:resources/icons/dark/calendar.png"),
	LOGOUT("file:resources/icons/light/logout.png"),
	LEFT_NAV_ARROW("file:resources/icons/light/left-nav.png", "file:resources/icons/dark/left-nav.png"),
	RIGHT_NAV_ARROW("file:resources/icons/light/right-nav.png", "file:resources/icons/dark/right-nav.png"),
	LEFT_ARROW("file:resources/icons/light/left-arrow.png", "file:resources/icons/dark/left-arrow.png"),
	RIGHT_ARROW("file:resources/icons/light/right-arrow.png"),
	PLUS("file:resources/icons/light/plus.png", "file:resources/icons/dark/plus.png"),
	SUN("file:resources/icons/light/sun.png"),
	MOON("file:resources/icons/light/moon.png"),
	LOGO("file:resources/icons/light/logo.png"),
	TASK("file:resources/icons/light/task.png"),
	PDF("file:resources/icons/light/pdf.png"),
	IMAGE("file:resources/icons/light/image.png"),
	FILE("file:resources/icons/light/file.png"),
	BIN("file:resources/icons/light/bin.png"),
	USER("file:resources/icons/light/user.png", "file:resources/icons/dark/user.png"),
	CLOSE("file:resources/icons/light/close.png", "file:resources/icons/dark/close.png"),
	SAVE("file:resources/icons/light/save.png", "file:resources/icons/dark/save.png"),
	APP_LOGO("file:resources/icons/light/app_logo.png"),
	PLUS_WHITE("file:resources/icons/light/plus-white.png"),
	CHANGE_PASSWORD("file:resources/icons/light/change-password.png", "file:resources/icons/dark/change-password.png"),
	EMAIL("file:resources/icons/light/email.png", "file:resources/icons/dark/email.png"),
	DOWN_ARROW("file:resources/icons/light/down-arrow.png", "file:resources/icons/dark/down-arrow.png");

	private final String pathLight;
	private final String pathDark;

	Icon(String path) {
		this.pathLight = path;
		this.pathDark = path;
	}

	Icon(String pathLight, String pathDark) {
		this.pathLight = pathLight;
		this.pathDark = pathDark;
	}

	public String getPath(Theme theme) {
		return theme.equals(Theme.DARK) ? pathDark : pathLight;
	}
}
