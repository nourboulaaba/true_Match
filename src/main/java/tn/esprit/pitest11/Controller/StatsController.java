

package tn.esprit.pitest11.Controller;


import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import tn.esprit.pitest11.Services.EntretienService;
import tn.esprit.pitest11.Services.RecrutementService;
import tn.esprit.pitest11.Services.UserService;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import tn.esprit.pitest11.Utils.GeminiAPI;

import javax.imageio.ImageIO;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class StatsController {

    // You may still instantiate these services for later use,
    // but for now we're using sample data.
    UserService us = new UserService();
    EntretienService es = new EntretienService();
    RecrutementService rs = new RecrutementService();

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private ScatterChart<Number, Number> scatter;

    @FXML
    private PieChart pieChart;

    @FXML
    private Button generate;

    private String barData;
    private String lineData;
    private String scatterData;
    private String pieData;


    /**
     * The initialize method is called automatically after the FXML is loaded.
     * It initializes the charts with sample data.
     */
    @FXML
    public void initialize() {
        System.out.println(es.readAll());

        populateCharts();
    }

    /**
     * Populates the charts with sample data.
     * - BarChart: Each "Recrutement" is represented with a label and a number of entretiens.
     * - LineChart: Dummy trend data.
     * - ScatterChart: Dummy numeric data.
     * - PieChart: Dummy distribution of entretiens by location.
     */
    public void populateCharts() {
        // ---- BarChart: Number of recruitments per job offer ----
        XYChart.Series<String, Number> barSeries = new XYChart.Series<>();
        Map<String, Long> recruitmentCount = rs.readAll().stream()  // Assuming 'rs' is RecruitmentService
                .collect(Collectors.groupingBy(r -> r.getOffre().getTitre(), Collectors.counting()));

        recruitmentCount.forEach((title, count) -> barSeries.getData().add(new XYChart.Data<>(title, count)));
        barChart.getData().clear();
        barChart.getData().add(barSeries);
        barData = recruitmentCount.toString();


        // ---- LineChart: Trend of recruitments over months ----
        XYChart.Series<String, Number> lineSeries = new XYChart.Series<>();
        Map<String, Long> recruitmentTrends = rs.readAll().stream()
                .collect(Collectors.groupingBy(r -> r.getDateDebut().getMonth().toString(), Collectors.counting()));

        recruitmentTrends.forEach((month, count) -> lineSeries.getData().add(new XYChart.Data<>(month, count)));
        lineChart.getData().clear();
        lineChart.getData().add(lineSeries);
        lineData = recruitmentTrends.toString();

        // ---- ScatterChart: Number of Interviews vs Recruitment ID ----
        XYChart.Series<Number, Number> scatterSeries = new XYChart.Series<>();
        rs.readAll().forEach(r -> scatterSeries.getData().add(new XYChart.Data<>(r.getId(), r.getNbEntretien())));
        scatter.getData().clear();
        scatter.getData().add(scatterSeries);
        scatterData = scatterSeries.getData().toString();
// ---- PieChart: Percentage of recruitments by job offer ----
        pieChart.getData().clear();
        Map<String, Long> recruitmentByOffer = rs.readAll().stream()
                .collect(Collectors.groupingBy(r -> r.getOffre().getTitre(), Collectors.counting()));

        recruitmentByOffer.forEach((offer, count) -> pieChart.getData().add(new PieChart.Data(offer, count)));
        pieData = recruitmentByOffer.toString();}


        /**
         * Generates a PDF report containing snapshots of each chart along with a decision analysis text above each.
         * The decision analysis is fetched via the OpenAI API.
         */
        @FXML
        void generatePDF(ActionEvent event) {
            String saveDirectory = "C:\\Users\\FARAH\\crud\\pitest11\\src\\main\\resources\\uploadPdf"; // Change this to your desired directory
            String filePath = saveDirectory + "/statistics.pdf";

            try {
                // Ensure the directory exists
                File dir = new File(saveDirectory);
                if (!dir.exists()) {
                    dir.mkdirs(); // Create the directory if it doesn't exist
                }

                // Capture chart images
                String barPath = saveChartAsImage(barChart, saveDirectory + "/bar_chart.png");
                String linePath = saveChartAsImage(lineChart, saveDirectory + "/line_chart.png");
                String piePath = saveChartAsImage(pieChart, saveDirectory + "/pie_chart.png");
                String scatterPath = saveChartAsImage(scatter, saveDirectory + "/scatter_chart.png");

                // Get AI recommendations
                String barRecommendations = getAIRecommendations("bar chart", barData);
                String lineRecommendations = getAIRecommendations("line chart", lineData);
                String pieRecommendations = getAIRecommendations("pie chart", pieData);
                String scatterRecommendations = getAIRecommendations("scatter chart", scatterData);

                // Create PDF
                PdfWriter writer = new PdfWriter(filePath);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                document.add(new Paragraph("Statistics Report").setBold().setFontSize(16));

                addChartToPDF(document, "Bar Chart: ", barPath, barRecommendations);
                addChartToPDF(document, "Line Chart: ", linePath, lineRecommendations);
                addChartToPDF(document, "Pie Chart:", piePath, pieRecommendations);
                addChartToPDF(document, "Scatter Chart: ", scatterPath, scatterRecommendations);

                document.close();

                // Open the generated PDF automatically
                Desktop.getDesktop().open(new File(filePath));

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to generate PDF.");
            }
        }

    private String saveChartAsImage(Chart chart, String fileName) throws IOException {
        WritableImage image = chart.snapshot(new SnapshotParameters(), null);


        File file = new File( fileName);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        return fileName;
    }

    private void addChartToPDF(Document document, String title, String imagePath, String recommendations) throws IOException {
        document.add(new Paragraph(title).setBold().setFontSize(14));

        ImageData imageData = ImageDataFactory.create(imagePath);
        Image chartImage = new Image(imageData);
        chartImage.setAutoScale(true);
        document.add(chartImage);

        document.add(new Paragraph("Recommendations:").setBold().setFontSize(12));
        document.add(new Paragraph(recommendations));

        document.add(new Paragraph("\n"));
    }

    private String getAIRecommendations(String chartType, String data) {
        String prompt = "You are an AI expert in business analysis and recruitment strategies. Based on the " +
                chartType + " representing statistical data for recruitment processes, provide two strategic recommendations. " +
                "Your analysis should focus on improving recruitment efficiency, increasing the number of successful hires, " +
                "optimizing the recruitment process, and ensuring a better alignment between job offers and candidates.\n\n" +
                "Data: " + data + "\n\n" +
                "Return exactly two actionable bullet points as recommendations, based on the patterns and trends you observe from the data."+
                "RETURN ONLY TWO BULLET POINTS ! ";

        try {
            JsonObject response = GeminiAPI.getGeminiResponse(prompt);
            return extractBulletPoints(response);
        } catch (IOException e) {
            e.printStackTrace();
            return "No recommendations available.";
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showAlertSuccess(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String extractBulletPoints(JsonObject jsonResponse) {
        if (jsonResponse.has("candidates") &&
                jsonResponse.getAsJsonArray("candidates").size() > 0) {
            String text = jsonResponse.getAsJsonArray("candidates")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("content")
                    .getAsJsonArray("parts")
                    .get(0).getAsJsonObject()
                    .get("text").getAsString();
            return text.replace("\n", "\n• "); // Format bullet points
        }
        return "• No recommendations available.";
    }


}
