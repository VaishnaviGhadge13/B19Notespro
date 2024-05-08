package easy.tuto.notespro;

public class Reminder {
    private String title;
    private String date;
    private String time;
    private String repeat;
    private String repeatNo;
    private String repeatType;
    private String active;

    private String rid;
    public Reminder() {
        // Default constructor required for Firestore
    }

    public Reminder(String rid,String title, String date, String time, String repeat, String repeatNo, String repeatType, String active) {
        this.rid=rid;
        this.title = title;
        this.date = date;
        this.time = time;
        this.repeat = repeat;
        this.repeatNo = repeatNo;
        this.repeatType = repeatType;
        this.active = active;
    }

    public Reminder(String reminderId, String title) {
        this.rid=reminderId;
        this.title=title;
    }

    public String getId()
    {
        return rid;
    }
    public void setId(String rid)
    {
        this.rid=rid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getRepeatNo() {
        return repeatNo;
    }

    public void setRepeatNo(String repeatNo) {
        this.repeatNo = repeatNo;
    }

    public String getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
