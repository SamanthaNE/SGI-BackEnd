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
                Integer status = processNewRecord(logEntry);
                if(status == 1){
                    logEntryService.markAsProcessed(logEntry);
                }
            }
            Thread.sleep(600000); // Wait 10 minute before checking again
        }
    }

    private Integer processNewRecord(LogEntry logEntry) {
        Integer status;

        System.out.println("New record in table " + logEntry.getTableName() +
                " with ID " + logEntry.getRecordId() +
                " and action " + logEntry.getAction());

        status = inferenceEngineService.assignScoreToScientificProduction(logEntry);
        if(status == 1){
            inferenceEngineService.updateScoreToResearchGroup(logEntry);
        }

        return status;
    }
}
