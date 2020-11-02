package at.technikumvienna.CDAPI.CDA;

public interface CdaEntry
{
	public String getType(); // Strings for now, to be replaced by an enum of CdaEntryTypes
	public String toXmlElement(String indent);
}
