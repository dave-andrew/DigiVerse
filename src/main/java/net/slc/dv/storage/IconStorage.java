package net.slc.dv.storage;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import net.slc.dv.enums.Theme;
import net.slc.dv.resources.Icon;

import java.util.HashMap;
import java.util.Map;


public class IconStorage {

	private static final Map<Icon, ObjectProperty<Image>> iconMap = new HashMap<>();

	public static void init(){
		for(Icon icon : Icon.values()){
			iconMap.put(icon, new SimpleObjectProperty<>(new Image(icon.getPath(Theme.LIGHT))));
		}
	}

	public static void changeTheme(Theme theme) {
		if (theme.equals(Theme.LIGHT)) {
			for(Icon icon : Icon.values()){
				iconMap.get(icon).setValue(new Image(icon.getPath(Theme.LIGHT)));
			}
		} else if (theme.equals(Theme.DARK)) {
			for(Icon icon : Icon.values()){
				iconMap.get(icon).setValue(new Image(icon.getPath(Theme.DARK)));
			}
		}

	}

	public static ObjectProperty<Image> getIcon(Icon icon){
		return iconMap.get(icon);
	}
}
