package com.insurance.customer.agent.file.helper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.insurance.customer.agent.file.model.Customer;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CsvFileHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CsvFileHandler.class);
	
	private final String backupCSVFile = "/Backup";
	private final String errorfolder = "/Error";
	private final String successFileName = "/Customer_backup.csv";
	public void processFile(String fromFile, String toFile) throws IOException {
		
		List<Customer> customerList = parse(fromFile);
		List<String> errorList = validateCustomers(customerList);
		if(!errorList.isEmpty()) {
			LOGGER.info("Validation errors noticed while processing the file");
			copyFiletoError(toFile, errorList);
			
		}
		else {
			copyFiletoBackup(fromFile, toFile+backupCSVFile);
			
		}
		
	}
		
	private void copyFiletoError( String toFile, List<String> errorList) throws IOException {
		String path = toFile+errorfolder;
		File dir = new File(path);
		dir.mkdirs();
		File file = new File(toFile+errorfolder+"/Error.txt");
		FileWriter writer = new FileWriter(file.getAbsoluteFile()); 
		for(String error: errorList) {
			writer.write(error + System.lineSeparator());
		} 
			  writer.close();
	}



	private boolean copyFiletoBackup(String source, String target) {
		boolean fileCopied = true;
		Path src = Paths.get(source);
		File destFile = new File(target);
		destFile.mkdirs();
		File backupFile = new File(target+successFileName);
		Path destPath = backupFile.toPath();
		try {
			if (Files.exists(src)) {
				Files.copy(src, destPath, StandardCopyOption.REPLACE_EXISTING);
				LOGGER.info("File copied to target folder");
			}
		} catch (Exception e) {
			fileCopied = false;
			e.printStackTrace();
		}

		return fileCopied;
	}

	/*
	 * public List<String> getData(String filePath, String titleToSearchFor) throws
	 * IOException { LOGGER.info("parse data from csv file "); Path path =
	 * Paths.get(filePath); if (Files.exists(path)) { List<String> lines =
	 * Files.readAllLines(path, Charset.defaultCharset()); List<String> columns =
	 * Arrays.asList(lines.get(0).split(",")); int titleIndex =
	 * columns.indexOf(titleToSearchFor); List<String> values =
	 * lines.stream().skip(1).map(line -> Arrays.asList(line.split(","))) .map(list
	 * -> list.get(titleIndex)).filter(Objects::nonNull).filter(s ->
	 * s.trim().length() > 0) .collect(Collectors.toList());
	 * 
	 * return values;
	 * 
	 * }
	 * 
	 * return new ArrayList<>();
	 * 
	 * }
	 */

	public List<Customer> parse(String filePath) {
		LOGGER.info("Parsing  the csv file from path"+filePath); 
		try {
			CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).withSkipLines(1). 
					build();
			List<Customer> customerList = reader.readAll().stream().map(data -> {
				Customer customer = new Customer();
				customer.setEmpID(data[0]);
				customer.setPrefix(data[1]);
				customer.setFirstName(data[2]);
				customer.setMiddleInitial(data[3]);
				customer.setLastName(data[4]);
				customer.setGender(data[5]);
				customer.seteMail(data[6]);
				customer.setDateofBirth(data[7]);
				customer.setDateofJoining(data[8]);
				customer.setSsn(data[9]);
				return customer;
			}).collect(Collectors.toList());
			//customerList.forEach(System.out::println);
			return customerList;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CsvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<Customer>();
	}
	
	
	public List<String> validateCustomers(List<Customer> customerList) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		List<String> errorList = new ArrayList<String>();
		for (Customer customer: customerList) {
		//LOGGER.info("Validating the customer"+customer); 
		Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
		StringBuilder errorMessage = new StringBuilder("Error occured while validating the record of ")
				.append(customer.getEmpID())
				.append(customer.getFirstName())
				.append(" with message : ");
		for (ConstraintViolation<Customer> violation : violations) {
			errorMessage.append(violation.getMessage());
			errorList.add(errorMessage.toString());
		 LOGGER.info(errorMessage.toString()); 
		}
		}
		return errorList;
	}
		
		
	
}
