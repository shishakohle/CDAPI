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
 * CDAPI version 0.0.1-alpha1 is licensed under the GNU General
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

import at.technikumvienna.CDAPI.CDA.Observation;
import at.technikumvienna.CDAPI.CDA.Patient;
import at.technikumvienna.CDAPI.CDA.datatypes.PhysicalQuantity;

public class ShowcasesForCDAPI
{
	public static void main(String[] args)
	{
		showcaseCDAPItutorial();
	}
	
	public static void showcaseCDAPItutorial()
	/**
	 * This is a tutorial on how to use CDAPI to create a CDA File.
	 * As an example, we'll create a PHMR (Personal Healthcare Monitor-
	 * ing Report), which is the only CDA Type supported by CDAPI so far
	 * (as a proof of concept). We'll take some raw information on weight
	 * and blood pressure and put in the PHMR.
	 */
	{
		/* Let's pretend, from some other source, we received a patient's
		 * data and some observations concerning their weight and blood
		 * pressure.
		 */
		
		// Our patient is Anna Cunningham
		String patientGivenName     = "Anna";
		String patientFamilyName    = "Cunningham";
		String patientGender        = "Female";
		String patientMaritalStatus = "Single";
		String patientBirthday      = "19910208";
		
		// Anna was weighed 72 kg on 2nd of Nov 2020, 9:30:15 a.m.,
		// Timezone UTC+1hour (applies for Vienna e.g.)
		String weightRecordingTime = "20201102093015+0100";
		String weightValue         = "72";
		String weightUnit          = "kg";
		
		// Anna's blood pressure was measured 120/80
		String bloodpressureRecordingTime = "20201102093327+0100";
		String bloodpressureSystolic      = "120";
		String bloodpressureDiastolic     = "80";
		String bloodpressureUnit          = "mmHg";
		
		/* First step for creating a CDA file with CDAPI is creating a
		 * CdaCreator object.
		 *  This CdaCreator will serve us with all means we need to
		 * create a CDA File and put data into it, provided CDAPI
		 * already supports our desired type of CDA. For our showcase
		 * and as a proof of concept, CDAPI already supports PHMRs. 
		 */
		
		CdaCreator myCdaCreator = new CdaCreator();
		
		/* Before we initialize a CDA File with CDAPI, we may want to
		 * load a basic configuriation, i.e. a context that applies
		 * on a general level for all CDA Files we're going to create
		 * and not only for a certain single CDA File.
		 * 	So we'll provide CDAPI with such a config file, which
		 * defines things like e.g. your name or the organization you
		 * are working for. Typically, these are data that go into
		 * the header/metadata (but may also be used in the CDA body)
		 * and represent information, that don't really change but
		 * stay basically the same for any CDA File created with CDAPI.
		 */
		
		myCdaCreator.useConfig("./src/main/resources/CDA-patterns/baseconfig.conf");
		
		/* Next, let's tell CDAPI that we want to initialize a new CDA
		 * File.
		 *  For this, we need to provide an alias for this CDA file
		 * which lateron allows for an identification of this certain
		 * file. And, as we want to create a PHMR, we need to provide
		 * a file which holds a CDAPI pattern of this certain CDA Type.
		 */
		
		myCdaCreator.initFile("myPHMR", "./src/main/resources/CDA-patterns/PHMR.pattern");
		
		/* One of the first things we might want to add to our PHMR is
		 * the patient the report is about. So let's hand over the
		 * patient's raw data to CDAPI.
		 */
		
		myCdaCreator.setPatient("myPHMR", new Patient( 
												patientGivenName,
												patientFamilyName,
												patientGender,
												patientMaritalStatus,
												patientBirthday) );
		
		/* We're almost at the point where we can add our medical mea-
		 * surements to the PHMR. One last step before that:
		 *  We intend to use the LOINC code system to describe our
		 * weight and blood pressure measurements. So next, we'll
		 * introduce CDAPI to the LOINC system. Let's choose on an
		 * alias for the LOINC code system and where to find the file
		 * representing the LOINC code system.
		 */
		
		myCdaCreator.useCodesystem("LOINC", "./src/main/resources/codesystems/loinc.codesystem");
		
		/* Bear in mind that CDAPI is just a proof-of-concept implemen-
		 * tation yet. The 'loinc.codesystem' file used here provides
		 * three LOINC codes only: weight, blood pressure systolic and
		 * diastolic.
		 * 	But so far, that's enough to show that we could create and
		 * use code system files for the whole LOINC system as well as
		 * for any other code system we might need.
		 *  As you can see, code systems are not baked into the source
		 * code of CDAPI, but instead are "plugged in" as a file (i.e.
		 * an external resource) to CDAPI. We therefore may exchange,
		 * add or update code systems basically "during runtime" and
		 * without touching the source code of CDAPI.
		 *  CDAPI on the long run is intended to bring along files for
		 * the most common medical code systems.
		 */
		
		/* Now let's choose on aliases for LOINC codes, so we don't
		 * need to remember the actual LOINC codes over and over.
		 */
		
		myCdaCreator.aliasCode("weight"      , "LOINC", "3141-9");
		myCdaCreator.aliasCode("bp_systolic" , "LOINC", "76534-7");
		myCdaCreator.aliasCode("bp_diastolic", "LOINC", "76535-4");
		
		/* We're almost there. Of course we want to add the observa-
		 * tions, i.e. the raw data on weight and blood pressure, to
		 * the PHMR, which can be done with ease:
		 */
		
		myCdaCreator.addEntry("myPHMR", new Observation( 
												"weight",
												new PhysicalQuantity(weightValue, weightUnit),
												weightRecordingTime ));
		
		myCdaCreator.addEntry("myPHMR", new Observation(
												"bp_systolic",
												new PhysicalQuantity(bloodpressureSystolic, bloodpressureUnit),
												bloodpressureRecordingTime ));
		
		myCdaCreator.addEntry("myPHMR", new Observation(
				"bp_diastolic",
				new PhysicalQuantity(bloodpressureDiastolic, bloodpressureUnit),
				bloodpressureRecordingTime ));
		
		/* Everything is set up! We're now ready to actually render
		 * the final CDA document and save it to the harddisk or an
		 * USB drive or some cloud storage or to an IHE XDS environment
		 * or (...)
		 */
		
		myCdaCreator.saveFile("myPHMR", "./src/main/resources/rendered-CDA/MyPHMR.xml");
		
		
		
		/* But be careful: Before we put out our brandnew PHMR, we
		 * better make sure it is actually a valid CDA and PHMR.
		 * 
		 *  Fatemeh Zarei recently has been working on validation
		 * tools for this exact purpose, for example with Schematron
		 * files, see:
		 * 
		 *    https://github.com/fzarei/validation  (2020-11-01)
		 *    
		 *  Once a git pull request from this repo has been merged
		 * into CDAPI, you might want to use CDAPI for validation
		 * with static methods in CdaCreator, maybe something like
		 * this:
		 */
		
		// boolean PHMRisValid = CdaCreator.isValidFile("./MyPHMR.xml", "./PHMR_Schematron_File.sch"); // not implemented in CDAPI yet
	}
}
