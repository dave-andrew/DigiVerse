package net.slc.dv;

import net.slc.dv.resources.Icon;
import net.slc.dv.resources.IconStorage;
import net.slc.dv.controller.AuthController;
import net.slc.dv.database.connection.ConnectionChecker;
import net.slc.dv.helper.ScreenManager;
import net.slc.dv.helper.StageManager;
import net.slc.dv.helper.ThemeManager;
import net.slc.dv.helper.toast.ToastBuilder;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.view.home.Home;
import net.slc.dv.view.login.LoginView;
import net.slc.dv.view.offlinegame.OfflineGameView;

public class Main extends Application {

	private AuthController authController;
	private ConnectionChecker connectionChecker;
	private Scene scene;
	private String currentScene;

	private Scene initialize() {
		authController = new AuthController();

		BorderPane borderPane = new BorderPane();

		VBox loading = new VBox();
		loading.setAlignment(Pos.CENTER);

		ProgressBar progressBar = new ProgressBar();
		progressBar.setPrefWidth(300);

		simulateLoading(progressBar);

		loading.getChildren().add(progressBar);

		borderPane.setCenter(loading);

		scene = new Scene(borderPane, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
		ThemeManager.getInstance().getTheme(scene);

		return scene;
	}

	private void simulateLoading(ProgressBar progressBar) {
		Service<Void> service = new Service<>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<>() {
					@Override
					protected Void call() throws Exception {
						for (int i = 0; i <= 100; i++) {
							updateProgress(i, 100);
							Thread.sleep(20);
						}
						return null;
					}
				};
			}
		};

		progressBar.progressProperty().bind(service.progressProperty());

		service.setOnSucceeded(e -> {
			this.connectionChecker = new ConnectionChecker();

			this.connectionChecker.getIsConnected().addListener(this::sceneHandler);
		});

		service.start();
	}

	private void sceneHandler(boolean value) {
		Stage stage = StageManager.getInstance();

		if (!value) {
			if(this.currentScene == null) {
				this.currentScene = "offline";
				new OfflineGameView(stage);
				return;
			}

			ToastBuilder.buildNormal().setText("You are offline!").build();
			this.currentScene = "offline";
			new OfflineGameView(stage);
			return;
		}

		String message = authController.checkAuth();

		if (this.currentScene != null && this.currentScene.equals("offline")) {
			ToastBuilder.buildButton()
					.setButtonText("Redirect")
					.setButtonAction(toast -> this.redirectConnected(message))
					.setOnClickClose(true)
					.setText("You are online!")
					.setFadeOutDelay(1000000)
					.build();
			return;
		}

		this.redirectConnected(message);
	}

	private void redirectConnected(String message) {
		if (message.equals("true")) {
			ToastBuilder.buildNormal().setText("Welcome back, " + LoggedUser.getInstance().getUsername() + "!").build();
			this.currentScene = "home";
			new Home(StageManager.getInstance());
			return;
		}

		if (message.equals("false")) {
			this.currentScene = "login";
			new LoginView(StageManager.getInstance());
		}
	}

	@Override
	public void start(Stage stage) {
		IconStorage.init();
		Stage primaryStage = StageManager.getInstance();
		scene = initialize();
		primaryStage.setScene(scene);

		primaryStage.setTitle("DigiVerse");
		primaryStage.getIcons().add(IconStorage.getIcon(Icon.APP_LOGO));
		stage.initStyle(StageStyle.UTILITY);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
