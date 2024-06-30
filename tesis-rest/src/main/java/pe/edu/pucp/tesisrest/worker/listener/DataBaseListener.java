package pe.edu.pucp.tesisrest.worker.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pe.edu.pucp.tesisrest.common.model.logentry.LogEntry;
import pe.edu.pucp.tesisrest.worker.service.impl.InferenceEngineServiceImpl;
import pe.edu.pucp.tesisrest.worker.service.impl.LogEntryServiceImpl;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataBaseListener implements CommandLineRunner {
    private final LogEntryServiceImpl logEntryService;
    private final InferenceEngineServiceImpl inferenceEngineService;

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            List<LogEntry> unprocessedEntries = logEntryService.getUnprocessedEntries();
            for (LogEntry logEntry : unprocessedEntries) {
                processNewRecord(logEntry);
                logEntryService.markAsProcessed(logEntry);
            }
            Thread.sleep(60000); // Wait 1 minute before checking again
        }
    }

    private void processNewRecord(LogEntry logEntry) {

        System.out.println("New record in table " + logEntry.getTableName() +
                " with ID " + logEntry.getRecordId() +
                " and action " + logEntry.getAction());

        inferenceEngineService.assignScoreToScientificProduction(logEntry);
    }
}
