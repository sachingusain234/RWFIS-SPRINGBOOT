package com.RAMS.RWFIS.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api/csv")
public class UploadController {

    private static final Set<String> ALLOWED_FEATURE_CONDITIONS =
            new HashSet<>(Arrays.asList("DI", "F", "G", "NA", "P"));

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSVFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a file.");
        }

        String contentType = file.getContentType();
        if (!(contentType != null && (contentType.equals("text/csv") || contentType.equals("application/vnd.ms-excel")))) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body("Only CSV files are allowed (MIME type).");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body("Only .csv files are allowed (file extension).");
        }

        // Step 1: Validate "Feature Condition" column directly from the stream
        List<String> invalidRows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                return ResponseEntity.badRequest().body("CSV file is empty.");
            }
            String[] headers = headerLine.split(",");
            int fcIdx = Arrays.asList(headers).indexOf("Feature Condition");
            if (fcIdx == -1) {
                return ResponseEntity.badRequest().body("\"Feature Condition\" column not found.");
            }

            String line;
            int rowNum = 2;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length <= fcIdx) {
                    invalidRows.add("Row " + rowNum + ": missing Feature Condition field.");
                } else {
                    String fcValue = fields[fcIdx].trim();
                    if (!ALLOWED_FEATURE_CONDITIONS.contains(fcValue)) {
                        invalidRows.add("Row " + rowNum + ": invalid Feature Condition value '" + fcValue + "'.");
                    }
                }
                rowNum++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read the uploaded file.");
        }

        if (!invalidRows.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Invalid Feature Condition values found:\n" + String.join("\n", invalidRows));
        }

        // Step 2: Save the validated file to disk
        String uploadDir = "C:\\uploads";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        File savedFile = new File(dir, fileName);

        try {
            file.transferTo(savedFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save the file.");
        }

        return ResponseEntity.ok("CSV file uploaded, validated, and saved successfully.");
    }
}
