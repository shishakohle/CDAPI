package at.technikumvienna.CDAPI.CDA;

import at.technikumvienna.CDAPI.CDA.datatypes.CdaDatatype;
import at.technikumvienna.CDAPI.CDA.datatypes.PointInTime;

public class Observation implements CdaEntry
{
	private final static String cdaEntryType = "observation";
	
	String      codeAlias;
	String      codesystemOID;
	String      codesystemDisplayName;
	String      actualCode;
	String      actualCodeDisplayName;
	CdaDatatype value;
	PointInTime effectiveTime;
	/* Mark: Proof of API concept only! Members missing here to conform HL7 v3 CDA / C-CDA. */
	// MoodCode moodCode;
	// Participant participant;
	// ...
	
	public Observation(String codeAlias, CdaDatatype value, PointInTime effectiveTime)
	{
		this.codeAlias     = codeAlias;
		this.value         = value;
		this.effectiveTime = effectiveTime;;
		// TODO: check for null pointers
	}
	
	// Overloaded constructor that allows for a String input of effectiveTime
	// important: also see the comments in Class PointInTime !
	public Observation(String codeAlias, CdaDatatype value, String effectiveTime)
	{
		this(codeAlias, value, new PointInTime(effectiveTime));
	}
	
	@Override
	public String getType()
	{
		return cdaEntryType;
	}
	
	public String getCodeAlias()
	{
		return this.codeAlias;
	}
	
	public void setCodesystemOID(String codesystemOID)
	{
		this.codesystemOID = codesystemOID;
	}
	
	public void setCodesystemDisyplayName(String codesystemDisplayName)
	{
		this.codesystemDisplayName = codesystemDisplayName;
	}
	
	public void setActualCode(String actualCode)
	{
		this.actualCode = actualCode;
	}
	
	public void setActualCodeDisplayName(String actualCodeDisplayName)
	{
		this.actualCodeDisplayName = actualCodeDisplayName;
	}
	
	@Override
	public String toXmlElement(String indent)
	{
		/* This current CDAPI is proof of concept only!
		 * So far, here is mostly a shortcut with hardcoded Strings.
		 * This is a mock-up but not a real implementation.
		 *  But e.g. the <value> XML Tag shows that CDAPI nests
		 * calls of toXmlElement() to assemble XML Tags that contain
		 * different XML Tags.
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
		
		out  = indent + "<observation classCode=\"OBS\" moodCode=\"EVN\">\n";
		out += indent + "\t<templateId root=\"2.16.840.1.113883.10.20.1.31\"/>\n";
		out += indent + "\t<templateId root=\"2.16.840.1.113883.10.20.9.8\"/>\n";
		out += indent + "\t<code code=\"" + this.actualCode + "\" codeSystem=\"" + this.codesystemOID
					  + "\" codeSystemName=\"" + this.codesystemDisplayName + "\" displayName=\""
					  + this.actualCodeDisplayName + "\"/>\n";
		out += indent + "\t<statusCode code=\"completed\"/>\n";
		out += indent + "\t<effectiveTime value=\"" + this.effectiveTime.toXmlElement() + "\"/>\n";
		out += indent + "\t" + this.value.toXmlElement() + "\n";
		out += indent + "\t<participant typeCode=\"DEV\">\n";
		out += indent + "\t\t<participantRole>\n";
		out += indent + "\t\t\t<id root=\"1.2.840.10004.1.1.1.0.0.1.0.0.1.2680\" assigningAuthorityName=\"EUI-64\" extension=\"1A-3E-41-78-9A-BC-DE-42\"/>\n";
		out += indent + "\t\t</participantRole>\n";
		out += indent + "\t</participant>\n";
		out += indent + "</observation>";
		
		return out;
	}
}
