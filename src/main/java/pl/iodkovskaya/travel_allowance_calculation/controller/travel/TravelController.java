package pl.iodkovskaya.travel_allowance_calculation.controller.travel;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.iodkovskaya.travel_allowance_calculation.logic.pdfDocument.PdfDocumentService;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.dto.TravelRequestDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.dto.TravelResponseDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.service.TravelService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/travels")
public class TravelController {
    private final TravelService travelService;
    private final PdfDocumentService pdfDocumentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TravelResponseDto calculateTravelExpenses(@RequestBody @Valid TravelRequestDto requestDto) {
        return travelService.calculateTravelExpenses(requestDto);
    }
//    @PostMapping("/print/{id}")
//    public ResponseEntity<String> generatePdf(@PathVariable Long id) {
//        try {
//            pdfDocumentService.generatePdfDocument(id);
//            return ResponseEntity.ok("PDF document generated successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating PDF document: " + e.getMessage());
//        }
//    }

    @PostMapping("/print/{id}")
    public ResponseEntity<Void> print2(@PathVariable("id") Long id) {
        try {
            pdfDocumentService.generatePdfDocument(id);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.status(302)
                .header("Location", "/api/v1/travels/print/changed_template.pdf")
                .build();
    }

    @GetMapping("/print/changed_template.pdf")
    public ResponseEntity<InputStreamResource> getChangedTemplate() {
        try {
            File file = new File("src/main/resources/print/changed_template.pdf");
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=changed_template.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
