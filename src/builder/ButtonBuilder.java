package builder;

import javafx.scene.control.Button;

import java.util.function.Consumer;

public class ButtonBuilder {

	private Button button;
	private ButtonBuilder(String text){
		this.button = new Button(text);
	}

	private ButtonBuilder(Button button){
		this.button = button;
	}

	public static ButtonBuilder from(Button button){
		return new ButtonBuilder(button);
	}

	public static ButtonBuilder create(String text){
		return new ButtonBuilder(text);
	}

	public ButtonBuilder setStyle(String style){
		this.button.setStyle(style);

		return this;
	}

	public ButtonBuilder setStyleClass(String styleClass){
		this.button.getStyleClass().add(styleClass);

		return this;
	}

	public ButtonBuilder setPrefWidth(int width){
		this.button.setPrefWidth(width);

		return this;
	}

	public ButtonBuilder setOnAction(Consumer consumer){
		this.button.setOnAction(e -> consumer.accept(this.button));

		return this;
	}

	public Button build(){
		return this.button;
	}


}
