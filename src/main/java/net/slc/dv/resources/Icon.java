package net.slc.dv.resources;

import net.slc.dv.enums.Theme;

public enum Icon {
	HOME("file:resources/icons/home.png"),
	CALENDAR("file:resources/icons/calendar.png"),
	LOGOUT("file:resources/icons/logout.png"),
	LEFT_NAV_ARROW("file:resources/icons/left-nav.png"),
	RIGHT_NAV_ARROW("file:resources/icons/right-nav.png"),
	LEFT_ARROW("file:resources/icons/left-arrow.png"),
	RIGHT_ARROW("file:resources/icons/right-arrow.png"),
	PLUS("file:resources/icons/plus.png"),
	SUN("file:resources/icons/sun.png"),
	MOON("file:resources/icons/moon.png"),
	LOGO("file:resources/icons/logo.png"),
	TASK("file:resources/icons/task.png"),
	PDF("file:resources/icons/pdf.png"),
	IMAGE("file:resources/icons/image.png"),
	FILE("file:resources/icons/file.png"),
	BIN("file:resources/icons/bin.png"),
	USER("file:resources/icons/user.png"),
	CLOSE("file:resources/icons/close.png"),
	SAVE("file:resources/icons/save.png"),
	APP_LOGO("file:resources/icons/app_logo.png"),
	PLUS_WHITE("file:resources/icons/plus-white.png");

	private String pathLight;
	private String pathDark;

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
