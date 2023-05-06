package com.idcardgenerator.services;

import com.idcardgenerator.entities.UserEntity;
import com.itextpdf.text.*;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.*;
import java.nio.file.Files;
	import java.nio.file.Path;
	import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class UserService {

	// 1. method to import data from the CSV file
	    public List<String[]> getDataFromCsv(String csvFilePath){
	        String line;
	        String delimiter = ",";
	        List<String[]> records = new ArrayList<>();
	        try(BufferedReader br = new BufferedReader(new FileReader(csvFilePath))){
				if(csvFilePath.isEmpty()) {
					throw new FileNotFoundException("No Such File Exists : file path provided might be wrong..");
				}
	            while((line = br.readLine())!= null) {
	                StringTokenizer tokenizer = new StringTokenizer(line,delimiter);
	            //creating fields var with the size of number of tokens found
					String[] fields = new String[tokenizer.countTokens()];
	                int i = 0;
	                while(tokenizer.hasMoreTokens()){
	                    String token = tokenizer.nextToken();
						//checking whether the coming token is a photo url or not
	                    if(token.endsWith(".jpg") || token.endsWith(".png")){
	                        //reading the photo data as binary data
	                        byte[] bytes = readPhoto(token);
	                        String base64Photo = Base64.getEncoder().encodeToString(bytes);
	                        fields[i++] = base64Photo;
	                    } else {
	                        fields[i++] = token;
	                    }
	                }
	                records.add(fields);
	            }
	        }catch (FileNotFoundException fe){
				System.out.println(fe.getMessage());
			}catch (ArrayIndexOutOfBoundsException ae){
				ae.printStackTrace();
			} catch(IOException e) {
	            e.printStackTrace();
	        }
	        return records;
	    }

	// method to read photo from csv file into the byte array
	    private static byte[] readPhoto(String photoFile) throws IOException{
	        Path path = Paths.get(photoFile);
	        return Files.readAllBytes(path) ;
	    }

	// 2. method to print file data just imported above
	/*
	* Method to print all the records retrieved via getDataFromCsv()
	* @param records contains list of records
	* @return nothing, just prints the record one by one
	*/
	    public void showData(List<String[]> records) {
	        for(String[] record : records){
	            for(String field : record){
	                System.out.println(field);
	            }
	            System.out.println();
	        }
	    }


	// 3. method to populate our custom class fields with the fields parsed from the csv file
	    public void createIdCard(List<String[]> records){
	        UserEntity userEntity = new UserEntity();

		//parsing record data into the UserEntity type object
			for(String[] record : records){
	            int id = Integer.parseInt(record[0]);
	            String name = record[1];
	            String position = record[2];
	            String phone = record[3];
	            String email = record[4];
	            String base64Photo = record[5];
	            byte[] photo = Base64.getDecoder().decode(base64Photo);

	        //setting the class fields with the data from the record
	            userEntity.setId(id);
	            userEntity.setName(name);
	            userEntity.setPosition(position);
	            userEntity.setPhone(phone);
                userEntity.setEmail(email);
                userEntity.setPhoto(photo);

			//path of output folder to save generated id card
				String pdfFilePath = "C:\\Users\\Amar\\Documents\\Output\\";

			//creating the name of the output pdf file using the name of the user
				String pdfFileName = pdfFilePath+userEntity.getName()+"_"+System.currentTimeMillis()+".pdf";

			//sending retrieved record to create the PDF file
				generatePdfIdCard(userEntity,pdfFileName);
	        }
	    }

	//method to create the pdf ID Card using the retrieved user properties from the csv file
	/*
	* @param : userEntity object of userEntity class
	* @param : pdfFile is the path of the output pdf file
	* code will make a new pdf file if no pdf found in the output folder
	*/
		public void generatePdfIdCard(UserEntity userEntity, String pdfFile) {
			Document document = null;
			try {
				File file = new File(pdfFile);
				boolean isCreated = false;

				//checking whether the file exists in the path,
				//if not then creating a new one
				if(!file.exists()){
					isCreated = file.createNewFile();
				}

				//throwing the file not found exception
				if(!isCreated){
					throw new FileNotFoundException("PDF File could not be created.");
				}
				//creating the size of the output pdf file document
				document = new Document(PageSize.A8, 2, 2,10,2);

				//editing the pdf file of above size in the provided path directory
				PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

				//opening the document for editing...
				document.open();

				//Add Name : writing the name of the user on the Id Card
				Paragraph namePara = new Paragraph(userEntity.getName(),
						new Font(Font.FontFamily.HELVETICA,18,Font.BOLD));
				namePara.setAlignment(Element.ALIGN_CENTER);
				document.add(namePara);

				//creating Line Separator : a black straight line
				LineSeparator separator = new LineSeparator();
				separator.setLineColor(BaseColor.BLACK);
				separator.setLineWidth(0.5f);
				separator.setPercentage(80f);
				document.add(separator);

				//Add Position : writing the position of the user on the Id Card
				Paragraph posPara = new Paragraph(userEntity.getPosition(),
						new Font(Font.FontFamily.HELVETICA,12,Font.BOLD));
				posPara.setAlignment(Element.ALIGN_CENTER);
				document.add(posPara);
//				document.add(separator);		//line Separator

				//Add Phone : writing the phone number of the user on the Id Card
				Paragraph phonePara = new Paragraph(userEntity.getPhone(),
						new Font(Font.FontFamily.HELVETICA,12,Font.BOLD));
				phonePara.setAlignment(Element.ALIGN_CENTER);
				document.add(phonePara);
//				document.add(separator);		//line separator

				//Add Email : writing the email id of the user on the Id Card
				Paragraph emailPara = new Paragraph(userEntity.getEmail(),
						new Font(Font.FontFamily.HELVETICA,11,Font.BOLD));
				emailPara.setAlignment(Element.ALIGN_CENTER);
				document.add(emailPara);
//				document.add(separator);      //line Separator

			//Add Photo : writing the photo of the user on the Id Card
				byte[] photoBytes = userEntity.getPhoto();
				Image image = Image.getInstance(photoBytes);
				image.scaleToFit(80,80);
				image.setAlignment(Element.ALIGN_CENTER);
				document.add(image);
			} catch (FileNotFoundException fe){
				System.out.println(fe.getMessage());
			} catch (DocumentException | IOException de) { //catching similar exceptions
				de.printStackTrace();
			} finally {
				if(document != null){
					document.close();
				} else{
					System.out.println("PDF File could not be generated.");
					return;
				}
				System.out.println("PDF File for --" + userEntity.getName() + "-- generated Successfully");
			}
		}
	}


//				PdfPTable table = new PdfPTable(1);
//				// Set border for table
//				table.getDefaultCell().setBorder(Rectangle.BOX);
//				table.getDefaultCell().setBorderColor(BaseColor.BLACK);
//				table.getDefaultCell().setBorderWidth(3f);
//				document.add(table);
////				table.setWidths(new float[]{1});
//
////				table.addCell(new PdfPCell(new Phrase("Name:")));
//				table.addCell(new PdfPCell(new Phrase(userEntity.getName())));
////				table.addCell(new PdfPCell(new Phrase("Position:")));
//				table.addCell(new PdfPCell(new Phrase(userEntity.getPosition())));
////				table.addCell(new PdfPCell(new Phrase("Phone:")));
//				table.addCell(new PdfPCell(new Phrase(userEntity.getPhone())));
////				table.addCell(new PdfPCell(new Phrase("Email:")));
//				table.addCell(new PdfPCell(new Phrase(userEntity.getEmail())));

//				Paragraph paragraph = new Paragraph("Name: " + userEntity.getName() + "\nPosition:" +
//						userEntity.getPosition() + "\nPhone:" + userEntity.getPhone() +
//						"\nEmail: " + userEntity.getEmail() + "\n");
//				document.add(paragraph);
//
//				PdfPCell photoCell = new PdfPCell(image, true);
//				photoCell.setRowspan(3);
//				table.addCell(photoCell);
//				document.add(table);
