/* CDAPI (0.0.1-alpha1)
 * 
 * API for the creation of HL7 CDA Files in Java Applications.
 * A proof-of-concept implementation.
 * 
 * Ingo Weigel, Vienna, November 2020
 * 
 * Find this project on GitHub: https://github.com/shishakohle/CDAPI
 * 
 * This implementation was created as part of the "Project Related
 * Teamwork" courses (Master Medical Engineering and e-Health) at
 * Technikum Vienna University of Applied Sciences (Austria) under
 * the guidance of lecturer Matthias Frohner, PhD, MSc.
 * 
 * CDAPI version 0.0.1-alpha1 is licensed unter the GNU General
 * Public License v3.0.

CDAPI version 0.0.1-alpha1 - API for the creation of HL7 CDA Files
in Java Applications
Copyright (C) 2020  Ingo Weigel

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, version 3 of the License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see https://www.gnu.org/licenses/ .

 */


package at.technikumvienna.CDAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import at.technikumvienna.CDAPI.CDA.CdaEntry;
import at.technikumvienna.CDAPI.CDA.CdaFile;
import at.technikumvienna.CDAPI.CDA.Observation;
import at.technikumvienna.CDAPI.CDA.Patient;

public class CdaCreator
{
	private final static String log4jConfPath = "./src/main/resources/log4j.properties";
	private static Logger logger = Logger.getRootLogger();
	// in case u use eclipse: for logger output to eclipse console:
	// https://stackoverflow.com/questions/3501355/log4j-output-not-displayed-in-eclipse-console
	
	private Map<String,String    > config      = new HashMap<String,String    >();
	private Map<String,CdaFile   > cdaFiles    = new HashMap<String,CdaFile   >();
	private Map<String,Codesystem> codesystems = new HashMap<String,Codesystem>();
	private Map<String,CodeAlias > codeAliases = new HashMap<String,CodeAlias >();
	
	public CdaCreator()
	{
		// configure logger
		PropertyConfigurator.configure(log4jConfPath);
	}
	
	public void initFile(String alias, String filepathPattern)
	/*TODO returnvalue*/
	{
		CdaFile newFile = new CdaFile(filepathPattern);
		
		if(newFile.isInitialized())
		{
			if( !fileExists(alias) )
			{
				this.cdaFiles.put(alias, newFile);
				logger.info("Succesfully initialized new file with alias "
						+ alias
						+ " from pattern: "
						+ filepathPattern);
			}
			else
			{
				logger.debug("A CDA file with alias "
						+ alias
						+ " already exists. Therefore, no new file was initialized.");
			}
		}
		else
		{
			logger.debug("Could not initialize new CDA file with pattern: " + filepathPattern);
		}
	}
	
	public void setPatient(String aliasFile, Patient patient)
	/* TODO: returnvalue*/
	{
		// has FILE been initialized?
		if( fileExists(aliasFile) )
		{
			this.cdaFiles.get(aliasFile).setPatient(patient);
			logger.info("New patient set for CDA File with alias " + aliasFile + ".");
		}
		else
		{
			logger.debug("No CDA File with alias " + aliasFile + " has been initialized. "
					+ "Therefore, the patient could not be assigned to a CDA File.");
		}
	}
	
	public void addEntry(String aliasFile, CdaEntry entry)
	/* TODO: returnvalue*/
	{
		// has FILE been initialized?
		if( fileExists(aliasFile) )
		{
			if(entry.getType().equals("observation"))
			{
				Observation observation = (Observation)entry;
				String codeAliasStr = observation.getCodeAlias();
				if (this.codeAliases.containsKey(codeAliasStr))
				{
					CodeAlias codeAlias = this.codeAliases.get(codeAliasStr);
					String codesystemAlias = codeAlias.getCodesystemAlias();
					
					String codesystemOID         = this.codesystems.get(codesystemAlias).getOID();
					String codesystemDisplayName = this.codesystems.get(codesystemAlias).getDislayName();
					String actualCode            = codeAlias.getActualCode();
					String actualCodeDisplayName = this.codesystems.get(codesystemAlias).getCodeDisplayName(actualCode);
					
					observation.setCodesystemOID         (codesystemOID);
					observation.setCodesystemDisyplayName(codesystemDisplayName);
					observation.setActualCode            (actualCode);
					observation.setActualCodeDisplayName (actualCodeDisplayName);
					
					this.cdaFiles.get(aliasFile).addEntry(observation);
					logger.info("New observation of " + codeAliasStr + " added to CDA File with alias " + aliasFile + ".");
				}
				else
				{
					logger.debug("Code alias \"" + codeAliasStr + "\" was not defined yet. "
								+ "Therefore, the observation of " + codeAliasStr
								+ " was not added to CDA File with alias " + aliasFile + ".");
				}
			}
			else
			{
				this.cdaFiles.get(aliasFile).addEntry(entry);
				logger.info("New entry added to CDA File with alias " + aliasFile + ".");
			}
		}
		else
		{
			logger.debug("No CDA File with alias " + aliasFile + " has been initialized. "
					+ "Therefore, the entry could not be assigned to a CDA File.");
		}
	}
	
	public void useConfig(String filepathConfig)
	/*TODO returnvalue*/
	{
		File configFile = new File(filepathConfig);
		
		try
		{
			Scanner filereader = new Scanner(configFile);
			
			while( filereader.hasNextLine() )
			{
				String currentLine = filereader.nextLine();
				String placeholder = currentLine.substring(0, currentLine.indexOf(" "));
				String placefiller = currentLine.substring(currentLine.indexOf(" ")+1, currentLine.length());
				
				this.config.put(placeholder, placefiller);
				
				logger.trace("Extracted " + placefiller + " for placeholder " + placeholder + " from: " + filepathConfig);
			}
			
			logger.info(this.config.size() + " placeholders/-fillers were found in: " + filepathConfig);
			
			filereader.close();
		}
		catch (FileNotFoundException ex)
		{
			logger.debug("Config file could not be found:" + filepathConfig);
		}
	}
	
	public void useCodesystem(String alias, String filepath)
	/*TODO returnvalue*/
	{
		Codesystem codesystem = new Codesystem(filepath);
		
		if (codesystem.isValid())
		{
			if ( !codesystemInUse(alias) )
			{
				this.codesystems.put(alias, codesystem);
				logger.info("Successfully added code system \""
						+ codesystem.getDislayName()
						+ "\" ("
						+ codesystem.getOID()
						+ ") with alias \""
						+ alias
						+ "\" for further use.");
			}
			else
			{
				logger.info("A code system aliased as \""
						+ alias
						+ "\" has already been loaded. Therefore, "
						+ filepath
						+ " was not added to code systems for further use.");
			}
		}
		else
		{
			logger.debug("Could not add code system from file: " + filepath);
		}
	}
	
	public void aliasCode(String alias, String aliasCodesystem, String actualCode)
	/*TODO returnvalue*/
	{
		// check whether code system has already been added
		if( codesystemInUse(aliasCodesystem) )
		{
			// check whether code actually exists in the code system
			if( codesystems.get(aliasCodesystem).hasCode(actualCode) )
			{
				// create new CodeAlias
				CodeAlias codealias = new CodeAlias(alias, aliasCodesystem, actualCode);
				this.codeAliases.put(alias, codealias);
				logger.info(actualCode + " in "
						+ aliasCodesystem
						+ " is now aliased as "
						+ alias
						+ ".");
			}
			else
			{
				logger.debug("\"" + actualCode
						+ "\" does not exist in "
						+ "\"" + aliasCodesystem
						+ "\", therefore code alias \""
						+ alias
						+ "\" could not be created.");
			}
		}
		else
		{
			logger.debug("\"" + aliasCodesystem
					+ "\" has not been added for further use yet, therefore code alias \""
					+ alias
					+ "\" could not be created.");
		}
	}
	
	public void saveFile (String aliasFile, String filepath)
	/* TODO: returnvalue*/
	{		
		// has FILE been initialized?
		if( fileExists(aliasFile) )
		{
			if( this.cdaFiles.get(aliasFile).saveFile(this.config, filepath))
			{
				logger.info("Successfully rendered CDA File with alias " + aliasFile
						+ " and saved it to: " + filepath);
			}
			else
			{
				logger.debug("Could not render CDA File with alias " + aliasFile
						+ " and not save it to: "+ filepath);
			}
		}
		else
		{
			logger.debug("No CDA File with alias " + aliasFile + " has been initialized. "
					+ "Therefore, CDA File was not rendered and not saved to: " + filepath);
		}
	}
	
	private boolean fileExists(String fileAlias)
	{
		return this.cdaFiles.containsKey(fileAlias);
	}
	
	private boolean codesystemInUse(String aliasCodesystem)
	{
		return this.codesystems.containsKey(aliasCodesystem);
	}
}
