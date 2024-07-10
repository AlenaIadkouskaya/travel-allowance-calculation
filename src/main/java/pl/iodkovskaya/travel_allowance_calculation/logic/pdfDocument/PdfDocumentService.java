package pl.iodkovskaya.travel_allowance_calculation.logic.pdfDocument;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.stereotype.Service;
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
        TravelEntity travel = travelRepository.findById(id).orElseThrow(() -> new TravelException("Travel not found"));

        Map<String, String> replacements = Map.ofEntries(
                entry("fullName", travel.getUserEntity().getFirstName() + " " + travel.getUserEntity().getSecondName()),
                entry("position", travel.getUserEntity().getPosition()),
                entry("fromCity", travel.getFromCity()),
                entry("toCity", travel.getToCity()),
                entry("startDate", travel.getStartDate().toString()),
                entry("startTime", travel.getStartTime().toString()),
                entry("endDate", travel.getEndDate().toString()),
                entry("endTime", travel.getEndTime().toString()),
                entry("advancePayment", String.valueOf(travel.getAdvancePayment()))
        );

        String templatePath = "src/main/resources/templates/files/template.pdf";
        String outputPath = "src/main/resources/templates/files/changed_template.pdf";

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
