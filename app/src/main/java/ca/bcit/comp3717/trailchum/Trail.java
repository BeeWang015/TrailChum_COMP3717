package ca.bcit.comp3717.trailchum;

public class Trail {

    private String COMPKEY;

    private String ADDRQUAL;

    private String MATERIAL;

    private String TRAILCLASS;

    private String STAIRS;

    private String AREAWID;

    private String AREALEN;

    private String PATHNAME;

    public void setCOMPKEY(String COMPKEY){
        this.COMPKEY = COMPKEY;
    }
    public String getCOMPKEY(){
        return this.COMPKEY;
    }
    public void setPATHNAME(String PATHNAME) {
        this.PATHNAME = PATHNAME;
    }
    public String getPATHNAME() {
        return this.PATHNAME;
    }
    public void setADDRQUAL(String ADDRQUAL){
        this.ADDRQUAL = ADDRQUAL;
    }
    public String getADDRQUAL(){
        return this.ADDRQUAL;
    }
    public void setMATERIAL(String MATERIAL){
        this.MATERIAL = MATERIAL;
    }
    public String getMATERIAL(){
        return this.MATERIAL;
    }
    public void setTRAILCLASS(String TRAILCLASS){
        this.TRAILCLASS = TRAILCLASS;
    }
    public String getTRAILCLASS(){
        return this.TRAILCLASS;
    }
    public void setSTAIRS(String STAIRS){
        this.STAIRS = STAIRS;
    }
    public String getSTAIRS(){
        return this.STAIRS;
    }
    public void setAREAWID(String AREAWID){
        this.AREAWID = AREAWID;
    }
    public String getAREAWID(){
        return this.AREAWID;
    }
    public void setAREALEN(String AREALEN){
        this.AREALEN = AREALEN;
    }
    public String getAREALEN(){
        return this.AREALEN;
    }
}
