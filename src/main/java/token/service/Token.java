package token.service;
/**
 * @Author: Rasmus Bo Thomsen
 * Mob programming, all members
 */
public class Token {
    String rfid;
    public Token() {
    }
    public Token(String rfid) {
        this.rfid = rfid;
    }
    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }
}
