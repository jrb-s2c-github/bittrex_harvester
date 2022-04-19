/*
 * Last tested 2020/09/24 OpenJDK Runtime Environment
 *   (build 11.0.6+10-post-Ubuntu-1ubuntu118.04.1)
 *
 * Note: This file is intended solely for testing purposes and may only be used
 *   as an example to debug and compare with your code. The 3rd party libraries
 *   used in this example may not be suitable for your production use cases.
 *   You should always independently verify the security and suitability of any
 *   3rd party library used in your code.
 *
 */
package za.co.s2c.cb.bittrex_client;

import com.github.signalr4j.client.ConnectionState;
import com.github.signalr4j.client.hubs.HubConnection;
import com.github.signalr4j.client.hubs.HubProxy;
import com.hazelcast.collection.IQueue;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// TODO split authentication out into different class ?
public class Streamer
{
    static final String URL = "https://socket-v3.bittrex.com/signalr";
    public static final String TICKERS = "Tickers";
    public static final String CANDLE = "CANDLE";
    private static IQueue<String> tickersStreamedFromBittrex;
    private static IQueue<String> candleStreamedFromBittrex;

    static {
        HazelcastInstance hazelcastInstance = Hazelcast.getAllHazelcastInstances().iterator().next();
        tickersStreamedFromBittrex = hazelcastInstance.getQueue("tickers_stream_from_bittrex");
        candleStreamedFromBittrex = hazelcastInstance.getQueue("candles_stream_from_bittrex");
    }

    public void start(String[] channels) throws Exception
    {
        final var client = new SocketClient(URL);
        if (!connect(client)) {
            throw new Exception("Could not connect to server");
        }

        if (!BittrexConstants.API_SECRET.isEmpty()) {
            authenticateClient(client);
        } else {
            System.out.println("Authentication skipped because API key was not provided");
        }

        subscribe(client, channels);
    }

    static Boolean connect(SocketClient client)
    {
        var connected = false;
        try {
            connected = client.connect();
        } catch (Exception e) {
            System.out.println(e);
        }

        if (connected) {
            System.out.println("Connected");
        } else {
            System.out.println("Failed to connect");
        }
        return connected;
    }

    static void authenticateClient(SocketClient client)
    {
        if (authenticate(client, BittrexConstants.API_KEY, BittrexConstants.API_SECRET)) {
            final var scheduler = Executors.newScheduledThreadPool(1);
            var authExpiringHandler = new Object() {
                public void authenticationExpiring() {
                    System.out.println("Authentication expiring...");
                    scheduler.schedule(new Runnable() {
                        @Override
                        public void run() {
                            authenticate(client, BittrexConstants.API_KEY, BittrexConstants.API_SECRET);
                        }
                    }, 1, TimeUnit.SECONDS);
                }
            };
            client.setMessageHandler(authExpiringHandler);
        }
    }

    static Boolean authenticate(SocketClient client, String apiKey, String apiSecret)
    {
        try {
            var response = client.authenticate(apiKey, apiSecret);
            if (response.Success) {
                System.out.println("Authenticated");
            } else {
                System.out.println("Failed to authenticate: " + response.ErrorCode);
            }
            return response.Success;
        } catch (Exception e) {
            System.out.println("Failed to authenticate: " + e.toString());
            return false;
        }
    }

    static void subscribe(SocketClient client, String[] channels)
    {
//        var channels = new String[] { "heartbeat", "trade_BTC-USD", "balance" };


        var msgHandler = new Object() {
            public void heartbeat() {
                System.out.println("<heartbeat>");
            }

            public void trade(String compressedData) {
                // If subscribed to multiple market's trade streams,
                // use the marketSymbol field in the message to differentiate
                sendToHazelcast("Trade", compressedData);
            }

            public void balance(String compressedData) {
                sendToHazelcast("Balance", compressedData);
            }

            public void tickers(String compressedData) {

                sendToHazelcast(TICKERS, compressedData);

            }

            public void candle(String compressedData) {

                sendToHazelcast(CANDLE, compressedData);

            }
        };

        client.setMessageHandler(msgHandler);
        try {
            var response = client.subscribe(channels);
            for (int i = 0; i < channels.length; i++) {
                System.out.println(channels[i] + ": " + (response[i].Success ? "Success" : response[i].ErrorCode));
            }
        } catch (Exception e) {
            System.out.println("Failed to subscribe: " + e.toString());
        }
    }

    static void sendToHazelcast(String msgType, String compressedData)
    {
        var text = "";

        if (msgType.equals(TICKERS)) {
            tickersStreamedFromBittrex.add(compressedData);
        } else if (msgType.equals(CANDLE)) {
            candleStreamedFromBittrex.add(compressedData);
        }
    }
}

class SocketResponse
{
    public Boolean Success;
    public String ErrorCode;

    public SocketResponse(Boolean success, String error)
    {
        Success = success;
        ErrorCode = error;
    }
}

class SocketClient
{
    private String _url;
    private HubConnection _hubConnection;
    private HubProxy _hubProxy;

    public SocketClient(String url)
    {
        _url = url;
        _hubConnection = new HubConnection(_url);
        _hubProxy = _hubConnection.createHubProxy("c3");
    }

    public Boolean connect() throws ExecutionException, InterruptedException
    {
        _hubConnection.start().get();
        return _hubConnection.getState() == ConnectionState.Connected;
    }

    public SocketResponse authenticate(String apiKey, String apiKeySecret)
            throws ExecutionException, InterruptedException
    {
        var timestamp = System.currentTimeMillis();
        var randomContent = UUID.randomUUID().toString();
        var content = "" + timestamp + randomContent.toString();
        var signedContent = "";
        try {
            signedContent = createSignature(apiKeySecret, content);
        } catch (Exception e) {
            return new SocketResponse(false, "COULD_NOT_CREATE_SIGNATURE");
        }
        var result = _hubProxy.invoke(SocketResponse.class, "Authenticate", apiKey, timestamp, randomContent, signedContent)
                .get();
        return result;
    }

    public void setMessageHandler(Object handler)
    {
        _hubProxy.subscribe(handler);
    }

    private static String createSignature(String apiSecret, String data)
            throws InterruptedException, NoSuchAlgorithmException, InvalidKeyException
    {
        var apiKeySpec = new SecretKeySpec(apiSecret.getBytes(), "HmacSHA512");
        var mac = Mac.getInstance("HmacSHA512");
        mac.init(apiKeySpec);
        return toHexString(mac.doFinal(data.getBytes()));
    }

    private static String toHexString(byte[] bytes)
    {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public SocketResponse[] subscribe(String[] channels)
            throws ExecutionException, InterruptedException
    {
        return _hubProxy.invoke(SocketResponse[].class, "Subscribe", (Object) channels).get();
    }
}