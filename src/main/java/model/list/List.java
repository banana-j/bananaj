/**
 * @author alexanderweiss
 * @date 06.11.2015
 */
package model.list;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import model.list.segment.Segment;
import org.json.JSONArray;
import org.json.JSONObject;

import connection.MailchimpConnection;
import jxl.CellView;
import jxl.Workbook;
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
	 * Get all members in this list
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Member> getMembers() throws Exception{
		
		ArrayList<Member> members = new ArrayList<Member>();
		final JSONObject list = new JSONObject(getConnection().do_Get(new URL("https://"+connection.getServer()+".api.mailchimp.com/3.0/lists/"+this.getId()+"/members?offset=0&count="+getMembercount())));
		final JSONArray membersArray = list.getJSONArray("members");
		
	    for (int i = 0 ; i < membersArray.length();i++) 
	    {	
	    	final JSONObject memberDetail = membersArray.getJSONObject(i);
	    	final JSONObject memberMergeTags = memberDetail.getJSONObject("merge_fields");
	    	final JSONObject memberStats = memberDetail.getJSONObject("stats");
	    	Member member = new Member(memberDetail.getString("id"),this,memberMergeTags.getString("FNAME"),memberMergeTags.getString("LNAME"),memberDetail.getString("unique_email_id"), memberDetail.getString("email_address"), translateStringIntoMemberStatus(memberDetail.getString("status")),memberDetail.getString("timestamp_signup"),memberDetail.getString("timestamp_opt"),memberStats.getDouble("avg_open_rate"),memberStats.getDouble("avg_click_rate"),memberDetail.getString("last_changed"),this.getConnection(),memberDetail);
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
    	
		return new Member(member.getString("id"),this,memberMergeTags.getString("FNAME"),memberMergeTags.getString("LNAME"),member.getString("unique_email_id"), member.getString("email_address"),  translateStringIntoMemberStatus(member.getString("status")),member.getString("timestamp_signup"),member.getString("timestamp_opt"),memberStats.getDouble("avg_open_rate"),memberStats.getDouble("avg_click_rate"),member.getString("last_changed"),this.getConnection(),member);
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
	 * @param FNAME
	 * @param LNAME
	 * @throws Exception
	 */
	public void addMember(MemberStatus status, String emailAdress, String FNAME, String LNAME) throws Exception{
		URL url = new URL(connection.getLISTENDPOINT()+"/"+this.getId()+"/members");			
		
		JSONObject member = new JSONObject();
		
		JSONObject merge_fields = new JSONObject();
		merge_fields.put("FNAME", FNAME);
		merge_fields.put("LNAME", LNAME);
		
		member.put("status", status.getStringRepresentation());
		member.put("email_address", emailAdress);
		member.put("merge_fields", merge_fields);


        getConnection().do_Post(new URL(connection.getLISTENDPOINT()+"/"+this.getId()+"/members"),member.toString());
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
	 * Writes the data of this list to an excel file in current directory.
	 * @throws Exception
	 */
	public void writeToExcel() throws Exception{
		ArrayList<Member> members = this.getMembers();
		
		WritableWorkbook workbook = Workbook.createWorkbook(new File("listdata_"+this.getId()+"_"+this.getName()+".xls"));
		WritableSheet sheet = workbook.createSheet(this.getName(), 0);
		
		
		WritableFont times16font = new WritableFont(WritableFont.TIMES, 16, WritableFont.BOLD, false); 
		WritableCellFormat times16format = new WritableCellFormat (times16font); 
		
		Label memberIDLabel = new Label(0, 0, "MemberID",times16format);
		Label fnameLabel = new Label(1, 0, "Vorname",times16format);
		Label lnameLabel = new Label(2,0,"Nachname",times16format);
		Label email_addressLabel = new Label(3,0,"Email Addresse",times16format);
		Label timestamp_sign_inLabel = new Label(4,0,"Sign up",times16format);
		Label timestapm_opt_inLabel = new Label(5,0,"Opt in",times16format);
		Label statusLabel = new Label(6,0,"Status",times16format);
		Label avg_open_rateLabel = new Label(7,0,"Avg. open rate",times16format);
		Label avg_click_rateLabel = new Label(8,0,"Avg. click rate",times16format);
		
		sheet.addCell(memberIDLabel);
		sheet.addCell(fnameLabel);
		sheet.addCell(lnameLabel);
		sheet.addCell(email_addressLabel);
		sheet.addCell(timestamp_sign_inLabel);
		sheet.addCell(timestapm_opt_inLabel);
		sheet.addCell(statusLabel);
		sheet.addCell(avg_open_rateLabel);
		sheet.addCell(avg_click_rateLabel);
		
		for(int i = 0 ; i<members.size();i++)
		{
			Member member = members.get(i);
			sheet.addCell(new Label(0,i+1,member.getId()));
			sheet.addCell(new Label(1,i+1,member.getFNAME()));
			sheet.addCell(new Label(2,i+1,member.getLNAME()));
			sheet.addCell(new Label(3,i+1,member.getEmail_address()));
			sheet.addCell(new Label(4,i+1,member.getTimestamp_signup()));
			sheet.addCell(new Label(5,i+1,member.getTimestamp_opt()));
			sheet.addCell(new Label(6,i+1,member.getStatus().getStringRepresentation()));
			sheet.addCell(new Number(7,i+1,member.getAvg_open_rate()));
			sheet.addCell(new Number(8,i+1,member.getAvg_click_rate()));
		}
		
		CellView cell;
		
		for(int x=0;x<9;x++)
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

}
