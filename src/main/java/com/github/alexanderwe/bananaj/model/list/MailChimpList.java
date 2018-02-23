/**
 * @author alexanderweiss
 * @date 06.11.2015
 */
package com.github.alexanderwe.bananaj.model.list;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;
import com.github.alexanderwe.bananaj.exceptions.EmailException;
import com.github.alexanderwe.bananaj.exceptions.FileFormatException;
import jxl.*;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Number;
import com.github.alexanderwe.bananaj.model.MailchimpObject;
import com.github.alexanderwe.bananaj.model.list.member.Member;
import com.github.alexanderwe.bananaj.model.list.member.MemberStatus;
import com.github.alexanderwe.bananaj.model.list.mergefield.MergeField;
import com.github.alexanderwe.bananaj.model.list.mergefield.MergeFieldOptions;
import com.github.alexanderwe.bananaj.model.list.segment.*;
import org.json.JSONArray;
import org.json.JSONObject;
import com.github.alexanderwe.bananaj.utils.DateConverter;
import com.github.alexanderwe.bananaj.utils.EmailValidator;
import com.github.alexanderwe.bananaj.utils.FileInspector;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Class for representing a mailchimp list. 
 * @author alexanderweiss
 *
 */
public class MailChimpList extends MailchimpObject {

	private String name;
	private int membercount;
	private LocalDateTime dateCreated;
	private MailChimpConnection connection;
	

	public MailChimpList(String id, String name, int membercount, LocalDateTime dateCreated, MailChimpConnection connection, JSONObject jsonRepresentation){
		super(id,jsonRepresentation);
		this.name = name;
		this.membercount = membercount;
		this.dateCreated = dateCreated;
		this.connection = connection;
	}

	/**
	 * Get all members in this list
	 * @param count x first members
	 * @param offset skip x first members
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Member> getMembers(int count, int offset) throws Exception{

		ArrayList<Member> members = new ArrayList<Member>();
		final JSONObject list;
		if(count != 0){
			list = new JSONObject(getConnection().do_Get(new URL("https://"+connection.getServer()+".api.mailchimp.com/3.0/lists/"+this.getId()+"/members?count="+count+"&offset="+offset),connection.getApikey()));
		} else {
			list = new JSONObject(getConnection().do_Get(new URL("https://"+connection.getServer()+".api.mailchimp.com/3.0/lists/"+this.getId()+"/members?count="+this.getMembercount()+"&offset="+offset),connection.getApikey()));
		}

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
				Object value = memberMergeTags.get(key);
				merge_fields.put(key, value);
			}
			Member member = new Member(memberDetail.getString("id"),this,merge_fields,memberDetail.getString("unique_email_id"), memberDetail.getString("email_address"), MemberStatus.valueOf(memberDetail.getString("status").toUpperCase()),memberDetail.getString("timestamp_signup"),memberDetail.getString("ip_signup"),memberDetail.getString("timestamp_opt"),memberDetail.getString("ip_opt"),memberStats.getDouble("avg_open_rate"),memberStats.getDouble("avg_click_rate"),memberDetail.getString("last_changed"),this.getConnection(),memberDetail);
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
		final JSONObject member = new JSONObject(getConnection().do_Get(new URL("https://"+connection.getServer()+".api.mailchimp.com/3.0/lists/"+getId()+"/members/"+memberID),connection.getApikey()));
    	final JSONObject memberMergeTags = member.getJSONObject("merge_fields");
    	final JSONObject memberStats = member.getJSONObject("stats");

		HashMap<String, Object> merge_fields = new HashMap<String, Object>();

		Iterator a = memberMergeTags.keys();
		while(a.hasNext()) {
			String key = (String)a.next();
			// loop to get the dynamic key
			String value = (String)memberMergeTags.get(key);
			merge_fields.put(key, value);
		}
		return new Member(member.getString("id"),this,merge_fields,member.getString("unique_email_id"), member.getString("email_address"),  MemberStatus.valueOf(member.getString("status").toUpperCase()),member.getString("timestamp_signup"),member.getString("ip_signup"),member.getString("timestamp_opt"),member.getString("ip_opt"),memberStats.getDouble("avg_open_rate"),memberStats.getDouble("avg_click_rate"),member.getString("last_changed"),this.getConnection(),member);
	}
	
	/**
	 * Add a member with the minimum of information
	 * @param status
	 * @param emailAddress
	 */
	public void addMember(MemberStatus status, String emailAddress) throws Exception{
		JSONObject member = new JSONObject();
		member.put("status", status.getStringRepresentation());
		member.put("email_address", emailAddress);

        getConnection().do_Post(new URL(connection.getListendpoint()+"/"+this.getId()+"/members"),member.toString(),connection.getApikey());
        this.membercount++;
	}
	
	/**
	 * Add a member with first and last name
	 * @param status
	 * @param emailAddress
	 * @param merge_fields_values
	 * @throws Exception
	 */
	public void addMember(MemberStatus status, String emailAddress, HashMap<String, Object> merge_fields_values) throws Exception{
		URL url = new URL(connection.getListendpoint()+"/"+this.getId()+"/members");
		
		JSONObject member = new JSONObject();
		JSONObject merge_fields = new JSONObject();

		Iterator it = merge_fields_values.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			it.remove(); // avoids a ConcurrentModificationException
			merge_fields.put(pair.getKey().toString(), pair.getValue());
		}
		
		member.put("status", status.getStringRepresentation());
		member.put("email_address", emailAddress);
		member.put("merge_fields", merge_fields);
        getConnection().do_Post(new URL(connection.getListendpoint()+"/"+this.getId()+"/members"),member.toString(),connection.getApikey());
		this.membercount++;
	}

	public void importMembersFromFile(File file) throws FileFormatException, IOException{
		//TODO fully implement read from xls
		String extension = FileInspector.getInstance().getExtension(file);

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
		getConnection().do_Delete(new URL(connection.getListendpoint()+"/"+getId()+"/members/"+memberID),connection.getApikey());
		this.membercount--;
	}
	
	/**
	 * Get the growth history of this list
	 * @return a growth history
	 * @throws Exception
	 */
	public GrowthHistory getGrowthHistory() throws Exception{
		final JSONObject growth_history = new JSONObject(getConnection().do_Get(new URL(connection.getListendpoint()+"/"+this.getId()+"/growth-history"),connection.getApikey()));
    	final JSONArray history = growth_history.getJSONArray("history");
    	final JSONObject historyDetail = history.getJSONObject(0);
    	
    	return new GrowthHistory(this, historyDetail.getString("month"), historyDetail.getInt("existing"), historyDetail.getInt("imports"), historyDetail.getInt("optins"));
	}

	/**
	 * Get all segments of this list
	 * @return
	 * @throws Exception
     */
	public ArrayList<Segment> getSegments() throws Exception {
        ArrayList<Segment> segments = new ArrayList<Segment>();
		JSONObject jsonSegments = new JSONObject(connection.do_Get(new URL(connection.getListendpoint()+"/"+this.getId()+"/segments") ,connection.getApikey()));

		final JSONArray segmentsArray = jsonSegments.getJSONArray("segments");

		for (int i = 0; i<segmentsArray.length(); i++){
			final JSONObject segmentDetail = segmentsArray.getJSONObject(i);
			Segment segment;

			//Extract options and conditions
			if (segmentDetail.getString("type").equals(SegmentType.STATIC.getStringRepresentation())) {  //IF SEGMENT IS STATIC NO OPTION FIELD IS PROVIDED
				segment = new Segment(
						segmentDetail.getInt("id"),
						segmentDetail.getString("name"),
						segmentDetail.getString("list_id"),
						SegmentType.valueOf(segmentDetail.getString("type").toUpperCase()),
						DateConverter.getInstance().createDateFromISO8601(segmentDetail.getString("created_at")),
						DateConverter.getInstance().createDateFromISO8601(segmentDetail.getString("updated_at")),
						segmentDetail.getInt("member_count"),
						this.getConnection(),
						segmentDetail);

			} else {
				MatchType matchType = MatchType.valueOf(segmentDetail.getJSONObject("options").getString("match").toUpperCase());

				JSONArray jsonConditions = segmentDetail.getJSONObject("options").getJSONArray("conditions");

				ArrayList<Condition> conditions = new ArrayList<>();
				for (int j = 0; j<jsonConditions.length();j++){
					JSONObject jsonCondition = jsonConditions.getJSONObject(j);

					conditions.add( new Condition.Builder()
									.field(jsonCondition.getString("field"))
									.operator(Operator.valueOf(jsonCondition.getString("op").toUpperCase()))
									.value(jsonCondition.getString("value"))
									.build());

				}

				segment = new Segment(
						segmentDetail.getInt("id"),
						segmentDetail.getString("name"),
						segmentDetail.getString("list_id"),
						SegmentType.valueOf(segmentDetail.getString("type").toUpperCase()),
						DateConverter.getInstance().createDateFromISO8601(segmentDetail.getString("created_at")),
						DateConverter.getInstance().createDateFromISO8601(segmentDetail.getString("updated_at")),
						segmentDetail.getInt("member_count"),
						new Options(matchType, conditions),
						this.getConnection(),
						segmentDetail);
			}

			segments.add(segment);
		}

        return segments;
    }

	/**
	 * Get a specific segment of this list
	 * @param segmentID
	 * @return
	 * @throws Exception
	 */
	public Segment getSegment(String segmentID) throws Exception {
		JSONObject jsonSegment = new JSONObject(connection.do_Get(new URL(connection.getListendpoint()+"/"+this.getId()+"/segments/"+segmentID) ,connection.getApikey()));

		//Extract options and conditions
		MatchType matchType = MatchType.valueOf(jsonSegment.getJSONObject("options").getString("match").toUpperCase());

		JSONArray jsonConditions = jsonSegment.getJSONObject("options").getJSONArray("conditions");
		ArrayList<Condition> conditions = new ArrayList<>();
		for (int i = 0; i<jsonConditions.length();i++){
			JSONObject jsonCondition = jsonConditions.getJSONObject(i);

			conditions.add( new Condition.Builder()
					.field(jsonCondition.getString("field"))
					.operator(Operator.valueOf(jsonCondition.getString("op").toUpperCase()))
					.value(jsonCondition.getString("value"))
					.build());
		}

		return new Segment(
				jsonSegment.getInt("id"),
				jsonSegment.getString("name"),
				jsonSegment.getString("list_id"),
				SegmentType.valueOf(jsonSegment.getString("type").toUpperCase()),
				DateConverter.getInstance().createDateFromISO8601(jsonSegment.getString("created_at")),
				DateConverter.getInstance().createDateFromISO8601(jsonSegment.getString("updated_at")),
				jsonSegment.getInt("member_count"),
				new Options(matchType,conditions),
				this.getConnection(),
				jsonSegment);
	}

	/**
	 * Add a segment to the list
	 * @param name
	 * @throws Exception
	 */
	public void addSegment(String name,Options option) throws Exception {
		JSONObject segment = new JSONObject();
		segment.put("name", name);

		segment.put("options",option.getJsonRepresentation());
		System.out.println(segment.toString());

		getConnection().do_Post(new URL(connection.getListendpoint()+"/"+this.getId()+"/segments"),segment.toString(),connection.getApikey());
	}


	/**
	 * Add a static segment with a name and predefined emails to this list.
	 * Every E-Mail address which is not present on the list itself will be ignored and not added to the static segment.
	 * @param name
	 * @param emails
	 * @throws Exception
	 */
	public void addStaticSegment(String name, String [] emails) throws Exception {
		JSONObject segment = new JSONObject();
		segment.put("name", name);
		for (String email : emails){
			if(!EmailValidator.getInstance().validate(email)){
				throw new EmailException(email);
			}
		}
		segment.put("static_segment", emails);
		getConnection().do_Post(new URL(connection.getListendpoint()+"/"+this.getId()+"/segments"),segment.toString(),connection.getApikey());

	}

	/**
	 * Delete a specific segment
	 * @param segmentId
	 * @throws Exception
	 */
	public void deleteSegment(String segmentId) throws Exception{
		getConnection().do_Delete(new URL(connection.getListendpoint()+"/"+this.getId()+"/segments/"+segmentId),connection.getApikey());
	}

	/**
	 * Get a list of all merge fields of this list
	 * @return
	 * @throws Exception
	 */
	public ArrayList<MergeField> getMergeFields() throws Exception {
		ArrayList<MergeField> mergeFields = new ArrayList<MergeField>();
		URL url = new URL(connection.getListendpoint()+"/"+this.getId()+"/merge-fields");

		JSONObject merge_fields = new JSONObject(connection.do_Get(url,connection.getApikey()));
		final JSONArray mergeFieldsArray = merge_fields.getJSONArray("merge_fields");

		for (int i = 0 ; i < mergeFieldsArray.length(); i++) {
			final JSONObject mergeFieldDetail = mergeFieldsArray.getJSONObject(i);

			final JSONObject mergeFieldOptionsJSON = mergeFieldDetail.getJSONObject("options");
			MergeFieldOptions mergeFieldOptions = new MergeFieldOptions();

			switch(mergeFieldDetail.getString("type")){
				case "address":mergeFieldOptions.setDefault_country(mergeFieldOptionsJSON.getInt("default_country"));break;
				case "phone":mergeFieldOptions.setPhone_format(mergeFieldOptionsJSON.getString("phone_format"));break;
				case "date":mergeFieldOptions.setDate_format(mergeFieldOptionsJSON.getString("date_format"));break;
				case "birthday":mergeFieldOptions.setDate_format(mergeFieldOptionsJSON.getString("date_format"));break;
				case "text":mergeFieldOptions.setSize(mergeFieldOptionsJSON.getInt("size"));break;
				case "radio":
					JSONArray mergeFieldOptionChoicesRadio = mergeFieldOptionsJSON.getJSONArray("choices");
					ArrayList<String> choicesRadio = new ArrayList<String>();
					for (int j = 0; j < mergeFieldOptionChoicesRadio.length(); j++){
						choicesRadio.add((String )mergeFieldOptionChoicesRadio.get(j));
					}
					mergeFieldOptions.setChoices(choicesRadio);
					break;
				case "dropdown":
					JSONArray mergeFieldOptionChoicesDropdown = mergeFieldOptionsJSON.getJSONArray("choices");
					ArrayList<String> choicesDropdown = new ArrayList<String>();
					for (int j = 0; j < mergeFieldOptionChoicesDropdown.length(); j++){
						choicesDropdown.add((String )mergeFieldOptionChoicesDropdown.get(j));
					}
					mergeFieldOptions.setChoices(choicesDropdown);
					break;
			}


			MergeField mergeField = new MergeField(
					String.valueOf(mergeFieldDetail.getInt("merge_id")),
					mergeFieldDetail.getString("tag"),
					mergeFieldDetail.getString("name"),
					mergeFieldDetail.getString("type"),
					mergeFieldDetail.getBoolean("required"),
					mergeFieldDetail.getString("default_value"),
					mergeFieldDetail.getBoolean("public"),
					mergeFieldDetail.getString("list_id"),
					mergeFieldOptions,
					mergeFieldDetail
			);
			mergeFields.add(mergeField);
		}
		return mergeFields;
	}

	/**
	 * Get a specific merge field of this list
	 * @param mergeFieldID
	 * @return
	 */
	public MergeField getMergeField(String mergeFieldID) throws Exception{
		URL url = new URL(connection.getListendpoint()+"/"+this.getId()+"/merge-fields/"+mergeFieldID);
		JSONObject mergeFieldJSON = new JSONObject(connection.do_Get(url,connection.getApikey()));

		final JSONObject mergeFieldOptionsJSON = mergeFieldJSON.getJSONObject("options");
		MergeFieldOptions mergeFieldOptions = new MergeFieldOptions();

		switch(mergeFieldJSON.getString("type")){
			case "address":mergeFieldOptions.setDefault_country(mergeFieldOptionsJSON.getInt("default_country"));break;
			case "phone":mergeFieldOptions.setPhone_format(mergeFieldOptionsJSON.getString("phone_format"));break;
			case "date":mergeFieldOptions.setDate_format(mergeFieldOptionsJSON.getString("date_format"));break;
			case "birthday":mergeFieldOptions.setDate_format(mergeFieldOptionsJSON.getString("date_format"));break;
			case "text":mergeFieldOptions.setSize(mergeFieldOptionsJSON.getInt("size"));break;
			case "radio":
				JSONArray mergeFieldOptionChoicesRadio = mergeFieldOptionsJSON.getJSONArray("choices");
				ArrayList<String> choicesRadio = new ArrayList<String>();
				for (int j = 0; j < mergeFieldOptionChoicesRadio.length(); j++){
					choicesRadio.add((String )mergeFieldOptionChoicesRadio.get(j));
				}
				mergeFieldOptions.setChoices(choicesRadio);
				break;
			case "dropdown":
				JSONArray mergeFieldOptionChoicesDropdown = mergeFieldOptionsJSON.getJSONArray("choices");
				ArrayList<String> choicesDropdown = new ArrayList<String>();
				for (int j = 0; j < mergeFieldOptionChoicesDropdown.length(); j++){
					choicesDropdown.add((String )mergeFieldOptionChoicesDropdown.get(j));
				}
				mergeFieldOptions.setChoices(choicesDropdown);
				break;
		}




		 return new MergeField(
				String.valueOf(mergeFieldJSON.getInt("merge_id")),
				mergeFieldJSON.getString("tag"),
				mergeFieldJSON.getString("name"),
				mergeFieldJSON.getString("type"),
				mergeFieldJSON.getBoolean("required"),
				mergeFieldJSON.getString("default_value"),
				mergeFieldJSON.getBoolean("public"),
				mergeFieldJSON.getString("list_id"),
				mergeFieldOptions,
				mergeFieldJSON
		);
	}

	public void addMergeField(MergeField mergeFieldtoAdd){

	}


	public void deleteMergeField(String mergeFieldID) throws Exception{
		connection.do_Delete(new URL(connection.getListendpoint()+"/"+this.getId()+"/merge-fields/"+mergeFieldID),connection.getApikey());
	}
	/**
	 * Writes the data of this list to an excel file in current directory. Define whether to show merge fields or not
	 * @param show_merge
	 * @throws Exception
	 */
	public void writeToExcel(String filepath,boolean show_merge) throws Exception{
		ArrayList<Member> members = this.getMembers(0,0);
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the membercount
	 */
	public int getMembercount() {
		return membercount;
	}

	/**
	 * @return the dateCreated
	 */
	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	/**
	 *
	 * @return the MailChimp com.github.alexanderwe.bananaj.connection
	 */
	public MailChimpConnection getConnection(){
		return this.connection;
	}

	@Override
	public String toString(){
		return this.getId() + " " + this.name + " " + this.membercount + System.lineSeparator() +
				"Date created: " + this.getDateCreated() + System.lineSeparator();
	}


}
