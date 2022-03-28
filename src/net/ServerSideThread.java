package net;

import gui.App;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class ServerSideThread implements Runnable {

    private ICallBack<String> callBack = null;
    private ICallBack<ServerSideThread> onConnection = null;
    private String port = "";

    private BufferedReader bufferedReader = null;
    private BufferedWriter bufferedWriter = null;

    private String sendMsg = "";
    private String receiveMsg = "";

    private ServerSocket serverSocket = null;
    private Socket socket = null;
    public ServerSideThread(String port, ICallBack<String> callBack, ICallBack<ServerSideThread> onConnection) {
        this.callBack = callBack;
        this.onConnection = onConnection;
        this.port = port;
    }

    public void send(String msg) {
        this.sendMsg = "@@" + msg;
    }

    public void close() {
        this.sendMsg = "%%";
    }

    public void cancel() throws IOException {
        if (socket != null){
            socket.close();
        }
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(Integer.parseInt(port));
            serverSocket.setReuseAddress(true);
            Socket socket = serverSocket.accept();
            System.out.println("connected");
            socket.setReuseAddress(true);
            this.onConnection.invoke(this);
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
                    //服务端不会收到关闭请求
//                        if (receiveMsg.startsWith("%%")){
//                            break;
//                        }
                }
                if (!sendMsg.isBlank()) {
                    bufferedWriter.write(sendMsg);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    if (sendMsg.startsWith("%%")) {
                        socket.close();
                        sendMsg = "";
                        break;
                    }
                    sendMsg = "";
                }

                Thread.sleep(20);
            }
            serverSocket.close();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
            e.printStackTrace();
        }catch (BindException e){
            App.getInstance().showMessage(Alert.AlertType.ERROR,"出错了",
                    "创建socket太频繁啦","尝试重启游戏看看吧");
        }
        catch (SocketException e) {
            System.out.println("server socket closed");
        } catch (IOException e){
            System.out.println("port error");
        }
        finally {
            System.out.println("over");
        }
    }
}
