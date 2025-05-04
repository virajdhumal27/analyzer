package com.stocks.analyzer.constants;

public enum Symbols {
    AMZN(CompanyName.AMAZON),
    AAPL(CompanyName.APPLE),
    DBD(CompanyName.DIEBOLD),
    GOOGL(CompanyName.GOOGLE),
    HDB(CompanyName.HDFC),
    IBM(CompanyName.IBM),
    UNSPECIFIED(CompanyName.UNSPECIFIED);

    private final String companyName;

    Symbols(String companyName) {
        this.companyName = companyName;
    }

    public static Symbols getSymbolByName(String requestedSymbol) {
        try {
            return Symbols.valueOf(requestedSymbol);
        } catch (IllegalArgumentException e) {
            return UNSPECIFIED;
        }
    }

    /**
     * Returns the symbol from company name, if no symbol present then returns <code>UNSPECIFIED</code>.
     *
     * @param companyName of type <code>String</code>.
     * @return Enum of <code>Symbol</code> matching with company name
     */
    public static Symbols getSymbol(String companyName) {
        for (Symbols symbol : values()) {
            if (symbol.companyName.equals(companyName)) {
                return symbol;
            }
        }
        return UNSPECIFIED;
    }

    /**
     * Returns the company name of symbol.
     *
     * @param symbol of type enum <code>Symbol</code>.
     * @return Company name in <code>String</code> from symbol.
     */
    public static String getCompanyName(Symbols symbol) {
        return symbol.companyName;
    }

    public static String getSymbolName(Symbols symbol) {
        return symbol.name();
    }

    private static class CompanyName {
        public static final String AMAZON = "AMAZON";
        public static final String APPLE = "APPLE";
        public static final String DIEBOLD = "DIEBOLD";
        public static final String GOOGLE = "GOOGLE";
        public static final String HDFC = "HDFC";
        public static final String IBM = "IBM";
        public static final String UNSPECIFIED = "UNSPECIFIED";
    }
}
