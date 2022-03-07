public class FileDetail {
    private String creater;
    private String[] accesser;
    private String fileName;
    private String date;//yyyy-MM-dd HH:mm:ss

    public FileDetail(String creater,String[] accesser,String fileName, String date){
        this.creater = creater;
        this.accesser = accesser;
        this.fileName = fileName;
        this.date = date;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String[] getAccesser() {
        return accesser;
    }

    public void setAccesser(String[] accesser) {
        this.accesser = accesser;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
