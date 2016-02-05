package model.list.segment;

import model.MailchimpObject;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by alexanderweiss on 04.02.16.
 */
public class Segment extends MailchimpObject {

    private String name;
    private SegmentType type;
    private String list_id;
    private Date created_at;
    private Date updated_at;
    private int member_count;
    private Options options;

    public Segment(int id, String name, String list_id, SegmentType type, Date created_at, Date updated_at, int member_count, Options options, JSONObject jsonRepresentation){
        super(String.valueOf(id), jsonRepresentation);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SegmentType getType() {
        return type;
    }

    public void setType(SegmentType type) {
        this.type = type;
    }

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public int getMember_count() {
        return member_count;
    }

    public void setMember_count(int member_count) {
        this.member_count = member_count;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }
}
