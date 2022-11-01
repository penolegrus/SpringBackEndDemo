package tests;

import core.TestBase;
import extensions.CustomLogFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;


public class FilesTest extends TestBase {

    @Test
    public void downloadFileTest() {
        File baseFile = new File("src/main/resources/files/threadqa.jpeg");
        byte[] downloaded = fileService.getBaseImage().asByteArray();
        Assertions.assertEquals(baseFile.length(), downloaded.length);
    }

    @Test
    public void uploadFileTest() throws IOException {
        File baseFile = new File("src/main/resources/files/threadqa.jpeg");
        fileService.uploadFile(baseFile);

        byte[] downloaded = fileService.getLastUploadedFile().asByteArray();
        Assertions.assertTrue(downloaded.length != 0);
        Assertions.assertEquals(downloaded.length, baseFile.length());
    }
}
