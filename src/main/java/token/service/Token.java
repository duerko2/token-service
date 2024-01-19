package token.service;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Token)) {
            return false;
        }
        var c = (Token) o;

        // check that account id is same
        return c.getRfid().equals(this.rfid);
    }
}
