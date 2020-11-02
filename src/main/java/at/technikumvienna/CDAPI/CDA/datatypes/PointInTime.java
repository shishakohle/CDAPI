package at.technikumvienna.CDAPI.CDA.datatypes;


public class PointInTime implements CdaDatatype
{
	private final static String cdaDatatype = "TS";
	
	/* 
	 * Up to now: PROOF OF CONCEPT ONLY
	 * 
	 * This yet is only a Mock-Up for the CDA Datatype "Point in Time" (TS), not a proper implementation!
	 * https://wiki.hl7.de/index.php?title=HL7_CDA_Core_Principles#Point_in_Time_.28TS.29   (2020-07-14)
	 * Also see comments in the constructor below.
	 * 
	 * See Class Physical Quantity for proof-of-concept for implementation of CDA Datatypes.
	 * See Classes Observation and Patient for proof-of-concept for implementation of CDA Entries.
	 * 
	 */
	
	private String timestamp="";
	
	public PointInTime(String timestamp)
	{
		this.timestamp = timestamp!=null ? timestamp : "";
		
		/* As this is proof of concept only, this is no
		 * proper implementation. Obviously any validation
		 * of the String timestamp to conform the CdaDatatype
		 * is yet missing here. Maybe a String isn't even the
		 * best idea? Maybe a java.util.Date object serves
		 * better, combined with java.text.SimpleDateFormat
		 * in the toXmlElement() method.
		 */
	}
	
	@Override
	public String getType()
	{
		return cdaDatatype;
	}
	
	@Override
	public String toXmlElement()
	{
		return timestamp;
	}
	
}
