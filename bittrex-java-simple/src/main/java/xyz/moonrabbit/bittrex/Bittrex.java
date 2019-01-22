package xyz.moonrabbit.bittrex;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Properties;
import java.util.stream.Collectors;

public class Bittrex {

    private static final String API_VERSION = "1.1";
    private static final String INITIAL_URL = "https://bittrex.com/api/";
    private static final String PUBLIC = "public";
    private static final String MARKET = "market";
    private static final String ACCOUNT = "account";
    private static final String encryptionAlgorithm = "HmacSHA512";
    private static final String GET_OPEN_ORDERS = "getopenorders";
    private static final String WITHDRAW = "withdraw";
    private static final String GET_ORDER_HISTORY = "getorderhistory";
    private static final String GET_WITHDRAWAL_HISTORY = "getwithdrawalhistory";
    private static final String GET_DEPOSIT_HISTORY = "getdeposithistory";

    private final String apikey;
    private final String secret;
    private final int retryAttempts;
    private int retryAttemptsLeft;
    private final int retryDelaySeconds;

    public Bittrex(String apikey, String secret, int retryAttempts, int retryDelaySeconds, InputStream keyFileStream) throws IOException {
        this.retryAttempts = retryAttempts;
        this.retryDelaySeconds = retryDelaySeconds;
        this.retryAttemptsLeft = retryAttempts;

        if (keyFileStream == null) {
            this.apikey = apikey;
            this.secret = secret;
        } else {
            Properties prop = new Properties();
            prop.load(keyFileStream);
            this.apikey = prop.getProperty("apikey");
            this.secret = prop.getProperty("secret");
        }

        if (this.apikey == null) {
            throw new NullPointerException("no apikey");
        }

        if (this.secret == null) {
            throw new NullPointerException("no secret");
        }
    }

    public String getMarkets() throws Exception { // Returns all markets with their metadata
        return getJson(API_VERSION, PUBLIC, "getmarkets");
    }

    public String getCurrencies() throws Exception { // Returns all currencies currently on Bittrex with their metadata
        return getJson(API_VERSION, PUBLIC, "getcurrencies");
    }

    public String getTicker(String market) throws Exception { // Returns current tick values for a specific market
        return getJson(API_VERSION, PUBLIC, "getticker", generateHashMapFromStringList("market", market));
    }

    public String getMarketSummaries() throws Exception { // Returns a 24-hour summary of all markets
        return getJson(API_VERSION, PUBLIC, "getmarketsummaries");
    }

    public String getMarketSummary(String market) throws Exception { // Returns a 24-hour summar for a specific market
        return getJson(API_VERSION, PUBLIC, "getmarketsummary", generateHashMapFromStringList("market", market));
    }

    public String getOrderBook(String market, String type) throws Exception { // Returns the orderbook for a specific market
        return getJson(API_VERSION, PUBLIC, "getorderbook", generateHashMapFromStringList("market", market, "type", type));
    }

    public String getMarketHistory(String market) throws Exception { // Returns latest trades that occurred for a specific market
        return getJson(API_VERSION, PUBLIC, "getmarkethistory", generateHashMapFromStringList("market", market));
    }

    public String buyLimit(String market, String quantity, String rate) throws Exception { // Places a limit buy in a specific market; returns the UUID of the order
        return getJson(API_VERSION, MARKET, "buylimit", generateHashMapFromStringList("market", market, "quantity", quantity, "rate", rate));
    }

    public String buyMarket(String market, String quantity) throws Exception { // Places a market buy in a specific market; returns the UUID of the order
        return getJson(API_VERSION, MARKET, "buymarket", generateHashMapFromStringList("market", market, "quantity", quantity));
    }

    public String sellLimit(String market, String quantity, String rate) throws Exception { // Places a limit sell in a specific market; returns the UUID of the order
        return getJson(API_VERSION, MARKET, "selllimit", generateHashMapFromStringList("market", market, "quantity", quantity, "rate", rate));
    }

    public String sellMarket(String market, String quantity) throws Exception { // Places a market sell in a specific market; returns the UUID of the order
        return getJson(API_VERSION, MARKET, "sellmarket", generateHashMapFromStringList("market", market, "quantity", quantity));
    }

    public String cancelOrder(String uuid) throws Exception { // Cancels a specific order based on its UUID
        return getJson(API_VERSION, MARKET, "cancel", generateHashMapFromStringList("uuid", uuid));
    }

    public String getOpenOrders(String market) throws Exception { // Returns your currently open orders in a specific market
        if (market.equals("")) {
            return getJson(API_VERSION, MARKET, GET_OPEN_ORDERS);
        }

        return getJson(API_VERSION, MARKET, GET_OPEN_ORDERS, generateHashMapFromStringList("market", market));
    }

    public String getOpenOrders() throws Exception { // Returns all your currently open orders
        return getOpenOrders("");
    }

    public String getBalances() throws Exception { // Returns all balances in your account
        return getJson(API_VERSION, ACCOUNT, "getbalances");
    }

    public String getBalance(String currency) throws Exception { // Returns a specific balance in your account
        return getJson(API_VERSION, ACCOUNT, "getbalance", generateHashMapFromStringList("currency", currency));
    }

    public String getDepositAddres(String currency) throws Exception { // Returns the deposit address for a specific currency - if one is not found, it will be generated
        return getJson(API_VERSION, ACCOUNT, "getdepositaddress", generateHashMapFromStringList("currency", currency));
    }

    public String withdraw(String currency, String quantity, String address, String paymentId) throws Exception { // Withdraw a certain amount of a specific coin to an address, and add a payment id
        if (paymentId.equals("")) {
            return getJson(API_VERSION, ACCOUNT, WITHDRAW, generateHashMapFromStringList("currency", currency, "quantity", quantity, "address", address));
        }

        return getJson(API_VERSION, ACCOUNT, WITHDRAW, generateHashMapFromStringList("currency", currency, "quantity", quantity, "address", address, "paymentid", paymentId));
    }

    public String withdraw(String currency, String quantity, String address) throws Exception { // Withdraw a certain amount of a specific coin to an address
        return withdraw(currency, quantity, address, "");
    }

    public String getOrder(String uuid) throws Exception { // Returns information about a specific order (by UUID)
        return getJson(API_VERSION, ACCOUNT, "getorder", generateHashMapFromStringList("uuid", uuid));
    }

    public String getOrderHistory(String market) throws Exception { // Returns your order history for a specific market
        if (market.equals("")) {
            return getJson(API_VERSION, ACCOUNT, GET_ORDER_HISTORY);
        }

        return getJson(API_VERSION, ACCOUNT, GET_ORDER_HISTORY, generateHashMapFromStringList("market", market));
    }

    public String getOrderHistory() throws Exception { // Returns all of your order history
        return getOrderHistory("");
    }

    public String getWithdrawalHistory(String currency) throws Exception { // Returns your withdrawal history for a specific currency
        if (currency.equals("")) {
            return getJson(API_VERSION, ACCOUNT, GET_WITHDRAWAL_HISTORY);
        }

        return getJson(API_VERSION, ACCOUNT, GET_WITHDRAWAL_HISTORY, generateHashMapFromStringList("currency", currency));
    }

    public String getWithdrawalHistory() throws Exception { // Returns all of your withdrawal history
        return getWithdrawalHistory("");
    }

    public String getDepositHistory(String currency) throws Exception { // Returns your deposit history for a specific currency
        if (currency.equals("")) {
            return getJson(API_VERSION, ACCOUNT, GET_DEPOSIT_HISTORY);
        }

        return getJson(API_VERSION, ACCOUNT, GET_DEPOSIT_HISTORY, generateHashMapFromStringList("currency", currency));
    }

    public String getDepositHistory() throws Exception { // Returns all of your deposit history
        return getDepositHistory("");
    }

    private HashMap<String, String> generateHashMapFromStringList(String... strings) throws Exception { // Method to easily create a HashMap from a list of Strings
        if (strings.length % 2 != 0) {
            throw new Exception("Must be in key-value pairs");
        }

        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < strings.length; i += 2) { // Each key will be i, with the following becoming its value
            map.put(strings[i], strings[i + 1]);
        }

        return map;
    }

    private String getJson(String apiVersion, String type, String method) throws Exception {
        return getResponseBody(generateUrl(apiVersion, type, method));
    }

    private String getJson(String apiVersion, String type, String method, HashMap<String, String> parameters) throws Exception {
        return getResponseBody(generateUrl(apiVersion, type, method, parameters));
    }

    private String generateUrl(String apiVersion, String type, String method) {
        return generateUrl(apiVersion, type, method, new HashMap<>());
    }

    private String generateUrl(String apiVersion, String type, String method, HashMap<String, String> parameters) {
        String url = INITIAL_URL;

        url += "v" + apiVersion + "/";
        url += type + "/";
        url += method;
        url += generateUrlParameters(parameters);

        return url;
    }

    private String generateUrlParameters(HashMap<String, String> parameters) { // Returns a String with the key-value pairs formatted for URL
        return parameters.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&", "?", ""));
    }

    private String getResponseBody(final String baseUrl) throws Exception {
        String result = null;
        String urlString = baseUrl + "&apikey=" + apikey + "&nonce=" + BittrexUtil.generateNonce();

        try {
            URL url = new URL(urlString);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            String apiSign = BittrexUtil.calculateHash(secret, urlString, encryptionAlgorithm);
            httpsURLConnection.setRequestProperty("apisign", apiSign);

            System.out.println("URL: [" + urlString + "]. ApiSign: [" + apiSign + "]");
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));

            StringBuilder resultBuffer = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                resultBuffer.append(line);
            }

            result = resultBuffer.toString();

        } catch (UnknownHostException | SocketException e) {
            if (retryAttemptsLeft-- > 0) {
                System.err.printf("Could not connect to host - retrying in %d seconds... [%d/%d]\n",
                        retryDelaySeconds, retryAttempts - retryAttemptsLeft, retryAttempts);

                BittrexUtil.sleepSilent(retryDelaySeconds);
                result = getResponseBody(baseUrl);
            } else {
                throw new Exception("Maximum amount of attempts to connect to host exceeded.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            retryAttemptsLeft = retryAttempts;
        }

        return result;
    }
}