package at.technikumvienna.CDAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class Codesystem
{
	private static Logger logger = Logger.getRootLogger();
	// in case u use eclipse: for logger output to eclipse console:
	// https://stackoverflow.com/questions/3501355/log4j-output-not-displayed-in-eclipse-console
	
	private String  codesystemOID;
	private String  codesystemDisplayName;
	
	private String  codesystemFilepath;
	private File    codesystemFile;
	
	private boolean isValid = false;
	
	public Codesystem(String filepathCodesystem)
	{
		this.codesystemFilepath = filepathCodesystem;
		this.codesystemFile = new File(this.codesystemFilepath);
		this.isValid = this.init();
	}
	
	private boolean init()
	{
		boolean isValid;
		
		try
		{
			Scanner filereader = new Scanner(this.codesystemFile);
			
			if( filereader.hasNext() )
			{
				this.codesystemOID = filereader.next();
				
				if( filereader.hasNext() )
				{
					this.codesystemDisplayName = filereader.next();
					logger.info("Valid code system \""
							+ this.codesystemDisplayName
							+ "\" ("
							+ this.codesystemOID
							+ ") found in file: "
							+ this.codesystemFilepath);
					isValid = true;
				}
				else
				{
					logger.debug("No code system display name found in file: "+ this.codesystemFilepath);
					isValid = false;
				}
			}
			else
			{
				logger.debug("No code system found in file: " + this.codesystemFilepath);
				isValid = false;
			}
			
			filereader.close();
		}
		catch (FileNotFoundException ex)
		{
			logger.debug("Code system file could not be found: " + this.codesystemFilepath);
			isValid = false;
		}
		
		return isValid;
	}
	
	public boolean isValid()
	{
		return this.isValid;
	}
	
	public boolean hasCode(String code)
	{
		return getCodeDisplayName(code) != null ? true : false; 
	}
	
	public String getCodeDisplayName(String code)
	{
		boolean foundCode=false;
		String codeDisplayName = null;
		
		try
		{
			Scanner filereader = new Scanner(this.codesystemFile);
			
			if( filereader.hasNextLine() )
			{
				filereader.nextLine(); // skip first line where OID and display name are
				
				while(filereader.hasNextLine() && !foundCode)
				{
					// read next line (i.e. the next code definition) from code system file
					String currentLine = filereader.nextLine();
					
					Scanner lineReader = new Scanner(currentLine);
					
					// extract first word out of this line (i.e. the actual code)
					if(lineReader.hasNext())
					{
						String codeCandidate = lineReader.next();
						
						// check if current code matches
						if( codeCandidate.equals(code) )
						{
							foundCode = true;
							
							// extract the rest of the line (i.e. the codeDisplayName)
							codeDisplayName = currentLine.substring(codeCandidate.length()).trim();
						}
					}
					
					lineReader.close();
				}
			}
			else
			{
				logger.warn("Code system had been added successfully for further use, but now no code system found in file: " + this.codesystemFilepath);
			}
			
			filereader.close();
		}
		catch (FileNotFoundException ex)
		{
			logger.warn("Code system had been added successfully for further use, but now file could not be found anymore: " + this.codesystemFilepath);
		}
		
		if (foundCode)
			logger.trace("Found code " + code + " in code system " + this.codesystemDisplayName + " (" + this.codesystemOID + ").");
		else
			logger.trace("Could not find code " + code + " in code system " + this.codesystemDisplayName + " (" + this.codesystemOID + ").");
		
		return codeDisplayName;
	}
	
	public String getOID()
	{
		return this.codesystemOID;
	}
	
	public String getDislayName()
	{
		return this.codesystemDisplayName;
	}
}
