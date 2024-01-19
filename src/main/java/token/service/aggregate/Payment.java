package token.service.aggregate;

public class Payment {


    int amount;
    Token token;
    AccountId merchantId;
    AccountId customerId;
    PaymentId paymentId;

    public Payment() {
    }

    public int getAmount() {
        return amount;
    }


    public AccountId getMerchantId() {
        return merchantId;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public void setMerchantId(AccountId merchantId) {
        this.merchantId = merchantId;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public AccountId getCustomerId() {
        return customerId;
    }

    public void setCustomerId(AccountId customerId) {
        this.customerId = customerId;
    }

    public PaymentId getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(PaymentId paymentId) {
        this.paymentId = paymentId;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Payment) {
            Payment other = (Payment) obj;
            return other.getPaymentId().equals(this.getPaymentId());
        }
        return false;
    }
}
