package socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class ChatSverThread implements Runnable {

    Socket child; // Socket class type 변수 child 선언
    BufferedReader ois; // BufferReader class type 변수 ois 선언
    PrintWriter oos; // PrintWriter class type 변수 oos 선언

    String user_id; // String 변수 user_id 선언
    HashMap<String, PrintWriter> hm; // 접속자 관리, 컬렉션 HashMap의 키값 String에 값 PrintWriter의 변수 hm 선언
    InetAddress ip; // InetAddress 클래스 타입의 변수 ip 선언
    String msg; // 문자열 변수 msg 선언

    public ChatSverThread(Socket s, HashMap<String, PrintWriter> h) {
        child = s; // 인자로 받은 변수 s를 child에 대입(Socket)
        hm = h; // 인자로 받은 변수 h를 hm에 대입(HashMap)

        try {
            /*
            BufferReader 객체를 생성 시 InputStreamReader 객체로 인자를 받고
            InputStreamReader 객체를 생성시에는 child(Socket)에 getInputStream()함수를 호출 후
            InputStream을 리턴하여 인자로 받고 InputStreamReader 객체를 생성
            BufferReader로 생성된 객체를 ois에 대입
             */
            ois = new BufferedReader(new InputStreamReader(child.getInputStream()));

            /*
            PrintWriter 객체 생성 시 child(Socket)에 getOutputStream()함수 호출 후
            OutputStream을 리턴하여 인자로 받고 PrintWriter 객체 생성
            PrintWriter로 생성 된 객체를 oos에 대입
             */
            oos = new PrintWriter(child.getOutputStream());

            user_id = ois.readLine(); // ois의 readLine함수를 호출하여 한줄의 문자열을 읽어서 user_id에 대입
            ip = child.getInetAddress(); // child(Socket)을 통해서 getInetAddress() 함수를 통해 Client Ip 주소를 무자여롤 받아 ip에 대입

            System.out.println(ip + "로 부터 " + user_id + "님이 접속하였습니다."); // 출력

            broadcast(user_id + "님이 접속하셨습니다."); // broadcast 함수 호출, 호출 시 문자열 인자로 대입

            synchronized (hm) { // 임계 영역 설정, HashMap에 추가 시
                hm.put(user_id, oos); // HashMap에 키 : user_id(String) 값 : oos(PrintWriter)를 추가함
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        String receiveData; // 문자열 변수 receiveData 선언

        try {
            while ((receiveData = ois.readLine()) != null) { // ois의 readLine 함수를 호출하여 한줄 씩 receiveData에 대입
                if (receiveData.equals("/quit")) {
                    synchronized (hm) {
                        hm.remove(user_id);
                    }
                    break;
                } else if (receiveData.indexOf("/to") >= 0) {
                    sendMsg(receiveData);
                } else {
                    System.out.println(user_id + " >> " + receiveData); // 출력
                    broadcast(user_id + " >> " + receiveData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            synchronized (hm) {
                hm.remove(user_id);
                broadcast(user_id + "님이 퇴장했습니다.");
                System.out.println(user_id + "님이 퇴장했습니다.");
            }

            try {
                if (child != null) {
                    ois.close();
                    oos.close();
                    child.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void broadcast(String message) {
        synchronized (hm) {
            try {
                for (PrintWriter oos : hm.values()) {
                    oos.println(message);
                    oos.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMsg(String message) {
        int begin = message.indexOf(" ") + 1;
        int end = message.indexOf(" ", begin);

        if (end != -1) {
            String id = message.substring(begin, end);
            String msg = message.substring(end + 1);
            PrintWriter oos = hm.get(id);

            try {
                if (oos != null) {
                    oos.println(user_id + "님이 다음과 같은 귓속말을 보내셨습니다. >> " + msg);
                    oos.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
