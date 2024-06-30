package pe.edu.pucp.tesisrest.worker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pe.edu.pucp.tesisrest.common.model.logentry.LogEntry;
import pe.edu.pucp.tesisrest.worker.repository.LogEntryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogEntryServiceImpl {

    private final LogEntryRepository logEntryRepository;

    public List<LogEntry> getUnprocessedEntries() {
        return logEntryRepository.findByProcessedFalse();
    }

    public void markAsProcessed(LogEntry logEntry) {
        logEntry.setProcessed(true);
        logEntryRepository.save(logEntry);
    }
}
