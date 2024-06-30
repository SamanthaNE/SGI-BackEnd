package pe.edu.pucp.tesisrest.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.tesisrest.common.model.logentry.LogEntry;

import java.util.List;

public interface LogEntryRepository extends JpaRepository<LogEntry, Integer> {
    List<LogEntry> findByProcessedFalse();
}
