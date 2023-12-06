package net.slc.dv.resources;

import javafx.scene.image.Image;
import lombok.Getter;
import net.slc.dv.enums.Theme;

import java.util.HashMap;
import java.util.Map;


public class IconStorage {

	private static final Map<Icon, String> iconMap = new HashMap<>();

	public static void init(){
		for(Icon icon : Icon.values()){
			iconMap.put(icon, icon.getPath(Theme.LIGHT));
		}
	}

	public static void changeTheme(Theme theme) {
		if (theme.equals(Theme.LIGHT)) {
			for(Icon icon : Icon.values()){
				iconMap.put(icon, icon.getPath(Theme.LIGHT));
			}
		} else if (theme.equals(Theme.DARK)) {
			for(Icon icon : Icon.values()){
				iconMap.put(icon, icon.getPath(Theme.DARK));
			}
		}

	}

	public static Image getIcon(Icon icon){
		return new Image(iconMap.get(icon));
	}
}
