package com.idcardgenerator;

import java.io.File;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.idcardgenerator.services.UserService;

//@SpringBootApplication
public class IdCardGeneratorApplication {

	public static void main(String[] args) {
//		SpringApplication.run(IdCardGeneratorApplication.class, args);

		//just for personal checking of the main() working
		System.out.println("Hello Maven!!");

		//Getting the object of the UserService class which contains all the business logic
		UserService userService = new UserService();

		//field of csv file path
		String csvFilePath = null;

		//getting the csv file path using file object
		try {
			File file = new File("src/main/resources/csv/Users.csv");
			csvFilePath = file.getPath();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		/*
		* calling getDataFromCsv() to read all the records situated into the .csv file
		* And saving it into a List of String Array type.
		* meaning one list element would be containing an array of String type data
		* i.e. - Name, Position, Phone, Email and Photo values.
		*/
		List<String[]> records = userService.getDataFromCsv(csvFilePath);


		//calling the record printer method()
		userService.showData(records);

		//calling the createIdCard() to create the pdf id card using the record param
		userService.createIdCard(records);


	}

}
