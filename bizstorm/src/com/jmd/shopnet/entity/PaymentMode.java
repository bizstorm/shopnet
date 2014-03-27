package com.jmd.shopnet.entity;

public enum PaymentMode {
	Cash("Cash"),
	Check("Check"),
	CrediCard("CrediCard"),
	DebitCard("DebitCard"),
	NetBanking("NetBanking"),
	AmexCard("AmexCard"),
	GiftCard("GiftCard"),
	Voucher("Voucher");

    private PaymentMode(final String text) {
        this.text = text;
    }

    private final String text;

    @Override
    public String toString() {
        return text;
    }

	public static PaymentMode getByText(String text) {
		String trimmed = text.replaceAll("\\s+","");
		if(Cash.text.equalsIgnoreCase(trimmed)) {
			return Cash;
		} else if(Check.text.equalsIgnoreCase(trimmed)) {
			return Check;
		} else if(CrediCard.text.equalsIgnoreCase(trimmed)) {
			return CrediCard;
		} else if(DebitCard.text.equalsIgnoreCase(trimmed)) {
			return DebitCard;
		} else if(NetBanking.text.equalsIgnoreCase(trimmed)) {
			return NetBanking;
		} else if(AmexCard.text.equalsIgnoreCase(trimmed)) {
			return AmexCard;
		} else if(GiftCard.text.equalsIgnoreCase(trimmed)) {
			return GiftCard;
		} else if(Voucher.text.equalsIgnoreCase(trimmed)) {
			return Voucher;
		} else {
			throw new IllegalArgumentException("Invalid PaymentMode: " + text);
		}
	}
}
