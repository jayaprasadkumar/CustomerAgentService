package com.insurance.customer.agent.service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.insurance.customer.agent.file.helper.CsvFileHandler;
import com.insurance.customer.agent.file.model.Customer;

@Service
public class WatcherService implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(WatcherService.class);

	@Value("${source.dir.path}")
	private String dirPath;
	@Value("${file.target.backup.path}")
	private String toFile;
	@Value("${file.target.error.path}")
	private String errorFile;
	@Value("${source.file.name}")
	private String sourceFileName;

	private final WatchService watchService;

	public WatcherService() throws IOException {
		this.watchService = FileSystems.getDefault().newWatchService();
	}

	@Override
	public void run() {

		try {
			Path path = Paths.get(dirPath);
			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY,
					StandardWatchEventKinds.ENTRY_DELETE);
			CsvFileHandler handler = new CsvFileHandler();
			WatchKey key;
			while ((key = watchService.take()) != null) {
				for (WatchEvent<?> event : key.pollEvents()) {
					LOGGER.info("Event kind:" + event.kind() + ". File affected: " + event.context());
					if (event.kind().equals(StandardWatchEventKinds.ENTRY_CREATE)
							&& sourceFileName.contains(event.context().toString())) {
						LOGGER.info("Processing the file");
						handler.processFile(dirPath + sourceFileName, toFile);
					}
				}
				key.reset();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
