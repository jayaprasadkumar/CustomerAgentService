package com.insurance.customer.agent.file.helper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Component;

@Component
public class CsvFileParser {
	
	
	
	
	public boolean processFile() 
			   {
		boolean fileCopied =true;
		String fromFile = "/Users/jayaprasadkumar/Documents/Doc/Source/Defaulters_Date.csv";
        String toFile = "/Users/jayaprasadkumar/Documents/Doc/Target/Defaulters_Date.csv";
        Path source = Paths.get(fromFile);
        Path target = Paths.get(toFile);
        try {
        	Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        	System.out.println("File copied to target folder");
        } catch (Exception e) {
        	  fileCopied = false;
  	        e.printStackTrace();
        }
        
        return fileCopied;
    }

}
