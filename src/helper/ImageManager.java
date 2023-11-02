package helper;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class ImageManager {

    public static Image convertBlobImage(Blob blob) {

        try {
            byte[] blobData = blob.getBytes(1, (int) blob.length());

            ByteArrayInputStream inputStream = new ByteArrayInputStream(blobData);

            return new Image(inputStream);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
