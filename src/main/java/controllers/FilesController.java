package controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import models.InfoMessage;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class FilesController {

    @Operation(description = "Скачивает картинку с сервера в формате JPEG")
    @ApiResponse(
            responseCode = "200",
            description = "Карнитка скачалась",
            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
    @GetMapping(path = "/api/files/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadImage() throws IOException {
        File jpegOnServer = new File("/home/ubuntu/threadqa.jpeg");
       // File jpeg = new File("src/main/resources/files/threadqa.jpeg");

        InputStreamResource resource = new InputStreamResource(Files.newInputStream(jpegOnServer.toPath()));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=threadqa.jpeg");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(jpegOnServer.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @Operation(description = "Скачивает последний загруженный файл")
    @ApiResponse(
            responseCode = "200",
            description = "Файл скачался")
    @GetMapping(path = "/api/files/downloadLastUploaded", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> downloadLastUploaded() throws IOException {
     //   File jpeg = new File("src/main/resources/files/uploadedFile");
        File jpegOnServer = new File("/home/ubuntu/uploadedFile");


        if (!jpegOnServer.exists()) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "file not exist"));
        }

        InputStreamResource resource = new InputStreamResource(Files.newInputStream(jpegOnServer.toPath()));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=uploadedFile");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(jpegOnServer.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @Operation(description = "Загружает файл на сервер")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл загружен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InfoMessage.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Файл слишком большой. Максимальный размер 3мб",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InfoMessage.class))
            )})
    @PostMapping(path = "/api/files/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<InfoMessage> uploadFile(@Parameter(description = "File для загрузки с Content Type Multipart")
                                                      @RequestParam("file") MultipartFile uploadFile) throws IOException {
        String fileName = "uploadedFile";

        byte[] bytes = uploadFile.getBytes();
   //     BufferedOutputStream stream = new BufferedOutputStream(Files.newOutputStream(Paths.get("src/main/resources/files/" + fileName)));
        BufferedOutputStream stream = new BufferedOutputStream(Files.newOutputStream(Paths.get("/home/ubuntu/" + fileName)));
        stream.write(bytes);
        stream.close();

        if (!new File("/home/ubuntu/" + fileName).exists()) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "cant upload file"));
        }
        return ResponseEntity.status(200).body(new InfoMessage("success", "file uploaded to server"));
    }
}

