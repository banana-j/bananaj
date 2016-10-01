/**
 * @author alexanderweiss
 * @date 07.12.2015
 */
package model.list;


/**
 * Class for representing a growth history of a mailChimpList
 * @author alexanderweiss
 *
 */
public class GrowthHistory {

	private MailChimpList mailChimpList;
	private String list_id;
	private String month;
	private int existing;
	private int imports;
	private int optins;
	
	
	
	public GrowthHistory(MailChimpList mailChimpList, String month, int existing, int imports, int optins) {
		setMailChimpList(mailChimpList);
		setList_id(mailChimpList.getId());
		setMonth(month);
		setExisting(existing);
		setImports(imports);
		setOptins(optins);
	}
	
	/**
	 * @return the mailChimpList
	 */
	public MailChimpList getMailChimpList() {
		return mailChimpList;
	}

	/**
	 * @param mailChimpList the mailChimpList to set
	 */
	public void setMailChimpList(MailChimpList mailChimpList) {
		this.mailChimpList = mailChimpList;
	}

	/**
	 * @return the list_id
	 */
	public String getList_id() {
		return list_id;
	}

	/**
	 * @param list_id the list_id to set
	 */
	public void setList_id(String list_id) {
		this.list_id = list_id;
	}

	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}

	/**
	 * @return the existing
	 */
	public int getExisting() {
		return existing;
	}

	/**
	 * @param existing the existing to set
	 */
	public void setExisting(int existing) {
		this.existing = existing;
	}

	/**
	 * @return the imports
	 */
	public int getImports() {
		return imports;
	}

	/**
	 * @param imports the imports to set
	 */
	public void setImports(int imports) {
		this.imports = imports;
	}

	/**
	 * @return the optins
	 */
	public int getOptins() {
		return optins;
	}

	/**
	 * @param optins the optins to set
	 */
	public void setOptins(int optins) {
		this.optins = optins;
	}

	@Override
	public String toString(){
		return "Growth History for mailChimpList: " + getList_id() + System.lineSeparator() +
				"Month: " + getMonth() + System.lineSeparator() +
				"Existing members: " + getExisting() + System.lineSeparator() +
				"Imported member: " + getImports() + System.lineSeparator() +
				"Opt-ins: " + getOptins();
	}
	
}
