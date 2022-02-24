package com.insurance.customer.agent.file.helper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CsvFileHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CsvFileHandler.class);
	
	private final String backupCSVFile = "/Backup";
	private final String errorfolder = "/Error";

	private String exceptionMessage;
	public void processFile(String fromFile, String toFile) throws IOException {
		
		List<Customer> customerList = parse(fromFile);
		List<String> errorList;
		if(!customerList.isEmpty()) {
		errorList = validateCustomers(customerList);
		if(!errorList.isEmpty()) {
			LOGGER.info("Validation errors noticed while processing the file");
			copyFiletoError(toFile, errorList);
			
		}
		else {
			copyFiletoBackup(fromFile, toFile+backupCSVFile);
		}
		}
		else {
			LOGGER.error("Exception occured while parsing the file or blank file recieved ");
			errorList = new ArrayList<String>();
			errorList.add(exceptionMessage);
		}
		
	}
		
	private void copyFiletoError( String toFile, List<String> errorList) throws IOException {
		String path = toFile+errorfolder;
		File dir = new File(path);
		dir.mkdirs();
		String time = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
		String fileName = toFile+errorfolder+"/Error_"+time+".txt";
		File file = new File(fileName);
		FileWriter writer = new FileWriter(file.getAbsoluteFile()); 
		for(String error: errorList) {
			writer.write(error + System.lineSeparator());
		} 
			  writer.close();
	}



	private void copyFiletoBackup(String source, String target) {
		Path src = Paths.get(source);
		File destFile = new File(target);
		destFile.mkdirs();
		String time = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
		String fileName = "/Customer_"+time+".csv";
		File backupFile = new File(target+fileName);
		Path destPath = backupFile.toPath();
		try {
			if (Files.exists(src)) {
				Files.copy(src, destPath, StandardCopyOption.REPLACE_EXISTING);
				LOGGER.info("File copied to target folder");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


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
			return customerList;
		} 
		catch(Exception e) {
			e.printStackTrace();
			exceptionMessage="Excpetion occured while parsing the file.May be invalid format/content. Please upload valid csv file with valid customer records";
		}
		return new ArrayList<Customer>();
	}
	
	
	public List<String> validateCustomers(List<Customer> customerList) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		List<String> errorList = new ArrayList<String>();
		for (Customer customer: customerList) {
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
