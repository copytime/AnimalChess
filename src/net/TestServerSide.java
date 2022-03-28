package net;

import java.util.Scanner;

public class TestServerSide {
    public static void main(String[] args) {
        ServerSideThread serverSideThread = new ServerSideThread("2333", System.out::println,null);
        Thread server = new Thread(serverSideThread);
        server.start();
        Scanner scanner = new Scanner(System.in);
        String msg;
        while (!(msg = scanner.nextLine()).equals("end")) {
            System.out.println("sending");
            serverSideThread.send(msg);
        }
        serverSideThread.close();
    }


}
