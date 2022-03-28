package net;

import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientSideThread implements Runnable {
    private ICallBack<String> callBack = null;
    private String port = "";
    private String address = "";
    private ICallBack<ClientSideThread> onConnection = null;
    private ICallBack<ClientSideThread> onFailed = null;

    private BufferedReader bufferedReader = null;
    private BufferedWriter bufferedWriter = null;

    private String sendMsg = "";
    private String receiveMsg = "";

    private Socket socket = null;

    public ClientSideThread(String address, String port, ICallBack<String> callBack, ICallBack<ClientSideThread> onConnection, ICallBack<ClientSideThread> onFailed) {
        this.callBack = callBack;
        this.port = port;
        this.address = address;
        this.onConnection = onConnection;
        this.onFailed = onFailed;
    }

    public void send(String msg) {
        this.sendMsg = "@@" + msg;
    }

    @Override
    public void run() {
        try {
            System.out.println("running client");
            socket = new Socket(address, Integer.parseInt(port));
            socket.setReuseAddress(true);
            onConnection.invoke(this);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            while (true) {
                if (bufferedReader.ready()) {
                    receiveMsg = bufferedReader.readLine();
                    if (receiveMsg.startsWith("@@")) {
                        receiveMsg = receiveMsg.substring(2);
                        if (callBack != null) {
                            callBack.invoke(receiveMsg);
                            receiveMsg = "";
                        }
                    }
                    if (receiveMsg.startsWith("%%")) {
                        break;
                    }
                }
                if (!sendMsg.isBlank()) {
                    bufferedWriter.write(sendMsg);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    sendMsg = "";
                }

                Thread.sleep(20);
            }

        } catch (InterruptedException e) {
            System.out.println("Interrupted");
            e.printStackTrace();
        } catch (ConnectException e) {
            onFailed.invoke(this);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("port error");
        } finally {
            System.out.println("over");
        }
    }

}
