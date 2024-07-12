package pl.iodkovskaya.travel_allowance_calculation.logic.pdfDocument;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.stereotype.Service;
import pl.iodkovskaya.travel_allowance_calculation.logic.diet.model.entity.DietEntity;
import pl.iodkovskaya.travel_allowance_calculation.logic.overnightStay.model.entity.OvernightStayEntity;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.exception.TravelException;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.entity.TravelEntity;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.repository.TravelRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import static java.util.Map.entry;

@Service
public class PdfDocumentService {
    private final TravelRepository travelRepository;

    public PdfDocumentService(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
    }

    public void generatePdfDocument(Long id) throws IOException {
        TravelEntity travelEntity = travelRepository.findById(id).orElseThrow(() -> new TravelException("Travel not found"));
        DietEntity dietEntity = travelEntity.getDietEntity();
        OvernightStayEntity overnightStayEntity = travelEntity.getOvernightStayEntity();

        Map<String, String> replacements = Map.ofEntries(
                entry("fullName", travelEntity.getUserEntity().getFirstName() + " " + travelEntity.getUserEntity().getSecondName()),
                entry("position", travelEntity.getUserEntity().getPosition()),
                entry("fromCity", travelEntity.getFromCity()),
                entry("toCity", travelEntity.getToCity()),
                entry("startDate", travelEntity.getStartDate().toString()),
                entry("startTime", travelEntity.getStartTime().toString()),
                entry("endDate", travelEntity.getEndDate().toString()),
                entry("endTime", travelEntity.getEndTime().toString()),
                entry("countBreakfast", String.valueOf(dietEntity.getNumberOfBreakfasts())),
                entry("countLunch", String.valueOf(dietEntity.getNumberOfLunches())),
                entry("countDinner", String.valueOf(dietEntity.getNumberOfDinners())),
                entry("totalAmount", String.valueOf(travelEntity.getTotalAmount())),
                entry("dietAmount", String.valueOf(dietEntity.getDietAmount())),
                entry("foodAmount", String.valueOf(dietEntity.getFoodAmount())),
                entry("overnightStayWithInvoice", String.valueOf(overnightStayEntity.getAmountOfTotalOvernightsStayWithInvoice())),
                entry("overnightStayWithoutInvoice", String.valueOf(overnightStayEntity.getAmountOfTotalOvernightsStayWithoutInvoice())),
                entry("advancePayment", String.valueOf(travelEntity.getAdvancePayment()))
        );

        String templatePath = "src/main/resources/print/template.pdf";
        String outputPath = "src/main/resources/print/changed_template.pdf";

        fillTemplate(templatePath, outputPath, replacements);
    }

    private void fillTemplate(String templatePath, String outputPath, Map<String, String> data) throws IOException {
        try (PDDocument document = PDDocument.load(new FileInputStream(templatePath))) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();

            if (acroForm != null) {
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    PDField field = acroForm.getField(entry.getKey());
                    if (field != null) {
                        field.setValue(entry.getValue());
                    }
                }
                acroForm.flatten();
            }
            document.save(outputPath);
        }
    }
}
