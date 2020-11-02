package at.technikumvienna.CDAPI.CDA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import at.technikumvienna.CDAPI.CDA.datatypes.PointInTime;

public class CdaFile
{
	private static Logger logger = Logger.getRootLogger();
	// in case u use eclipse: for logger output to eclipse console:
	// https://stackoverflow.com/questions/3501355/log4j-output-not-displayed-in-eclipse-console
	
	private String  patternFilepath;
	private File    patternFile;
	private boolean isInitialized = false;
	
	private Patient patient;
	private List<CdaEntry> entries = new ArrayList<CdaEntry>();
	
	public CdaFile(String filepathPattern)
	{
		this.patternFilepath = filepathPattern;
		/* TODO: check for null pointer */
		this.patternFile     = new File(this.patternFilepath);
		this.isInitialized   = this.init();
	}
	
	private boolean init()
	{
		boolean isInitialized;
		
		try
		{
			Scanner filereader = new Scanner(this.patternFile);
			
			logger.trace("Successfully initilized CDA File with pattern file: " + this.patternFilepath);
			isInitialized = true;
			
			filereader.close();
		}
		catch (FileNotFoundException ex)
		{
			logger.debug("CDA pattern file could not be found: " + this.patternFilepath);
			isInitialized = false;
		}
		
		return isInitialized;
	}
	
	public void setPatient(Patient patient)
	{
		this.patient = patient;
		logger.trace("New patient set for CDA File.");
	}
	
	public void addEntry(CdaEntry entry)
	{
		this.entries.add(entry);
	}
	
	public boolean saveFile(Map<String,String> config, String outFilepath)
	{
		boolean success=true;
		
		try
		{
			FileWriter outFilewriter = new FileWriter(outFilepath);
			
			try
			{
				Scanner patternFilereader = new Scanner(this.patternFile);
				
				// iterate over pattern file and replace placeholders
				while( patternFilereader.hasNextLine() )
				{
					String currentLine = patternFilereader.nextLine();
					String renderedLine = currentLine;
					
					if(currentLine.contains("[") && currentLine.contains("]"))
					{
						// placeholder detected. replace it with corresponding data.
						
						String placeholder = currentLine.substring(currentLine.indexOf('['),currentLine.indexOf(']')+1);
						
						String renderedLineFirstPart = currentLine.substring(0,currentLine.indexOf('['));
						String renderedLineSecondPart = "";
						String renderedLineThirdPart = currentLine.substring(currentLine.indexOf(']')+1, currentLine.length());
						
						// remove any leading/trailing spaces
						if (!renderedLineFirstPart.isEmpty() && renderedLineFirstPart.charAt(renderedLineFirstPart.length()-1) == ' ')
						{
							renderedLineFirstPart = renderedLineFirstPart.substring(0, renderedLineFirstPart.length()-1);
						}
						if (!renderedLineThirdPart.isEmpty() && renderedLineThirdPart.charAt(0) == ' ')
						{
							renderedLineThirdPart = renderedLineThirdPart.substring(1,renderedLineThirdPart.length());
						}
						
						if( config.containsKey(placeholder) )
						{
							renderedLineSecondPart = config.get(placeholder);
							// TODO logger.trace()
						}
						else if (placeholder.equals("[entries]"))
						{
							if(!this.entries.isEmpty())
							{
								// manage indent in XML file
								String indent = renderedLineFirstPart;
								renderedLineFirstPart = "";
								
								// iterate over List entries
								for(CdaEntry entry : entries)
								{
									renderedLineSecondPart += entry.toXmlElement(indent) + "\n";
								}
								// TODO: logger.trace() 
							}
							else
							{
								success = false;
								logger.debug("No entries were added to the CDA File, therefore it could not be rendered and not be saved to: " + outFilepath);
							}
						}
						else if(placeholder.contentEquals("[timeIssued]"))
						{
							renderedLineSecondPart = currentTimeAsCdaDatatype().toXmlElement();
							// TODO: logger.trace()
						}
						else if(placeholder.contentEquals("[patient]"))
						{
							if(this.patient!=null)
							{
								// manage indent in XML file
								String indent = renderedLineFirstPart;
								renderedLineFirstPart = "";
								
								renderedLineSecondPart = this.patient.toXmlElement(indent);
								// TODO logger.trace
							}
							else
							{
								success = false;
								logger.debug("No patient was added to the CDA File, therefore it could not be rendered and not be saved to: " + outFilepath);
							}
						}
						else
						{
							success = false;
							logger.debug("Could not resolve the placeholder \"" + placeholder + "\" in pattern for CdaFile.");
						}
						
						renderedLine = renderedLineFirstPart + renderedLineSecondPart + renderedLineThirdPart;
					}
					
					outFilewriter.write(renderedLine + "\n");
				}				
				
				patternFilereader.close();
				
				if(success) logger.info("Successfully rendered CDA File and saved it to: " + outFilepath);
			}
			catch (FileNotFoundException ex)
			{
				logger.debug("CDA pattern file could not be found: " + this.patternFilepath);
				success = false;
			}
			
			outFilewriter.close();
		}
		catch (IOException ex)
		{
			success = false;
			logger.debug("Could not create or write to file: " + outFilepath);
			logger.debug("Do you have the permissions? Does the folder exist? "
					+ "Is the file locked/currently in use by another application?");
		}
		
		return success;
	}
	
	public boolean isInitialized()
	{
		return this.isInitialized;
	}
	
	private PointInTime currentTimeAsCdaDatatype()
	{
		// example 20201102093015+0100
		Date date = new Date();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssZ");
	    String currentTime = formatter.format(date);
		
	    return new PointInTime(currentTime);
	}
}
