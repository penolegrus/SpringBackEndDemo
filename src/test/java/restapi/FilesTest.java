package restapi;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static io.restassured.RestAssured.given;

public class FilesTest extends BaseApiTest {

    @Test
    public void downloadFileTest() {
        File baseFile = new File("src/main/resources/files/threadqa.jpeg");
        byte[] downloaded = restService.get("/api/files/download").asResponse().asByteArray();
        Assertions.assertEquals(baseFile.length(), downloaded.length);
    }

    @Test
    public void uploadFileTest() throws IOException {
        File baseFile = new File("src/main/resources/files/threadqa.jpeg");
        given().contentType(ContentType.MULTIPART)
                .multiPart("file", "myFile", Files.readAllBytes(baseFile.toPath()))
                .body(baseFile)
                .post("http://85.192.34.140:8080/api/files/upload");

        byte[] downloaded = restService.get("/api/files/downloadLastUploaded").asResponse().asByteArray();
        Assertions.assertTrue(downloaded.length!=0);
        Assertions.assertEquals(downloaded.length, baseFile.length());
    }

    @Test
    public void downloadLastFileIfNotExist(){
        File rm = new File("src/main/resources/files/uploadedFile");
        rm.delete();
        restService.get("/api/files/downloadLastUploaded").hasMessage("file not exist");
    }
}
