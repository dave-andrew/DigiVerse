package net.slc.dv.storage;

import net.slc.dv.enums.Language;
import net.slc.dv.resources.Text;

import java.util.HashMap;
import java.util.Map;

public class TextStorage {
	private static final Map<Text, String> textMap = new HashMap<>();

	public static void init(){
		for(Text text : Text.values()){
			textMap.put(text, text.getText(Language.INDONESIAN));
		}
	}

	public static void changeLanguage(Language language) {
		for(Text text : Text.values()){
			textMap.put(text, text.getText(language));
		}
	}

	public static String getText(Text text){
		return textMap.get(text);
	}
}
