package at.technikumvienna.CDAPI;

public class CodeAlias
{
	private String alias;
	private String codesystemAlias;
	private String actualCode;
	
	public CodeAlias(String alias, String codesystemAlias, String actualCode)
	{
		this.alias           = alias;
		this.codesystemAlias = codesystemAlias;
		this.actualCode      = actualCode;
	}
	public String getAlias()
	{
		return this.alias;
	}
	
	public String getCodesystemAlias()
	{
		return this.codesystemAlias;
	}
	
	public String getActualCode()
	{
		return this.actualCode;
	}
}
