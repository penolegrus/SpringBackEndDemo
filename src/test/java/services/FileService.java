package services;

import assections.AssertableResponse;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static io.restassured.RestAssured.given;

@Slf4j
public class FileService extends WebService {
    public FileService() {
        super("/files/");
    }

    public AssertableResponse getBaseImage() {
        return new AssertableResponse(requestSpec.get("download").then());
    }

    public AssertableResponse getLastUploadedFile(){
        return new AssertableResponse(requestSpec.get("downloadLastUploaded").then());
    }

    public AssertableResponse uploadFile(File file) throws IOException {
        return new AssertableResponse(requestSpec.contentType(ContentType.MULTIPART)
                .multiPart("file", "myFile", Files.readAllBytes(file.toPath()))
                .body(file).post("upload").then());
    }
}
