package net.slc.dv.storage;

import net.slc.dv.enums.Language;
import net.slc.dv.resources.Text;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TextStorage {
	private static final Map<Text, String> textMap = new HashMap<>();
	private static Language language = Language.ENGLISH;

	public static void init(){
		Locale locale = Locale.getDefault();
		String localLanguage = locale.getLanguage();

        switch (localLanguage) {
            case "id":
                language = Language.INDONESIAN;
                break;
            case "ko":
                language = Language.KOREAN;
                break;
            case "ja":
                language = Language.JAPANESE;
                break;
            case "zh":
                language = Language.CHINESE;
                break;
            case "es":
                language = Language.SPANISH;
                break;
            case "nl":
                language = Language.DUTCH;
                break;
            case "vi":
                language = Language.VIETNAMESE;
                break;
            case "ar":
                language = Language.ARABIC;
                break;
            case "in":
                language = Language.INDIAN;
                break;
            default:
                language = Language.ENGLISH;
                break;
        }

		for(Text text : Text.values()){
			textMap.put(text, text.getText(language));
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
