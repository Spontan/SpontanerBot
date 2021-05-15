package twitch;


import java.net.*;
import java.io.*;
import java.util.*;

public class IrcSenderSimple {
    private static void sendString(BufferedWriter bw, String str) {
        try {
            bw.write(str + "\r\n");
            bw.flush();
        }
        catch (Exception e) {
            System.out.println("Exception: "+e);
        }
    }

    public static void test(String nick, String pass) {
        try {

            String server   = "irc.chat.twitch.tv";
            int port        = 6667;
            String nickname = nick;
            String password = pass;
            String channel  = "#bot-test-ch";
            String message  = "hi, all";

            Socket socket = new Socket(server,port);
            System.out.println("*** Connected to server.");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            InputStream inStream = socket.getInputStream();
            System.out.println("*** Opened OutputStreamWriter.");
            BufferedWriter bwriter = new BufferedWriter(outputStreamWriter);
            System.out.println("*** Opened BufferedWriter.");


            sendString(bwriter, "PASS " + password);
            sendString(bwriter,"NICK "+nickname);

            Thread readThread = new Thread(){

                public void run(){
                    while(!this.isInterrupted()) {
                        Scanner inReader = new Scanner(inStream);

                        while (inReader.hasNextLine())
                            System.out.println(inReader.nextLine());
                    }
                }
            };
            readThread.start();

            while(true) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(System.in));

                String command = reader.readLine();

                if(command.equals("!quit"))  break;
                if(command.equals("!connect")){
                    socket = new Socket(server,port);
                    System.out.println("attempting reconnect");
                    outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
                    InputStream inStream2 = socket.getInputStream();
                    System.out.println("*** Opened OutputStreamWriter.");
                    bwriter = new BufferedWriter(outputStreamWriter);
                    System.out.println("*** Opened BufferedWriter.");
                    sendString(bwriter, "PASS " + password);
                    sendString(bwriter,"NICK "+nickname);

                    readThread = new Thread(){

                        public void run(){
                            while(!this.isInterrupted()) {
                                Scanner inReader = new Scanner(inStream2);

                                while (inReader.hasNextLine())
                                    System.out.println(inReader.nextLine());
                            }
                        }
                    };
                    readThread.start();
                }


                System.out.println("sending: " + command);

                sendString(bwriter, command);
            }

            readThread.interrupt();


            //sendString(bwriter,"PRIVMSG "+channel+" :"+message);

            bwriter.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}