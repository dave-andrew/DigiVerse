package database.connection;

import helper.ObservableVariable;

import java.sql.SQLException;

public class ConnectionChecker implements Runnable {

    private final Connect connect;
    private final ObservableVariable<Boolean> isConnected;
    private final int CONNECTION_CHECK_INTERVAL = 1000;

    public ConnectionChecker() {
        this.connect = Connect.getConnection();
        this.isConnected = new ObservableVariable<>(null);

        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();

        this.checkConnectionChange();
    }

    private void checkConnectionChange() {
        this.isConnected.addListener(value -> {
            if (value.equals(true)) {
                System.out.println("connected..");
                return;
            }

            System.out.println("disconnected");
        });
    }

    private void checkConnection() {
        // System.out.println("Checking connection...");
        try {
            Thread.sleep(CONNECTION_CHECK_INTERVAL);

            if (connect.getConnect() == null || !connect.getConnect().isValid(5)) {
                this.connect.reconnect();
                isConnected.setValue(false);
                return;
            }

            isConnected.setValue(true);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ObservableVariable<Boolean> getIsConnected() {
        return isConnected;
    }

    @Override
    public void run() {
        do {
            this.checkConnection();
        } while (true);
    }
}
