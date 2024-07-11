package pl.iodkovskaya.travel_allowance_calculation.controller.travel;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.iodkovskaya.travel_allowance_calculation.logic.pdfDocument.PdfDocumentService;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.dto.TravelRequestDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.dto.TravelResponseDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.service.TravelService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/travels")
public class TravelController {
    private final TravelService travelService;
    private final PdfDocumentService pdfDocumentPrinter;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TravelResponseDto calculateTravelExpenses(@RequestBody @Valid TravelRequestDto requestDto) {
        return travelService.calculateTravelExpenses(requestDto);
    }
}
