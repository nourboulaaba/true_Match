package tn.esprit.pitest11.controllerConge;
import com.calendarfx.model.*;
import com.calendarfx.view.CalendarView;
import tn.esprit.pitest11.Entities.Conges;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import tn.esprit.pitest11.service.CongeService;
import tn.esprit.pitest11.utils.CongeRequest;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CalendrierConge {

    @FXML
    private AnchorPane calendrierPane;

    @FXML
    private TextField employeIdField;

    @FXML
    private Label soldeCongesLabel;

    private final CongeService congeService = new CongeService();

    public void initialize() throws SQLException {
        afficherCalendrier();
    }

    private void afficherCalendrier() throws SQLException {
        CalendarView calendarView = new CalendarView();
        Calendar congesCalendar = new Calendar("Congés");
        congesCalendar.setStyle(Calendar.Style.STYLE2);

        // Vérifie si un ID employé est fourni
        List<Conges> congesList;
        if (!employeIdField.getText().trim().isEmpty()) {
            try {
                int employeId = Integer.parseInt(employeIdField.getText().trim());
                congesList = CongeRequest.getCongesByEmploye(employeId);
            } catch (NumberFormatException e) {
                soldeCongesLabel.setText("ID employé invalide !");
                return;
            }
        } else {
            congesList = congeService.getAllConges();
        }

        for (Conges conge : congesList) {
            congesCalendar.addEntry(convertToCalendarEntry(conge));
        }

        CalendarSource source = new CalendarSource("Sources de Calendrier");
        source.getCalendars().add(congesCalendar);
        calendarView.getCalendarSources().add(source);

        calendrierPane.getChildren().add(calendarView);
    }

    private Entry<String> convertToCalendarEntry(Conges conge) {
        Entry<String> entry = new Entry<>("Congé de l'employé " + conge.getIdEmploye());
        LocalDate startDate = conge.getDateDebut().toLocalDate();
        LocalDate endDate = conge.getDateFin().toLocalDate();
        entry.setInterval(startDate, endDate);
        return entry;
    }

    @FXML
    private void afficherSoldeConges() {
        String employeIdText = employeIdField.getText().trim();

        if (employeIdText.isEmpty()) {
            soldeCongesLabel.setText("Veuillez saisir un ID employé !");
            return;
        }

        try {
            int employeId = Integer.parseInt(employeIdText);

            int solde = congeService.calculerSoldeConges(employeId);

            if (solde == -1) {
                soldeCongesLabel.setText("Cet employé n'existe pas !");
            } else {
                soldeCongesLabel.setText("Solde restant: " + solde + " jours");
            }
        } catch (NumberFormatException e) {
            soldeCongesLabel.setText("ID employé invalide !");
        }
    }


}

