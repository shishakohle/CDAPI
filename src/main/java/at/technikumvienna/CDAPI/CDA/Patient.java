package at.technikumvienna.CDAPI.CDA;

import at.technikumvienna.CDAPI.CDA.datatypes.PointInTime;

public class Patient implements CdaEntry
{
	private final static String cdaEntryType = "patient";
	
	private String givenName;
	private String familyName;
	private String gender; // PROOF OF CONCEPT ONLY! Gender here is improvised as a String. CDAPI in the future shall provide: Class administrativeGender implements CdaDatatype lateron.
	private String maritalStatus; // Same goes for maritalStatus.
	private PointInTime birthday;
	
	public Patient(String givenName, String familyName, String gender, String maritalStatus, PointInTime birthday)
	{
		this.givenName = givenName;
		this.familyName = familyName;
		this.gender = gender;
		this.maritalStatus = maritalStatus;
		this.birthday = birthday;
		/* TODO: check for null pointers */
	}
	
	/* proof-of-concept:
	 * Here are two example for how an overloaded constructor
	 * could be created for better use of this Class.
	 */
	
	public Patient(String givenName, String familyName, PointInTime birthday)
	{
		this(givenName, familyName, null, null, birthday);
	}
	
	public Patient(String givenName, String familyName, String gender, String maritalStatus, String birthday)
	{
		this(givenName,
			familyName,
			gender,
			maritalStatus,
			new PointInTime(birthday) );
	}
	
	@Override
	public String getType()
	{
		return cdaEntryType;
	}
	
	@Override
	public String toXmlElement(String indent)
	{
		/* This current CDAPI is proof of concept only!
		 * So far, here is mostly a shortcut with hardcoded Strings.
		 * This is a mock-up but not a real implementation.
		 *  But e.g. the <birthTime> XML Tag shows that CDAPI nests
		 * calls of toXmlElement() to assemble XML Tags that contain
		 * different XML Tags or XML Attributes.
		 *  As soon as CDAPI grows above the "proof of concept"
		 * status, there will be pattern files, that provide (even
		 * nested) patterns as an external and customizabele
		 * resource to CDAPI. Like this, CDAPI plans to grow a whole
		 * library of the most commom patterns.
		 *  Also ids and templateIds are just randomly made up/copied
		 * from an PHMR example, the statusCode is hardcoded here.
		 * This current implementation of CDAPI just proves that all
		 * these could be added to CDAPI as either coded implementa-
		 * tions of the CdaDatatype interface/the CdaEntry interface
		 * or could be "plugged in" externally as a pattern with
		 * placeholders - all of which is actually demonstrated with
		 * a few examples in other places of this CDAPI implementa-
		 * tion.
		 */
		
		String out;
		
		out  = indent + "<patient>" + "\n";
		out += indent + "\t<name>" + "\n";
		out += indent + "\t\t<given>" + this.givenName + "</given>" + "\n";
		out += indent + "\t\t<family>" + this.familyName + "</family>" + "\n";
		out += indent + "\t</name>" + "\n";
		out += indent + "\t<administrativeGenderCode code=\"" + this.gender.charAt(0) /* Yes, thats a dirtxy workaround, not the CDAPI concept */ + "\" codeSystem=\"2.16.840.1.113883.5.1\" displayName=\"" + this.gender + "\" codeSystemName=\"AdministrativeGender\"/>" + "\n";
		out += indent + "\t<birthTime value=\"" + this.birthday.toXmlElement() + "\"/>" + "\n";
		out += indent + "\t<maritalStatusCode code=\"" + this.maritalStatus.charAt(0) /* Yes, thats a dirtxy workaround, not the CDAPI concept */ + "\" displayName=\"" + this.maritalStatus + "\" codeSystem=\"2.16.840.1.113883.5.2\" codeSystemName=\"MaritalStatus\"/>" + "\n";
		out += indent + "</patient>";
		
		return out;
	}
}
