/*
 * AllShell.java - Remote shell multi platform.
 *
 * This tool, is a multiplatform remote shell.
 *
 * Copyright (C) 2017 Luca Petrocchi <petrocchi@myoffset.me>
 *
 * DATE:	04/05/2017
 * AUTHOR:	Luca Petrocchi
 * EMAIL:	petrocchi@myoffset.me
 * WEBSITE	https://myoffset.me/
 * URL:		https://github.com/petrocchi
 *
 */

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class AllShell {

    public static void shell(int port) throws Exception {
        InetAddress host = null;

        try {
            host = InetAddress.getByName(null);
        }
        catch (UnknownHostException e) {
            System.err.println(e);
        }

        ServerSocket sSockfd = new ServerSocket(port);
        System.out.println("[+] Remote shell listen on " + port);

        while (true) {
            PrintWriter outw = null;
            
            try {
                Socket cSockfd = sSockfd.accept();

                Scanner in = new Scanner(cSockfd.getInputStream());
                String command = null;

                outw = new PrintWriter(cSockfd.getOutputStream());

                outw.print("[+] Welcome to remote shell!\n[+] Type \"quit\" close connection.\nShell> ");
                outw.flush();

                while (true) {
                    command = in.nextLine();
                    if (command.equals("quit")) {
                        break;
                    } else {
                        exec(outw, command);
                        
                        outw.print("\nShell> ");
                        outw.flush();
                    }
                }

                in.close();
                outw.close();
                cSockfd.close();
            }
            catch (UnknownHostException e) {
                System.err.println(e);
            }
            catch (Exception e) {
                outw.println(e);
                outw.flush();
            }
        }
    }

    public static void exec(PrintWriter outw, String command) throws Exception {
        Scanner in = null;
        
        try {
            Process mycommand = Runtime.getRuntime().exec(command);
            in = new Scanner(mycommand.getInputStream());

            while (in.hasNextLine()) {
                outw.println(in.nextLine());
                outw.flush();
            }

            mycommand.destroy();
            in.close();
        }
        catch(Exception e) {
            outw.println(e);
            outw.flush();
        }
    }
    
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("[+] java allShell <listen port>");
            return;
        }
        
        shell(Integer.parseInt(args[0]));
        
    }
}
