/**
 * @author alexanderweiss
 * @date 07.12.2015
 */
package com.github.alexanderwe.bananaj.model.list;


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
		this.mailChimpList = mailChimpList;
		this.list_id = mailChimpList.getId();
		this.month = month;
		this.existing = existing;
		this.imports = imports;
		this.optins = optins;
	}
	
	/**
	 * @return the mailChimpList
	 */
	public MailChimpList getMailChimpList() {
		return mailChimpList;
	}

	/**
	 * @return the list_id
	 */
	public String getList_id() {
		return list_id;
	}

	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * @return the existing
	 */
	public int getExisting() {
		return existing;
	}

	/**
	 * @return the imports
	 */
	public int getImports() {
		return imports;
	}

	/**
	 * @return the optins
	 */
	public int getOptins() {
		return optins;
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
