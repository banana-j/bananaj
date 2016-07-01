/**
 * @author alexanderweiss
 * @date 06.11.2015
 */
package model.list;

import java.io.*;
import java.net.URL;
import java.util.*;

import javax.net.ssl.HttpsURLConnection;

import exceptions.FileFormatException;
import jxl.*;
import jxl.read.biff.BiffException;
import model.list.segment.Segment;
import org.json.JSONArray;
import org.json.JSONObject;

import connection.MailchimpConnection;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import model.MailchimpObject;
import model.list.member.Member;
import model.list.member.MemberStatus;


/**
 * Class for representing a mailchimp list. 
 * @author alexanderweiss
 *
 */
public class List extends MailchimpObject {

	private String name;
	private int membercount;
	private Date dateCreated;
	private MailchimpConnection connection;
	

	public List(String id, String name, int membercount, Date dateCreated, MailchimpConnection connection, JSONObject jsonRepresentation){
		super(id,jsonRepresentation);
		setName(name);
		setMembercount(membercount);
		setDateCreated(dateCreated);
		setConnection(connection);
	}

	/**
	 * Get all members in this list, with all statuses
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Member> getMembers() throws Exception{

		ArrayList<Member> members = new ArrayList<Member>();
		final JSONObject list = new JSONObject(getConnection().do_Get(new URL("https://"+connection.getServer()+".api.mailchimp.com/3.0/lists/"+this.getId()+"/members")));
		final JSONArray membersArray = list.getJSONArray("members");


		for (int i = 0 ; i < membersArray.length();i++)
		{
			final JSONObject memberDetail = membersArray.getJSONObject(i);
			final JSONObject memberMergeTags = memberDetail.getJSONObject("merge_fields");
			final JSONObject memberStats = memberDetail.getJSONObject("stats");

			HashMap<String, Object> merge_fields = new HashMap<String, Object>();

			Iterator a = memberMergeTags.keys();
			while(a.hasNext()) {
				String key = (String)a.next();
				// loop to get the dynamic key
				String value = (String)memberMergeTags.get(key);
				merge_fields.put(key, value);
			}
			Member member = new Member(memberDetail.getString("id"),this,merge_fields,memberDetail.getString("unique_email_id"), memberDetail.getString("email_address"), translateStringIntoMemberStatus(memberDetail.getString("status")),memberDetail.getString("timestamp_signup"),memberDetail.getString("ip_signup"),memberDetail.getString("timestamp_opt"),memberDetail.getString("ip_opt"),memberStats.getDouble("avg_open_rate"),memberStats.getDouble("avg_click_rate"),memberDetail.getString("last_changed"),this.getConnection(),memberDetail);
			members.add(member);

		}
		return members;
	}

	/**
	 * Get a single member from list
	 * @param memberID
	 * @return
	 * @throws Exception
	 */
	public Member getMember(String memberID) throws Exception{
		final JSONObject member = new JSONObject(getConnection().do_Get(new URL("https://"+connection.getServer()+".api.mailchimp.com/3.0/lists/"+getId()+"/members/"+memberID)));
    	final JSONObject memberMergeTags = member.getJSONObject("merge_fields");
    	final JSONObject memberStats = member.getJSONObject("stats");

		HashMap<String, Object> merge_fields = new HashMap<String, Object>();

		Iterator a = memberMergeTags.keys();
		while(a.hasNext()) {
			String key = (String)a.next();
			// loop to get the dynamic key
			String value = (String)memberMergeTags.get(key);
			System.out.print(key);
			System.out.println(": "+value);
			merge_fields.put(key, value);
		}
		return new Member(member.getString("id"),this,merge_fields,member.getString("unique_email_id"), member.getString("email_address"),  translateStringIntoMemberStatus(member.getString("status")),member.getString("timestamp_signup"),member.getString("ip_signup"),member.getString("timestamp_opt"),member.getString("ip_opt"),memberStats.getDouble("avg_open_rate"),memberStats.getDouble("avg_click_rate"),member.getString("last_changed"),this.getConnection(),member);
	}
	
	/**
	 * Add a member with the minimum of information
	 * @param status
	 * @param emailAdress
	 */
	public void addMember(MemberStatus status, String emailAdress) throws Exception{
		JSONObject member = new JSONObject();
		member.put("status", status.getStringRepresentation());
		member.put("email_address", emailAdress);

        getConnection().do_Post(new URL(connection.getLISTENDPOINT()+"/"+this.getId()+"/members"),member.toString());
	}
	
	/**
	 * Add a member with first and last name
	 * @param status
	 * @param emailAdress
	 * @param merge_fields_values
	 * @throws Exception
	 */
	public void addMember(MemberStatus status, String emailAdress, HashMap<String, Object> merge_fields_values) throws Exception{
		URL url = new URL(connection.getLISTENDPOINT()+"/"+this.getId()+"/members");			
		
		JSONObject member = new JSONObject();
		JSONObject merge_fields = new JSONObject();

		Iterator it = merge_fields_values.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			it.remove(); // avoids a ConcurrentModificationException
			merge_fields.put(pair.getKey().toString(), pair.getValue());
		}
		
		member.put("status", status.getStringRepresentation());
		member.put("email_address", emailAdress);
		member.put("merge_fields", merge_fields);
        getConnection().do_Post(new URL(connection.getLISTENDPOINT()+"/"+this.getId()+"/members"),member.toString());
	}

	public void importMembersFromFile(File file) throws FileFormatException, IOException{
		//TODO fully implement read from xls
		String extension = getExtension(file);

		if(extension.equals(".xls")|| extension.equals(".xlsx")){
			Workbook w;
			try {
				w = Workbook.getWorkbook(file);
				// Get the first sheet
				Sheet sheet = w.getSheet(0);
				// Loop over first 10 column and lines

				for (int j = 0; j < sheet.getColumns(); j++) {
					for (int i = 0; i < sheet.getRows(); i++) {
						Cell cell = sheet.getCell(j, i);
						CellType type = cell.getType();
						if (type == CellType.LABEL) {
							System.out.println("I got a label "
									+ cell.getContents());
						}

						if (type == CellType.NUMBER) {
							System.out.println("I got a number "
									+ cell.getContents());
						}

					}
				}
			} catch (BiffException e) {
				e.printStackTrace();
			}

		}







	}

	/**
	 * Delete a member from list
	 * @param memberID
	 * @throws Exception
	 */
	public void deleteMemberFromList(String memberID) throws Exception{
		getConnection().do_Delete(new URL("https://"+connection.getServer()+".api.mailchimp.com/3.0/lists/"+getId()+"/members/"+memberID));
	}
	
	/**
	 * Get the growth history of this list
	 * @return a growth history
	 * @throws Exception
	 */
	public GrowthHistory getGrowthHistory() throws Exception{
		final JSONObject growth_history = new JSONObject(getConnection().do_Get(new URL(connection.getLISTENDPOINT()+"/"+this.getId()+"/growth-history")));
    	final JSONArray history = growth_history.getJSONArray("history");
    	final JSONObject historyDetail = history.getJSONObject(0);
    	
    	return new GrowthHistory(this, historyDetail.getString("month"), historyDetail.getInt("existing"), historyDetail.getInt("imports"), historyDetail.getInt("optins"));
	}

	/**
	 * Get the segments of this list
	 * @return
	 * @throws Exception
     */
	public ArrayList<Segment> getSegments() throws Exception{
        ArrayList<Segment> segments = new ArrayList<Segment>();
        URL url = new URL(connection.getLISTENDPOINT()+"/"+this.getId()+"/segments");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("Authorization",connection.getApikey());

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode+"\n");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();



        return segments;
    }
	
	/**
	 * Writes the data of this list to an excel file in current directory. Define wether to show merge fields or not
	 * @param show_merge
	 * @throws Exception
	 */
	public void writeToExcel(String filepath,boolean show_merge) throws Exception{
		ArrayList<Member> members = this.getMembers();
		int merge_field_count = 0;
		WritableWorkbook workbook;


		if(filepath.contains(".xls")){
			workbook = Workbook.createWorkbook(new File(filepath));
		}else{
			workbook = Workbook.createWorkbook(new File(filepath+".xls"));
		}

		WritableSheet sheet = workbook.createSheet(this.getName(), 0);
		
		
		WritableFont times16font = new WritableFont(WritableFont.TIMES, 16, WritableFont.BOLD, false); 
		WritableCellFormat times16format = new WritableCellFormat (times16font); 
		
		Label memberIDLabel = new Label(0, 0, "MemberID",times16format);
		Label email_addressLabel = new Label(1,0,"Email Address",times16format);
		Label timestamp_sign_inLabel = new Label(2,0,"Sign up",times16format);
		Label ip_signinLabel = new Label(3,0,"IP Sign up", times16format);
		Label timestamp_opt_inLabel = new Label(4,0,"Opt in",times16format);
		Label ip_optLabel = new Label(5,0,"IP Opt in", times16format);
		Label statusLabel = new Label(6,0,"Status",times16format);
		Label avg_open_rateLabel = new Label(7,0,"Avg. open rate",times16format);
		Label avg_click_rateLabel = new Label(8,0,"Avg. click rate",times16format);
		

		sheet.addCell(memberIDLabel);
		sheet.addCell(email_addressLabel);
		sheet.addCell(timestamp_sign_inLabel);
		sheet.addCell(ip_signinLabel);
		sheet.addCell(timestamp_opt_inLabel);
		sheet.addCell(ip_optLabel);
		sheet.addCell(statusLabel);
		sheet.addCell(avg_open_rateLabel);
		sheet.addCell(avg_click_rateLabel);

		if (show_merge){
			int last_column = 9;

			Iterator iter = members.get(0).getMerge_fields().entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry pair = (Map.Entry)iter.next();
				sheet.addCell(new Label(last_column,0,(String)pair.getKey(),times16format));
				iter.remove(); // avoids a ConcurrentModificationException
				last_column++;
				merge_field_count++;
			}
		}


		for(int i = 0 ; i < members.size();i++)
		{
			Member member = members.get(i);
			sheet.addCell(new Label(0,i+1,member.getId()));
			sheet.addCell(new Label(1,i+1,member.getEmail_address()));
			sheet.addCell(new Label(2,i+1,member.getTimestamp_signup()));
			sheet.addCell(new Label(3,i+1,member.getIp_signup()));
			sheet.addCell(new Label(4,i+1,member.getTimestamp_opt()));
			sheet.addCell(new Label(5,i+1,member.getIp_opt()));
			sheet.addCell(new Label(6,i+1,member.getStatus().getStringRepresentation()));
			sheet.addCell(new Number(7,i+1,member.getAvg_open_rate()));
			sheet.addCell(new Number(8,i+1,member.getAvg_click_rate()));

			if (show_merge){
				//add merge fields values
				int last_index = 9;
				Iterator iter_member = member.getMerge_fields().entrySet().iterator();
				while (iter_member.hasNext()) {
					Map.Entry pair = (Map.Entry)iter_member.next();
					sheet.addCell(new Label(last_index,i+1,(String)pair.getValue()));
					iter_member.remove(); // avoids a ConcurrentModificationException
					last_index++;

				}
			}
		}

		CellView cell;

		int column_count = 9 + merge_field_count;
		for(int x=0;x<column_count;x++)
		{
			cell=sheet.getColumnView(x);
			cell.setAutosize(true);
			sheet.setColumnView(x, cell);
		}


		workbook.write(); 
		workbook.close();

		System.out.println("Writing to excel - done");
	}
	
	/**
	 * Convert a string containing the type of a campaign in CompaignType enum
	 * @param campaignType
	 * @return
	 */
	private MemberStatus translateStringIntoMemberStatus(String campaignType){
		switch(campaignType){
		case "pending": return MemberStatus.PENDING;
		case "subscribed": return MemberStatus.SUBSCRIBED;
		case "unsubscribed": return MemberStatus.UNSUBSCRIBED;
		case "cleaned":  return MemberStatus.CLEANED;
		default:return null;
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the membercount
	 */
	public int getMembercount() {
		return membercount;
	}
	/**
	 * @param membercount the membercount to set
	 */
	public void setMembercount(int membercount) {
		this.membercount = membercount;
	}
	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}
	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public MailchimpConnection getConnection(){
		return this.connection;
	}
	
	public void setConnection(MailchimpConnection connection){
		this.connection = connection;
	}
	
	@Override
	public String toString(){
		return this.getId() + " " + this.name + " " + this.membercount + System.lineSeparator() +
				"Date created: " + this.getDateCreated() + System.lineSeparator();
	}

	private String getExtension(File file){
		String extension = "";

		int i = file.getName().lastIndexOf('.');
		if (i >= 0) {
			extension = file.getName().substring(i+1);
		}

		return "."+extension;
	}

}
